package com.chillax.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chillax.entry.result.SingleResultDO;
import com.chillax.until.VerifyCodeUtils;
import com.chillax.until.redis.load.RedisManager;

@Controller
@RequestMapping
public class LoginAction {

	private static final Logger log = LoggerFactory.getLogger(LoginAction.class);
	
	@RequestMapping(method = RequestMethod.GET,value="/verifyCode")
	public void GetImageStream(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
        response.setContentType("image/jpeg");  
        //生成随机字串  
        String verifyCode = VerifyCodeUtils.generateVerifyCode(5);  
        //存入会话session  
        HttpSession session = request.getSession(true);  
        session.setAttribute("verifyCode", verifyCode.toLowerCase());
        //generate verify code image
        int w = 120, h = 44;  
        try {
        	VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        } catch (IOException e) {
        	log.error("generate verify code error",e);
        }  
	}
	
	@RequestMapping(method = RequestMethod.GET,value="/loginVerify")
	public SingleResultDO<String> LoginVerify(HttpServletRequest request, HttpServletResponse response){
		SingleResultDO<String> rtn=new SingleResultDO<String>();
		HttpSession session = request.getSession(true);
        String verifyTimes=session.getId()+"_verifyTimes";
        long times=RedisManager.getCountByKeyAndInc("GROUP_0", verifyTimes);
        RedisManager.expire("GROUP_0", verifyTimes, 20*60);
        if(times>5){
        	
        }
        return rtn;
	}
}
