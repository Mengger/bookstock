package com.bookrecovery.entry.enums;

public enum WechatQREnum {

	ScannerBookInfo("scannerBookInfo","http://www.bookrecovery.cn/BookProject/","测试二维码接口");
	final private String type;
	final private String path;
	final private String desc;
	public String getType() {
		return type;
	}
	public String getPath() {
		return path;
	}
	public String getDesc() {
		return desc;
	}
	public static WechatQREnum getByType(String type){
		if(type==null){
			return null;
		}
		for(WechatQREnum wechatQREnum:WechatQREnum.values()){
			if(type.equals(wechatQREnum.getType())){
				return wechatQREnum;
			}
		}
		return null;
	}
	private WechatQREnum(String type, String path, String desc) {
		this.type = type;
		this.path = path;
		this.desc = desc;
	}
	
}
