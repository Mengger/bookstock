package com.bookrecovery.controller;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookrecovery.entry.enums.WechatQREnum;
import com.bookrecovery.entry.result.SingleResultDO;
import com.bookrecovery.service.WechatTokenService;
import com.bookrecovery.until.AesException;
import com.bookrecovery.until.SHA1;
import com.bookrecovery.until.VerifyCodeUtils;

@Controller
@RequestMapping
public class WechatVerifyAction {

	private static final Logger log = LoggerFactory.getLogger(WechatVerifyAction.class);
	
	@RequestMapping(method = RequestMethod.GET,value="/wechatVerifyCode")
	@ResponseBody
	public SingleResultDO<WechatSannerQR> getWechatVerifyCode(String wechatQRcode) throws AesException{
		SingleResultDO<WechatSannerQR> rtn=new SingleResultDO<WechatVerifyAction.WechatSannerQR>();
		String timestamp=String.valueOf(new Date().getTime()).substring(0, 10);
		String nonceStr=VerifyCodeUtils.generateVerifyCode(10);
		String signature=SHA1.getScanneORSignature(WechatTokenService.getJsapiTicket(), nonceStr, timestamp, WechatQREnum.getByType(wechatQRcode).getPath());
		WechatSannerQR rut=new WechatSannerQR();
		rut.setNonceStr(nonceStr);
		rut.setSignature(signature);
		rut.setTimestamp(timestamp);
		rtn.setResult(rut);
		return rtn;
	}
	
	
	class WechatSannerQR{
		private String timestamp;
		private String nonceStr;
		private String signature;
		public String getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
		public String getNonceStr() {
			return nonceStr;
		}
		public void setNonceStr(String nonceStr) {
			this.nonceStr = nonceStr;
		}
		public String getSignature() {
			return signature;
		}
		public void setSignature(String signature) {
			this.signature = signature;
		}
		
	}
}
