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
import org.springframework.transaction.annotation.Transactional;
import zys.learning.redismiaoshademo.common.RedisFix;
import zys.learning.redismiaoshademo.pojo.Order;
import zys.learning.redismiaoshademo.pojo.Product;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class RedisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisService.class);

    private StringRedisTemplate redisTemplate;
    private OrderService orderService;
    private ProductService productService;

    @Autowired
    public RedisService(StringRedisTemplate redisTemplate, OrderService orderService,
                        ProductService productService) {
        this.redisTemplate = redisTemplate;
        this.orderService = orderService;
        this.productService = productService;
    }

    /***
     * redis 秒杀功能
     * @param productId
     * @param username
     * @return
     */
    public boolean secKill(String productId, String username) {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/seckill.lua")));
        redisScript.setResultType(String.class);
        String res = redisTemplate.execute(redisScript, Arrays.asList(productId), username);
        return res.equals("1");
    }

    public Set<String> getAllKeys() {
        Set<String> keys = redisTemplate.keys("*");
        return keys;
    }

    public List<String> getListByKey(String key) {
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        List<String> users = opsForList.range(key, 0, -1);
        return users;
    }

    /***
     * 数据库保存订单失败，redis进行回滚操作，product库存量加1，删除对应list中的用户
     * @param productId
     * @param username
     */
    public void rollback(String productId, String username) {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/rollback.lua")));
        redisScript.setResultType(String.class);
        redisTemplate.execute(redisScript, Arrays.asList(productId), username);
    }

    @Transactional(rollbackFor = Exception.class)
    public void transData2Db() throws Exception{
        for (String key: getAllKeys()) {
            if (key.endsWith(RedisFix.PRODUCT_USERS_SUFFIX)) {
                List<String> users = getListByKey(key);
                String[] strings = key.split(":");
                for (String s: users) {
                    Order order = new Order(Long.valueOf(strings[1]), Long.valueOf(s));
                    if (orderService.save(order) == null) {
                        throw new Exception("保存订单错误");
                    }
                }
                if (!delKey(key))
                    throw new Exception("删除键值出现错误");
            }
            if (key.startsWith(RedisFix.PRODUCT_PREFIX)
                    && !key.endsWith(RedisFix.PRODUCT_USERS_SUFFIX)) {
                int stock = Integer.valueOf(getVal(key));
                String[] strings = key.split(":");
                Product p = productService.getProductById(Long.valueOf(strings[1]));
                p.setStock(stock);
                if (productService.save(p) == null)
                    throw new Exception("更新库存失败");
                if (!delKey(key))
                    throw new Exception("删除键值出现错误");
            }
        }
    }

    public boolean delKey(String key) {
        if (key == null) return false;
        else
            return redisTemplate.delete(key);

    }

    public String getVal(String key) {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        return opsForValue.get(key);
    }
}
