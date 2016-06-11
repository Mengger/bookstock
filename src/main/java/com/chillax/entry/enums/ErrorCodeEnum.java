package com.chillax.entry.enums;

public enum ErrorCodeEnum {
	
	Error_input("error_input","入参不符合规范"),
	Error_input_stream("error_input","请选择要上传的照片"),
	Sorry_info("sorry","后台开小差了，请稍后再试"),
	Book_exit("bookinfo is exit","该书本信息已存在");
	 private final String errorCode;
    private final String errorMessage;

    private ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
