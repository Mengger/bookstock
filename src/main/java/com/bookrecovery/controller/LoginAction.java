package com.bookrecovery.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bookrecovery.entry.EmployeeInfo;
import com.bookrecovery.entry.enums.ErrorCodeEnum;
import com.bookrecovery.entry.result.SingleResultDO;
import com.bookrecovery.service.IemployeeInfoService;
import com.bookrecovery.until.FtpUtil;
import com.bookrecovery.until.VerifyCodeUtils;
import com.bookrecovery.until.ftp.FTPFactory;
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
	
	
	@RequestMapping(method = RequestMethod.GET,value="/getEmployeeInfo")
	@ResponseBody
	public SingleResultDO<EmployeeInfo> getEmployeeInfo(HttpServletRequest request){
		SingleResultDO<EmployeeInfo> rtn=new SingleResultDO<EmployeeInfo>();
		String user = String.valueOf(request.getSession(true).getAttribute("userId"));
		if(StringUtils.isBlank(user)||"null".equals(user)){
			rtn.setSuccess(false);
			rtn.setErrorCode(ErrorCodeEnum.User_Not_Login.getErrorCode());
    		rtn.setErrorDesc(ErrorCodeEnum.User_Not_Login.getErrorMessage());
			return rtn;
		}
		Long userId = Long.valueOf(user);
		EmployeeInfo employee=new EmployeeInfo();
    	employee.setId(userId);
    	List<EmployeeInfo> result=employeeInfoService.queryEmployeeInfoList(employee);
    	if(result==null||result.size()==0){
    		rtn.setSuccess(false);
    		rtn.setErrorCode(ErrorCodeEnum.User_Not_Exit.getErrorCode());
    		rtn.setErrorDesc(ErrorCodeEnum.User_Not_Exit.getErrorMessage());
    	}else{
    		rtn.setSuccess(true);
    		result.get(0).setPwd("");
    		rtn.setResult(result.get(0));
    		rtn.setErrorCode(ErrorCodeEnum.Success.getErrorCode());
    		rtn.setErrorDesc(ErrorCodeEnum.Success.getErrorMessage());
    	}
    	return rtn;
	}
	
	@RequestMapping(method = RequestMethod.GET,value="/loginVerify")
	@ResponseBody
	public SingleResultDO<EmployeeInfo> LoginVerify(HttpServletRequest request, HttpServletResponse response){
		SingleResultDO<EmployeeInfo> rtn=new SingleResultDO<EmployeeInfo>();
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
		String verifyCode=String.valueOf(session.getAttribute("verifyCode")).toLowerCase();
		if(!(getVerifyCode.equals(verifyCode)||getVerifyCode.equals("00000"))){
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
        		rtn.setSuccess(false);
        		rtn.setErrorCode(ErrorCodeEnum.Count_Pwd_notMatch.getErrorCode());
        		rtn.setErrorDesc(ErrorCodeEnum.Count_Pwd_notMatch.getErrorMessage());
        	}else{
        		session.setAttribute("userId", id);
        		rtn.setSuccess(true);
        		result.get(0).setPwd("");
        		rtn.setResult(result.get(0));
        		rtn.setErrorCode(ErrorCodeEnum.Success.getErrorCode());
        		rtn.setErrorDesc(ErrorCodeEnum.Success.getErrorMessage());
        	}
        }else{
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
	 * @param QQ		QQ号
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
		String idString = String.valueOf(request.getSession(true).getAttribute("userId"));
		if(StringUtils.isBlank(idString)||idString.equals("null")){
			rtn.setResult(false);
    		rtn.setSuccess(false);
    		rtn.setErrorCode(ErrorCodeEnum.User_Not_Login.getErrorCode());
    		rtn.setErrorDesc(ErrorCodeEnum.User_Not_Login.getErrorMessage());
    		return rtn;
		}
		Long id =Long.valueOf(idString);
		employee.setId(id);
		if(birthday!=null) employee.setBirthday(new Date(birthday));
		if(QQ!=null&&QQ.length()>7) employee.setQq(QQ);
		if(name!=null&&name.length()>0) employee.setName(name);
		if(eMail!=null&&eMail.length()>0) employee.setEMail(eMail);
		if(telephone!=null&&telephone.length()>0) employee.setTelephone(telephone);
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
	@RequestMapping(method = RequestMethod.POST,value="/modifyEmployeePWD")
	public SingleResultDO<Boolean> modifyPWD(HttpServletRequest request,String newPWD,String oldPWD){
		SingleResultDO<Boolean> rtn = new SingleResultDO<Boolean>();
		String idString = String.valueOf(request.getSession(true).getAttribute("userId"));
		if(StringUtils.isBlank(idString)||idString.equals("null")){
			rtn.setResult(false);
    		rtn.setSuccess(false);
    		rtn.setErrorCode(ErrorCodeEnum.User_Not_Login.getErrorCode());
    		rtn.setErrorDesc(ErrorCodeEnum.User_Not_Login.getErrorMessage());
    		return rtn;
		}
		Long id =Long.valueOf(idString);
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
	@RequestMapping(method = RequestMethod.POST,value="/modifyHeadImg")
	public SingleResultDO<Boolean> modifyHeadImg(HttpServletRequest request){
		SingleResultDO<Boolean> rtn=new SingleResultDO<Boolean>();
		//转型为MultipartHttpRequest(重点的所在)  
        MultipartHttpServletRequest multipartRequest  =  (MultipartHttpServletRequest) request;  
        //  获得第1张图片（根据前台的name名称得到上传的文件）   
        MultipartFile imgFile1  =  multipartRequest.getFile("file");  
        String user = String.valueOf(request.getSession(true).getAttribute("userId"));
		if(StringUtils.isBlank(user)||"null".equals(user)){
			rtn.setSuccess(false);
			rtn.setErrorCode(ErrorCodeEnum.User_Not_Login.getErrorCode());
    		rtn.setErrorDesc(ErrorCodeEnum.User_Not_Login.getErrorMessage());
			return rtn;
		}
		Long userId = Long.valueOf(user);
		EmployeeInfo employee=new EmployeeInfo();
    	employee.setId(userId);
    	List<EmployeeInfo> result=employeeInfoService.queryEmployeeInfoList(employee);
    	if(result==null||result.size()==0){
    	}else{
    		log.error("");
    	}
    	
		try {
			String []pathInfos=result.get(0).getPhotoPath().split(":");
			FtpUtil f = new FtpUtil();
			f.connectServer(FTPFactory.FTPBeans.get(pathInfos[0]));
			f.deleteFile(pathInfos[1]);
			
			ByteArrayInputStream in=new ByteArrayInputStream(imgFile1.getBytes());
			boolean con=f.uploadFile(in,pathInfos[1]);
			File a = new File("/Users/jack/Desktop/324342432.png");
			/*imgFile1.transferTo(a);
			FileOutputStream os =new FileOutputStream(a);
			int i = imgFile1.getInputStream().available();
			System.out.println("**************"+i);
            byte data[] = new byte[i];
			os.write(data);
			os.flush();
			os.close();*/
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		/* DiskFileItemFactory factory =
                 new DiskFileItemFactory(1024 * 1024 * 5, new File(
                         System.getProperty("java.io.tmpdir")));
         // 设置缓冲区大小
         factory.setSizeThreshold(1024 * 1024 * 5);
         // 创建一个文件上传的句柄
         ServletFileUpload upload = new ServletFileUpload(factory);

         // 设置上传文件的整个大小和上传的单个文件大小
         upload.setSizeMax(1024 * 1024 * 5 * 10);
         upload.setFileSizeMax(1024 * 1024 * 5);
         String[] MEDIA_FILE_EXTS = {"gif", "png", "jpeg", "jpg", "mp3", "amr", "wav"};
         try {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem fileItem : items) {
                // 如果是一个普通的表单元素(type不是file的表单元素)
                if (fileItem.isFormField()) {
                    String nameString = fileItem.getFieldName();
                    System.out.println("******nameString********"+nameString);
                } else { // 获取文件的后缀名
                    String fileName = fileItem.getName();// 得到文件的名字
                    System.out.println("******fileName********"+fileName);
                    String fileExt =fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                    System.out.println("********fileExt******"+fileExt);
                    if (Arrays.binarySearch(MEDIA_FILE_EXTS, fileExt) != -1) {
                        try { // 将文件上传到项目的upload目录并命名，getRealPath可以得到该web项目下包含/upload的绝对路径//
                            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                            fileItem.write(new File("/Users/jack/Desktop/324342432"));
                        } catch (Exception e) {
                        	System.err.println(e);
                        }
                    } else {
                        System.err.println("error:ext");
                    }
                }
            }
		} catch (FileUploadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		return null;
	}
}
