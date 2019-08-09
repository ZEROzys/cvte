package zys.learning.redismiaoshademo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RedisService {

    private Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void set(String key, String value) {
        //更改在redis里面查看key编码问题
//        RedisSerializer redisSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(redisSerializer);
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    public void decr(String key, int count) {
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        vo.decrement(key, count);
    }

    public String get(String key) {
        ValueOperations<String, String > vo =  redisTemplate.opsForValue();
        return vo.get(key);
    }

    public void setList(String key, String username) {
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        opsForList.rightPush(key, username);
    }

    public boolean setNx(String lockKey, String expireTime) {
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        return vo.setIfAbsent(lockKey, expireTime);
    }

    public String getAndSet(String key, String val) {
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        return vo.getAndSet(key, val);
    }

    public void delKey(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    public boolean secKill(String productId, String username) {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/script.lua")));
        redisScript.setResultType(String.class);
        String res = redisTemplate.execute(redisScript, Arrays.asList(productId), username);
        return res.equals("1");

    }
}
