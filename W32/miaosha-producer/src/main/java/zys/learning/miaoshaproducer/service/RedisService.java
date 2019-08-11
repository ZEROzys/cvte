package zys.learning.miaoshaproducer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String get(String key) {
        ValueOperations<String, String > vo =  redisTemplate.opsForValue();
        return vo.get(key);
    }
    public void set(String key, String value) {
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }


}
