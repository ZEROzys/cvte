package zys.practice.seckillcommon.common;

/***
 * 封装通用返回响应结构
 */
public enum CommonReponse {

    SCKILL_SUCCESS(0, "秒杀成功"),
    SCKILL_IN_QUEUE(40010, "排队中，请耐心等待"),
    SCKILL_FAILURE(40020, "秒杀失败"),
    PRODUCT_NO_STOCK(40030, "该商品已被抢购一空"),
    USER_REPEAT_SCKILL(40040, "您已成功秒杀过该商品"),
    PRODUCT_NOT_EXIST(40050, "商品不存在");

    private final int code;
    private final String msg;

    private CommonReponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
