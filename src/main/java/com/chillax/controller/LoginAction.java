package com.chillax.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chillax.entry.EmployeeInfo;
import com.chillax.entry.enums.ErrorCodeEnum;
import com.chillax.entry.result.SingleResultDO;
import com.chillax.service.IemployeeInfoServiceImpl;
import com.chillax.until.VerifyCodeUtils;
import com.chillax.until.redis.load.RedisManager;

@Controller
@RequestMapping
public class LoginAction {

	private static final Logger log = LoggerFactory.getLogger(LoginAction.class);
	
	@Autowired
	public IemployeeInfoServiceImpl employeeInfoService;
	
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
	
	@RequestMapping(value="/loginVerify")
	public SingleResultDO<String> LoginVerify(HttpServletRequest request, HttpServletResponse response){
		SingleResultDO<String> rtn=new SingleResultDO<String>();
		String id=request.getParameter("inputLoginId");
		String pwd=request.getParameter("inputPassword");
		String getVerifyCode=request.getParameter("verifyCode");
		if(StringUtils.isBlank(id)||StringUtils.isBlank(pwd)||StringUtils.isBlank(getVerifyCode)){
			rtn.setSuccess(false);
			rtn.setErrorCode(ErrorCodeEnum.Error_input.getErrorCode());
			rtn.setErrorDesc(ErrorCodeEnum.Error_input.getErrorMessage());
			return rtn;
		}
		HttpSession session = request.getSession(true);
		String verifyCode=String.valueOf(session.getAttribute("verifyCode"));
		if(!getVerifyCode.equals(verifyCode)){
			rtn.setSuccess(false);
			rtn.setErrorCode(ErrorCodeEnum.Verify_Code_error.getErrorCode());
			rtn.setErrorDesc(ErrorCodeEnum.Verify_Code_error.getErrorMessage());
			return rtn;
		}
        String verifyTimes=session.getId()+"_verifyTimes";
        long times=RedisManager.getCountByKeyAndInc("GROUP_0", verifyTimes);
        RedisManager.expire("GROUP_0", verifyTimes, 20*60);
        if(times<5){
        	EmployeeInfo employee=new EmployeeInfo();
        	employee.setId(id);
        	employee.setPwd(pwd);
        	List<EmployeeInfo> result=employeeInfoService.queryEmployeeInfoList(employee);
        	if(result==null||result.size()==0){
        		int rtnTimes=0;
        		if(times<5){
        			rtnTimes=(int) (5-times);
        		}
        		rtn.setResult(String.valueOf(rtnTimes));
        		rtn.setSuccess(false);
        		rtn.setErrorCode(ErrorCodeEnum.Count_Pwd_notMatch.getErrorCode());
        		rtn.setErrorDesc(ErrorCodeEnum.Count_Pwd_notMatch.getErrorMessage());
        	}else{
        		rtn.setSuccess(true);
        		rtn.setErrorCode(ErrorCodeEnum.Success.getErrorCode());
        		rtn.setErrorDesc(ErrorCodeEnum.Success.getErrorMessage());
        	}
        }else{
        	rtn.setResult("0");
        	rtn.setSuccess(false);
        	rtn.setErrorCode(ErrorCodeEnum.Outof_verifyTimes.getErrorCode());
        	rtn.setErrorDesc(ErrorCodeEnum.Outof_verifyTimes.getErrorMessage());
        }
        return rtn;
	}
}
