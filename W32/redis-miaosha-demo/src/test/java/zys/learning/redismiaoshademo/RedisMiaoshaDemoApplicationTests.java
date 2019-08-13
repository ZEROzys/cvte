package zys.learning.redismiaoshademo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import zys.learning.redismiaoshademo.service.RedisService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisMiaoshaDemoApplicationTests {

    @Autowired
    private RedisService redisService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void rollback() {
        redisService.rollback("product:1", "740");
    }
}
