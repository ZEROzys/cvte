package zys.learning.redismiaoshademo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import zys.learning.redismiaoshademo.service.RedisService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @Test
    public void testTrans2Db() {
        try {
            redisService.transData2Db();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
