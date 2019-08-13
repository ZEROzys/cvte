package zys.learning.redismiaoshademo.common;

public class RedisFix {

    public static final String PRODUCT_PREFIX = "product:";
    public static final String PRODUCT_USERS_SUFFIX = ":users";

    private String redisfix;

    public RedisFix(String redisfix) {
        this.redisfix = redisfix;
    }

}
