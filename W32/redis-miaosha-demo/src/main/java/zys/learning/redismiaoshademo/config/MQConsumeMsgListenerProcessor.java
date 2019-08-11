package zys.learning.redismiaoshademo.config;

import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zys.learning.redismiaoshademo.pojo.Order;
import zys.learning.redismiaoshademo.service.OrderService;
import zys.learning.redismiaoshademo.service.RedisService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MQConsumeMsgListenerProcessor implements MessageListenerConcurrently {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQConsumeMsgListenerProcessor.class);
    private static final String PRODUCT_PREFIX = "product:1";

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderService orderService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(
            List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        for (MessageExt me: msgs) {
            String[] msg = new String(me.getBody()).split(",");
            LOGGER.info("收到来自{}的订单", msg[1]);
            if (redisService.secKill(PRODUCT_PREFIX, msg[1])) {
                Order order = new Order(Long.valueOf(msg[0]), Long.valueOf(msg[1]));
                orderService.save(order);
            }
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}