package zys.learning.miaoshaproducer.common;

/***
 * 封装通用返回响应结构
 */
public class BaseResponse {
    private int code;
    private String msg;

    // 秒杀模块
    public static BaseResponse SCKILL_SUCCESS = new BaseResponse(0, "秒杀成功");
    public static BaseResponse SCKILL_IN_QUEUE = new BaseResponse(40010, "排队中，请耐心等待");
    public static BaseResponse SCKILL_FAILURE = new BaseResponse(40020, "秒杀失败");
    public static BaseResponse PRODUCT_NO_STOCK = new BaseResponse(40030, "该商品已被抢购一空");
    public static BaseResponse USER_REPEAT_SCKILL = new BaseResponse(40040, "您已成功秒杀过该商品");
    public static BaseResponse PRODUCT_NOT_EXIST = new BaseResponse(40050, "商品不存在");
    // 用户模块
    public static BaseResponse USER_NOT_EXIST = new BaseResponse(50010, "用户不存在");
    public static BaseResponse USER_NOT_LOGIN = new BaseResponse(50020, "未登录");

    private BaseResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

