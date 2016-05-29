package com.chillax.entry.result;

/**
 * 基础返回结果
 */
public class BaseResultDO extends BaseDO {

    private static final long serialVersionUID = 3962215109252373857L;

    private boolean success = true;        // 操作是否成功
    private String errorCode;
    private String errorDesc;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    /**
     * 当前操作是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置操作结果
     *
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

}
