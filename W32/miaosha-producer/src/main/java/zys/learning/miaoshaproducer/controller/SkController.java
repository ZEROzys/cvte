package zys.learning.miaoshaproducer.controller;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zys.learning.miaoshaproducer.pojo.Product;
import zys.learning.miaoshaproducer.service.ProductService;
import zys.learning.miaoshaproducer.service.RedisService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("")
public class SkController implements InitializingBean {

    private static final String TOPIC = "SECKILL";
    private static final String PRODUCT_PREFIX = "product:";
    private static final Logger LOGGER = LoggerFactory.getLogger(SkController.class);

    @Autowired
    private DefaultMQProducer producer;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductService productService;

//    private AtomicInteger stock = new AtomicInteger(0);

    @PostMapping("/seckill")
    public String secKill(String username, String productId) {
        // 前置校验
        // 1. 订单参数校验
        // TODO

        // 2. 商品信息校验
        if (redisService.get(PRODUCT_PREFIX + productId) == null) {
            LOGGER.info("此商品不在秒杀活动中");
            return "此商品不在秒杀活动中";
        }

        if (Integer.valueOf(redisService.get(PRODUCT_PREFIX + productId)) <= 0) {
            LOGGER.info("该商品已被秒杀完");
            return "该商品已被秒杀完";
        }

        StringBuffer sb = new StringBuffer(productId);
        sb.append(","+username);
        // 组装Message消息体
        Message message = new Message(TOPIC, productId, sb.toString().getBytes());
        try {
            SendResult send = producer.send(message);
            // 判断 SendStatus
            if (send == null) {
                return "下单失败";
            }
            if (send.getSendStatus() != SendStatus.SEND_OK) {
                return "下单失败";
            }
            LOGGER.info(send.getMsgId());
            return "秒杀成功";
        } catch (Exception e) {
            e.printStackTrace();
        }
//        int expect = stock.get();
//        while (expect < 10) {
//
//            if (stock.compareAndSet(expect, expect + 1)) {
//                logger.info(String.valueOf(stock.get()));
//                try {
//                    Message message = new Message(TOPIC, productId, username.getBytes());
//                    SendResult send = producer.send(message);
//                    logger.info(send.getSendStatus().toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return "秒杀成功";
//            }
//        }
//        producer.shutdown();
        return "秒杀失败";
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        List<Product> products = productService.getAll();

        for (Product p: products) {
            redisService.set(PRODUCT_PREFIX + p.getId(), String.valueOf(p.getStock()));
        }
    }
}
