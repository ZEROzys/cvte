package zys.learning.miaoshaproducer.controller;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zys.learning.miaoshaproducer.common.BaseResponse;
import zys.learning.miaoshaproducer.pojo.Product;
import zys.learning.miaoshaproducer.service.ProductService;
import zys.learning.miaoshaproducer.service.RedisService;

import java.util.List;

@RestController
@RequestMapping("")
public class SkController implements InitializingBean {

    private static final String TOPIC = "SECKILL";
    private static final String PRODUCT_PREFIX = "product:";
    private static final String ORDER_LIST_POSTFIX = ":users";
    private static final Logger LOGGER = LoggerFactory.getLogger(SkController.class);

    @Autowired
    private DefaultMQProducer producer;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductService productService;

    /***
     * 秒杀接口
     * @param username
     * @param productId
     * @return
     */
    @PostMapping("/seckill")
    public BaseResponse secKill(String username, String productId) {
        // 前置校验
        // 1. 订单参数校验
        // TODO

        // 2. 商品信息校验
        if (redisService.get(PRODUCT_PREFIX + productId) == null) {
            LOGGER.info("此商品不在秒杀活动中");
            return BaseResponse.PRODUCT_NOT_EXIST;
        }

        if (Integer.valueOf(redisService.get(PRODUCT_PREFIX + productId)) <= 0) {
            LOGGER.info("该商品已被秒杀完");
            return BaseResponse.PRODUCT_NO_STOCK;
        }

        // 3. 判断是否有重复秒杀
        String orderListKey = PRODUCT_PREFIX + productId + ORDER_LIST_POSTFIX;
        if (redisService.hasKey(orderListKey))
            if (redisService.findInList(orderListKey, username))
                return BaseResponse.USER_REPEAT_SCKILL;

        StringBuffer sb = new StringBuffer(productId);
        sb.append(","+username);
        //  组装Message消息体
        Message message = new Message(TOPIC, productId, sb.toString().getBytes());
        //  同步消息
//        try {
//            SendResult send = producer.send(message);
//            // 判断 SendStatus
//            if (send == null) {
//                return BaseResponse.SCKILL_FAILURE;
//            }
//            if (send.getSendStatus() != SendStatus.SEND_OK) {
//                return BaseResponse.SCKILL_FAILURE;
//            }
//            LOGGER.info(send.getMsgId());
//            return BaseResponse.SCKILL_IN_QUEUE;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //  异步消息
        try {
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    LOGGER.info("消息{}发送成功", sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace();
                }
            }, 1000);
            return BaseResponse.SCKILL_IN_QUEUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BaseResponse.SCKILL_FAILURE;
    }

    /***
     * 轮询接口，供前端轮询判断当前用户是否秒杀成功
     * @param username 用户id
     * @param productId 商品id
     * @return
     */
    @GetMapping("/seckillPolling")
    public BaseResponse seckillPolling(String username, String productId) {
        String key = PRODUCT_PREFIX + productId + ORDER_LIST_POSTFIX;

        if (redisService.findInList(key, username))
            return BaseResponse.SCKILL_SUCCESS;
        else
            return BaseResponse.SCKILL_FAILURE;
    }

    @Override
    public void afterPropertiesSet() {
        List<Product> products = productService.getAll();

        for (Product p: products) {
            redisService.set(PRODUCT_PREFIX + p.getId(), String.valueOf(p.getStock()));
        }
    }
}
