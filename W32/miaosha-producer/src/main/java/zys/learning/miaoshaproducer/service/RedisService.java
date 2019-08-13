package zys.learning.miaoshaproducer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public boolean findInList(String key, String username) {
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        List<String> users = opsForList.range(key, 0, -1);
        return users.contains(username);
    }

    public String get(String key) {
        ValueOperations<String, String > vo =  redisTemplate.opsForValue();
        return vo.get(key);
    }
    public void set(String key, String value) {
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }
}
