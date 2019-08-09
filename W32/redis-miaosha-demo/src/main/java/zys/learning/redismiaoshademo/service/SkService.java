package zys.learning.redismiaoshademo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zys.learning.redismiaoshademo.redis.RedisLock;

import java.util.concurrent.TimeUnit;

@Service
public class SkService {

    private Logger logger = LoggerFactory.getLogger(SkService.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisLock redisLock;

    private static final int TIMEOUT = 2 * 1000;

    public boolean secKill(String productId, String username, String lock) {
        long lockUntil = System.currentTimeMillis() + TIMEOUT;

        if (!redisLock.lock(lock, String.valueOf(lockUntil))) {
            logger.info("当前排队的人太多，请稍等");
            return false;
        }

        try {
            int store = Integer.parseInt(redisService.get(productId));
            if (store <= 0) {
                logger.info("该商品已售完");
                return false;
            }
            redisService.decr(productId, 1);
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            logger.error(username + "成功秒杀");
            return true;
        } finally {
            redisLock.release(lock, String.valueOf(lockUntil));
        }

    }

}
