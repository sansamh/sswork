package io.sansam.sswork.common.resp;

import lombok.Data;

/**
 * <p>
 * ErroResult 业务处理异常统一返回此对象
 * </p>
 *
 * @author houcb
 * @since 2019-10-21 16:03
 */
@Data
public class ErrorResult extends Throwable {

    private static final long serialVersionUID = -2139914967958859787L;

    private Integer code;
    private String message;
    private Object errors;
    private ResultCode resultCode;

    public ErrorResult() {
    }

    public ErrorResult(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.resultCode = resultCode;
    }

    public ErrorResult(ResultCode resultCode, Object errors) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.errors = errors;
        this.resultCode = resultCode;
    }

}
