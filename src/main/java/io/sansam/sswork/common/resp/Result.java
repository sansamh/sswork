package io.sansam.sswork.common.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * Result 使用ResponseResult注解的类或者方法，方法返回值会被包装成Result对象
 * </p>
 *
 * @author houcb
 * @since 2019-10-21 15:01
 */
@Data
public class Result implements Serializable {

    private static final long serialVersionUID = -4488207575370477275L;

    private Integer code;
    private String message;
    private Object data;
    private boolean success;

    private Result() {
    }

    public Result(ResultCode resultCode, Object data, boolean success) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.data = data;
        this.success = success;
    }

    /**
     * 成功 不带参数
     * @return
     */
    public static Result success() {
        return buildResult(ResultCode.SUCCESS, null, true);
    }

    /**
     * 成功 带参数
     * @param data 返回的数据
     * @return
     */
    public static Result success(Object data) {
        return buildResult(ResultCode.SUCCESS, data, true);
    }


    /**
     * 失败 不带参数
     * @param resultCode resultCode
     * @return
     */
    public static Result failure(ResultCode resultCode) {
        return buildResult(resultCode, null, false);
    }

    /**
     * 失败 带参数
     * @param resultCode resultCode
     * @return
     */
    public static Result failure(ResultCode resultCode, Object data) {
        return buildResult(resultCode, data, false);
    }

    private void setResultCode(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    private static Result buildResult(ResultCode resultCode, Object data, boolean success) {
        Result result = new Result();
        result.setResultCode(resultCode);
        if (!Objects.isNull(data)) {
            result.setData(data);
        }
        result.setSuccess(success);
        return result;
    }


}
