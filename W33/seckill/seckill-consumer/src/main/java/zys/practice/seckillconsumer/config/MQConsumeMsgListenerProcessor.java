package zys.practice.seckillconsumer.config;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zys.practice.seckillconsumer.service.WebSocketServer;
import zys.practice.seckillcommon.common.RedisFix;
import zys.practice.seckillcommon.pojo.Order;
import zys.practice.seckillcommon.service.ProductService;
import zys.practice.seckillcommon.service.RedisService;
import java.util.List;

@Component
public class MQConsumeMsgListenerProcessor implements MessageListenerConcurrently {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQConsumeMsgListenerProcessor.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductService productService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(
            List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        for (MessageExt me: msgs) {
            String[] msg = new String(me.getBody()).split(",");
            if (redisService.secKill(RedisFix.PRODUCT_PREFIX+msg[0], msg[1])) {

                //  这里保存到缓存的同时保存在数据库中，也可以先保存在缓存，通过一个定时任务保证缓存数据库的一致性
                Order order = new Order(Long.valueOf(msg[0]), Long.valueOf(msg[1]));
                //  数据库保存失败
                try {
                    if (productService.save(order)) {
                        LOGGER.info("{}秒杀成功", msg[1]);
                        WebSocketServer.sendInfo("秒杀成功", msg[1]);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    else  {
                        redisService.rollback(RedisFix.PRODUCT_PREFIX+msg[0], msg[1]);
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

//                LOGGER.info("{}秒杀成功", msg[1]);
            }
            else
                LOGGER.info("{}秒杀失败", msg[1]);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}