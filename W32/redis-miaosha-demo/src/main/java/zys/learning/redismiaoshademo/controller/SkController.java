package zys.learning.redismiaoshademo.controller;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zys.learning.redismiaoshademo.service.ProductService;
import zys.learning.redismiaoshademo.service.RedisService;
import zys.learning.redismiaoshademo.service.SkService;

@RestController
@RequestMapping("")
public class SkController {

    private Logger logger = LoggerFactory.getLogger(SkController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisService redisService;

    @PostMapping("/seckill")
    public String seckill(String username, String productId) throws Exception {

        if (productService.updateStock(Long.valueOf(productId), Long.valueOf(username)))
            return "秒杀成功";

//        logger.info(username + " " + productId);
//        if (skService.secKill(productId, username, productId + "::lock")) {
//            redisService.setList(productId+"::users", username);
//            return "秒杀成功";
//        }
//        return "秒杀失败";

//        if (redisService.secKill(productId, username))
//            return "秒杀成功";
        return "秒杀失败";
    }
}
