package com.bookrecovery.entry.enums;

public enum ErrorCodeEnum {
	
	Error_input("error_input","入参不符合规范"),
	Error_input_stream("error_input","请选择要上传的照片"),
	Sorry_info("sorry","后台开小差了，请稍后再试"),
	Outof_verifyTimes("Outof_verifyTimes","20分钟内只能校验5次"),
	Count_Pwd_notMatch("Count_Pwd_notMatch","账号密码不匹配"),
	Verify_Code_error("Verify_Code_error","验证码不正确"),
	Success("success","成功"),
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
