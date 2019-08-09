package zys.learning.miaoshaproducer.controller;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("")
public class SkController {

    private static String Topic = "SECKILL";
    private Logger logger = LoggerFactory.getLogger(SkController.class);

    @Autowired
    private DefaultMQProducer producer;

    private AtomicInteger stock = new AtomicInteger(0);

    @PostMapping("/seckill")
    public String secKill(String username, String productId) {
        int expect = stock.get();
        while (expect < 10) {

            if (stock.compareAndSet(expect, expect + 1)) {
                logger.info(String.valueOf(stock.get()));
                try {
                    Message message = new Message(Topic, productId, username.getBytes());
                    SendResult send = producer.send(message);
                    logger.info(send.getSendStatus().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "秒杀成功";
            }
        }
//        producer.shutdown();
        return "秒杀失败";
    }

}
