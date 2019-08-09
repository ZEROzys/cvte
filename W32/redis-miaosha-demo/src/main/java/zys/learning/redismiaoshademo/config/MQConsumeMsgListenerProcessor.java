package zys.learning.redismiaoshademo.config;

import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zys.learning.redismiaoshademo.service.RedisService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MQConsumeMsgListenerProcessor implements MessageListenerOrderly {
    private static final Logger logger = LoggerFactory.getLogger(MQConsumeMsgListenerProcessor.class);

//    private AtomicInteger consumeTimes = new AtomicInteger(0);

    @Autowired
    private RedisService redisService;

    @Override
    public ConsumeOrderlyStatus consumeMessage(
            List<MessageExt> list, ConsumeOrderlyContext context) {
        context.setAutoCommit(true);
//        this.consumeTimes.incrementAndGet();
        String username = new String(list.get(0).getBody());
        logger.info(username);
        redisService.secKill("product::1", username);
        return ConsumeOrderlyStatus.SUCCESS;
    }
}