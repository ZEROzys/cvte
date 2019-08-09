package zys.learning.redismiaoshademo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zys.learning.redismiaoshademo.service.RedisService;

@Component
public class RedisLock {
    @Autowired
    private RedisService redisService;

    /**
     * 加锁
     *
     * @param lockKey   加锁的Key
     * @param expireTime 过期时间：当前时间+超时时间
     * @return
     */
    public boolean lock(String lockKey, String expireTime) {
        if (redisService.setNx(lockKey, expireTime)) {
            // 对应setnx命令，可以成功设置,也就是key不存在，获得锁成功
            return true;
        }

        //  设置失败，获得锁失败
        //  判断锁超时 - 防止原来的操作异常，没有运行解锁操作 ，防止死锁
        String currentLock = redisService.get(lockKey);
        //  如果锁过期 currentLock不为空且小于当前时间
        if (!StringUtils.isEmpty(currentLock) && Long.parseLong(currentLock) < System.currentTimeMillis()) {
            //  如果lockKey对应的锁已经存在，获取上一次设置的时间戳之后并重置lockKey对应的锁的时间戳
            String preLock = redisService.getAndSet(lockKey, expireTime);

            //  假设两个线程同时进来这里，因为key被占用了，而且锁过期了。
            //  获取的值currentLock=A(get取的旧的值肯定是一样的),两个线程的timeStamp都是B,key都是K.锁时间已经过期了。
            //  而这里面的getAndSet一次只会一个执行，也就是一个执行之后，上一个的timeStamp已经变成了B。
            //  只有一个线程获取的上一个值会是A，另一个线程拿到的值是B。
            if (!StringUtils.isEmpty(preLock) && preLock.equals(currentLock)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey
     * @param expireTime
     */
    public void release(String lockKey, String expireTime) {
        try {
            String currentValue = redisService.get(lockKey);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(expireTime)) {
                // 删除锁状态
                redisService.delKey(lockKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        redisService.delKey(lockKey);
    }
}

