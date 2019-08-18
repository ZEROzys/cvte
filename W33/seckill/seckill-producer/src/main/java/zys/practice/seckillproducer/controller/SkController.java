package zys.practice.seckillproducer.controller;

import com.google.common.util.concurrent.RateLimiter;
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
import zys.practice.seckillcommon.common.CommonReponse;
import zys.practice.seckillcommon.pojo.Product;
import zys.practice.seckillcommon.service.ProductService;
import zys.practice.seckillcommon.service.RedisService;
import java.util.List;

@RestController
@RequestMapping("")
public class SkController implements InitializingBean {

    private static final String TOPIC = "SECKILL";
    private static final String PRODUCT_PREFIX = "product:";
    private static final String ORDER_LIST_POSTFIX = ":uId:";
    private static final Logger LOGGER = LoggerFactory.getLogger(SkController.class);

    @Autowired
    private DefaultMQProducer producer;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductService productService;

    private RateLimiter rateLimiter = RateLimiter.create(20);

    /***
     * 秒杀接口
     */
    @PostMapping("/seckill")
    public CommonReponse secKill(String username, String productId) {
        if (!rateLimiter.tryAcquire()) {
            LOGGER.info("{}被限流了", username);
            return CommonReponse.PRODUCT_NO_STOCK;
        }
        LOGGER.info("{}进来了", username);

        // TODO：前置校验
        // 1. 订单参数校验

        // 2. 商品信息校验
        String stock = redisService.get(PRODUCT_PREFIX + productId);
        if (stock == null) {
            LOGGER.info("此商品不在秒杀活动中");
            return CommonReponse.PRODUCT_NOT_EXIST;
        }

        if (Integer.valueOf(stock) <= 0) {
            LOGGER.info("该商品已被秒杀完");
            return CommonReponse.PRODUCT_NO_STOCK;
        }

        // 3. 判断是否有重复秒杀
        String pUKey = PRODUCT_PREFIX + productId + ORDER_LIST_POSTFIX + username;
        if (redisService.hasKey(pUKey))
            return CommonReponse.USER_REPEAT_SCKILL;

        String messageBody = productId + "," + username;
        //  组装Message消息体
        Message message = new Message(TOPIC, productId, messageBody.getBytes());
        //  异步消息
        try {
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    LOGGER.info("来自{}的请求成功入队", username);
                }

                @Override
                public void onException(Throwable e) {
                    LOGGER.info("来自{}的请求超时", username);
                }
            }, 2000);
            return CommonReponse.SCKILL_IN_QUEUE;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CommonReponse.SCKILL_FAILURE;
    }

    /***
     * 轮询接口，供前端轮询判断当前用户是否秒杀成功
     */
    @GetMapping("/seckillPolling")
    public CommonReponse seckillPolling(String username, String productId) {
        String pUKey = PRODUCT_PREFIX + productId + ORDER_LIST_POSTFIX + username;
        if (redisService.hasKey(pUKey))
            return CommonReponse.SCKILL_SUCCESS;
        else
            return CommonReponse.SCKILL_FAILURE;
    }

    @Override
    public void afterPropertiesSet() {
        List<Product> products = productService.getAll();

        for (Product p: products) {
            redisService.set(PRODUCT_PREFIX + p.getId(), String.valueOf(p.getStock()));
        }
    }
}

