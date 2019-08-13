package zys.learning.redismiaoshademo.config;

import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zys.learning.redismiaoshademo.pojo.Order;
import zys.learning.redismiaoshademo.service.ProductService;
import zys.learning.redismiaoshademo.service.RedisService;
import zys.learning.redismiaoshademo.service.WebSocketServer;

import java.util.List;

@Component
public class MQConsumeMsgListenerProcessor implements MessageListenerConcurrently {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQConsumeMsgListenerProcessor.class);
    private static final String PRODUCT_PREFIX = "product:1";

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductService productService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(
            List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        for (MessageExt me: msgs) {
            String[] msg = new String(me.getBody()).split(",");
            LOGGER.info("收到来自{}的订单", msg[1]);
            if (redisService.secKill(PRODUCT_PREFIX, msg[1])) {
                Order order = new Order(Long.valueOf(msg[0]), Long.valueOf(msg[1]));
                //  数据库保存失败
                try {
                    if (productService.save(order)) {
                        WebSocketServer.sendInfo("秒杀成功", msg[1]);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    else  {
                        redisService.rollback(PRODUCT_PREFIX, msg[1]);
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                } catch (Exception e) {
                    LOGGER.error("保存数据库错误", e);
                    if (me.getReconsumeTimes() == 2) {
                        WebSocketServer.sendInfo("秒杀失败", msg[1]);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}