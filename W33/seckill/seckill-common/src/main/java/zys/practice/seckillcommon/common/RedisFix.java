package zys.practice.seckillcommon.common;

public class RedisFix {

    public static final String PRODUCT_PREFIX = "product:";
//    public static final String PRODUCT_USERS_SUFFIX = ":uId:";

    private String redisfix;

    public RedisFix(String redisfix) {
        this.redisfix = redisfix;
    }

}
