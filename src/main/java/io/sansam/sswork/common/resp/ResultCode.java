package io.sansam.sswork.common.resp;

/**
 * <p>
 * ResultCode
 * </p>
 *
 * @author houcb
 * @since 2019-10-21 14:49
 */
public enum  ResultCode {

    /**
     * #1000～1999 区间表示参数错误
     * #2000～2999 区间表示用户错误
     * #3000～3999 区间表示接口异常
     * #4000～4999 系统内部异常
     * #5000～5999 服务器异常
     */
    // 成功
    SUCCESS(1, "成功"),

    /**
     * 参数错误
     */
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_NOT_EXISTS(1002, "参数为空"),
    /**
     * 用户权限错误
     */
    USER_NOT_LOGGED_IN(2001, "用户未登录，访问的路径需要验证，请登录"),

    /**
     * 系统内部异常
     */
    SYSTEM_ERROR(5001, "系统内部异常，请联系管理员"),
    ;

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }


}
