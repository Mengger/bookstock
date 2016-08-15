package com.bookrecovery.service;

import com.alibaba.fastjson.JSON;
import com.bookrecovery.entry.WechatConfig;
import com.bookrecovery.entry.wechat.Wechat;
import com.bookrecovery.until.HttpRequest;
import com.bookrecovery.until.redis.load.RedisManager;

public class WechatTokenService {

	private static final String access_tocke_url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+WechatConfig.AppID+"&secret="+WechatConfig.AppSecret;
	
	private static final String jsapi_ticket_url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=";
	
	/**
	 * 获取wechat的token
	 * @return
	 */
	public static String getAccessToken(){
		String token=RedisManager.getValueByKeyAndGroup("GROUP_0", "access_token");
		if(token==null){
			String queryResult=HttpRequest.sendGet(access_tocke_url, null);
			Wechat rtn=JSON.parseObject(queryResult,Wechat.class);
			//token的实效时间是7200s
			token=rtn.getAccess_token();
			RedisManager.setValueByKeyAndGroupSetTime("GROUP_0", "access_token", token, 7200);
		}
		return token;
	}
	
	/**
	 * 获取jsapi_ticket
	 * @return
	 */
	public static String getJsapiTicket(){
		String jsapiTicket=RedisManager.getValueByKeyAndGroup("GROUP_0", "jsapi_ticket");
		if(jsapiTicket==null){	
			String jsapi_ticket_url_new=jsapi_ticket_url+getAccessToken()+"&type=jsapi";
			String queryResult=HttpRequest.sendGet(jsapi_ticket_url_new, null);
			Wechat rtn=JSON.parseObject(queryResult,Wechat.class);
			jsapiTicket=rtn.getTicket();
			RedisManager.setValueByKeyAndGroupSetTime("GROUP_0", "jsapi_ticket", jsapiTicket, 7200);
		}
		return jsapiTicket;
	}
	
}
