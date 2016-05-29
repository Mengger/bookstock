package com.chillax.entry.menu;

public enum ErrorCodeEnum {
	
	Error_input("error_input","入参不符合规范"),
	Sorry_info("sorry","后台开小差了，请稍后再试");
	
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
