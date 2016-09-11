package com.bookrecovery.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookrecovery.entry.EmployeeInfo;
import com.bookrecovery.entry.enums.ErrorCodeEnum;
import com.bookrecovery.entry.result.SingleResultDO;
import com.bookrecovery.service.IemployeeInfoService;
import com.bookrecovery.until.VerifyCodeUtils;
import com.bookrecovery.until.redis.load.RedisManager;

@Controller
@RequestMapping
public class LoginAction {

	private static final Logger log = LoggerFactory.getLogger(LoginAction.class);
	
	@Autowired
	public IemployeeInfoService employeeInfoService;
	
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
	@ResponseBody
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
        times=0;
        RedisManager.expire("GROUP_0", verifyTimes, 20*60);
        if(times<5){
        	EmployeeInfo employee=new EmployeeInfo();
        	employee.setId(Long.valueOf(id));
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
        		request.setAttribute("userId", id);
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
	
	/**
	 * 修改用户信息
	 * @param request
	 * @param birthday	生日
	 * @param eMail		邮箱
	 * @param QQ		🐧号
	 * @param telephone	手机号
	 * @param name		名字
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET,value="/modifyEmployeeInfo")
	public SingleResultDO<Boolean> modifyEmployeeInfo(HttpServletRequest request,Long birthday,String eMail,String QQ,
			String telephone,String name){
		SingleResultDO<Boolean> rtn = new SingleResultDO<Boolean>();
		EmployeeInfo employee = new EmployeeInfo();
		employee.setId((Long)request.getAttribute("userId"));
		if(birthday!=null) employee.setBirthday(new Date(birthday));
		if(QQ!=null&&QQ.length()>7) employee.setQq(QQ);
		if(name!=null&&name.length()>0) employee.setName(name);
		if(eMail!=null&&eMail.length()>0){
			employee.setEMail(eMail);
		}else{
			rtn.setSuccess(false);
			rtn.setResult(false);
			rtn.setErrorCode(ErrorCodeEnum.Error_input.getErrorCode());
			rtn.setErrorDesc(ErrorCodeEnum.Error_input.getErrorMessage());
			return rtn;
		}
		boolean modifyResult = employeeInfoService.modifyEmployeeInfoById(employee);
		rtn.setResult(modifyResult);
		rtn.setErrorCode(ErrorCodeEnum.Success.getErrorCode());
		rtn.setErrorDesc(ErrorCodeEnum.Success.getErrorMessage());
		return rtn;
	}
	
	
	/**
	 * 修改登陆密码
	 * @param request
	 * @param newPWD  新密码
	 * @param oldPWD  老密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,value="/modifyEmployeeInfo")
	public SingleResultDO<Boolean> modifyPWD(HttpServletRequest request,String newPWD,String oldPWD){
		SingleResultDO<Boolean> rtn = new SingleResultDO<Boolean>();
		Long id = (Long)request.getAttribute("userId");
		EmployeeInfo employee=new EmployeeInfo();
    	employee.setId(id);
    	employee.setPwd(oldPWD);
    	List<EmployeeInfo> result=employeeInfoService.queryEmployeeInfoList(employee);
    	if(result==null||result.size()==0){
    		rtn.setResult(false);
    		rtn.setSuccess(false);
    		rtn.setErrorCode(ErrorCodeEnum.Count_Pwd_notMatch.getErrorCode());
    		rtn.setErrorDesc(ErrorCodeEnum.Count_Pwd_notMatch.getErrorMessage());
    	}else{
    		employee.setPwd(newPWD);
    		boolean modifyResult = employeeInfoService.modifyEmployeeInfoById(employee);
    		rtn.setResult(modifyResult);
    		rtn.setSuccess(true);
    		rtn.setErrorCode(ErrorCodeEnum.Success.getErrorCode());
    		rtn.setErrorDesc(ErrorCodeEnum.Success.getErrorMessage());
    	}
		return rtn;
	}
	
	
	/**
	 * 修改图片
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET,value="/modifyHeadImg")
	public SingleResultDO<Boolean> modifyHeadImg(HttpServletRequest request){
	//	Long userId = (Long)request.getAttribute("userId");
		try {
			File a = new File("/Users/jack/Desktop/324342432");
			FileOutputStream os =new FileOutputStream(a);
			int i = request.getInputStream().available();
			System.out.println("**************"+i);
            byte data[] = new byte[i];
			os.write(data);
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
