package com.chillax.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chillax.entry.BookInfo;
import com.chillax.entry.Vo.BookInfoVo;
import com.chillax.entry.enums.ErrorCodeEnum;
import com.chillax.entry.result.SingleResultDO;
import com.chillax.service.IbookInfoService;
import com.chillax.until.DateUntil;
import com.chillax.until.FtpUtil;
import com.chillax.until.ftp.FTPFactory;
import com.chillax.until.redis.load.RedisManager;

import sun.misc.BASE64Decoder;  
import sun.misc.BASE64Encoder;  

@Controller
@RequestMapping
public class BookInfoAction {

	private static final Logger log = LoggerFactory.getLogger(BookInfoAction.class);

	@Resource
	private IbookInfoService bookInfoService;
	
	@RequestMapping(method = RequestMethod.GET , value="/bookInfo")
	@ResponseBody
	public SingleResultDO<BookInfoVo> queryBookInfoById(String bookId){
		SingleResultDO<BookInfoVo> rtn=new SingleResultDO<BookInfoVo>();
		if(null==bookId){
			rtn.setSuccess(false);
			rtn.setErrorCode(ErrorCodeEnum.Error_input.getErrorCode());
			rtn.setErrorDesc(ErrorCodeEnum.Error_input.getErrorMessage());
		}
		
		try {
			rtn.setResult(bookInfoService.queryDBThenInterNet(bookId));
		} catch (Exception e) {
			log.error("queryBookInfoById error ____bookId:"+bookId,e);
			rtn.setSuccess(false);
			rtn.setErrorCode(ErrorCodeEnum.Sorry_info.getErrorCode());
			rtn.setErrorDesc(ErrorCodeEnum.Sorry_info.getErrorMessage());
		}
		return rtn;
	}
	
	@RequestMapping(method = RequestMethod.GET , value="/saveInfo")
	@ResponseBody
	public SingleResultDO<String> saveBookInfo(String bookId,String bookProtocl,String bookName,String loaclPath,
			String bookConcern,String bookAuthor,int orderPrices,String bookPageNum,String bookPhotoPath){
		BookInfo book=new BookInfo();
		book.setBookId(bookId);
		book.setBookProtocl(bookProtocl);
		book.setBookName(bookName);
		book.setBookConcerm(bookConcern);
		book.setOrderPrices(orderPrices);
		book.setAuthor(bookAuthor);
		book.setPageNum(bookPageNum);
		book.setPhotoPath(bookPhotoPath);
		String ftpPath;
		try {
			ftpPath = RedisManager.getValueByKeyAndGroup("GROUP_0", bookId);
			if(!StringUtils.isBlank(ftpPath)){
				book.setLocalPhotoPath(ftpPath);
				RedisManager.removeByKeyAndGroup("GROUP_0", bookId);
				RedisManager.removeByKeyAndGroup("GROUP_0", bookId+"_path");
			}
		} catch (Exception e) {
			log.error("redis is error",e);
		}
		SingleResultDO<String> rtn=new SingleResultDO<String>();
		try {
			bookInfoService.saveBookInfo(book);
			rtn.setSuccess(true);
		} catch (Exception e) {
			log.error("save book error",e);
			rtn.setSuccess(false);
			rtn.setErrorCode(ErrorCodeEnum.Book_exit.getErrorCode());
			rtn.setErrorDesc(ErrorCodeEnum.Book_exit.getErrorMessage());
		}
		return rtn;
	}
	
	@RequestMapping(method = RequestMethod.POST , value="/fileUploadBase64")
	@ResponseBody
	public SingleResultDO<String> fileUploadBase64(String base64,String bookId) {  
    	SingleResultDO<String> rtn=new SingleResultDO<String>();
        // 判断文件是否为空  
        if (!StringUtils.isBlank(base64)) {
        	String imgInfo[]=base64.split(",");
        	base64=imgInfo[1];
        	String imgType=imgInfo[0].substring(imgInfo[0].indexOf("/"), imgInfo[0].indexOf(";"));
            try {
                // 转存文件  
            	String filePath=null;
            	if(RedisManager.getCountByKeyAndInc("GROUP_0", "floder_content")>100){
            		//该文件夹的图片数量大于100   清零，重新创建新的文件夹
            		RedisManager.setValueByKeyAndGroup("GROUP_0", "floder_content", "1");
            		RedisManager.setValueByKeyAndGroup("GROUP_0", "floder_name", DateUntil.getNowTime("yyyyMMddHHmmss"));
            	}
            	filePath=RedisManager.getValueByKeyAndGroup("GROUP_0", "floder_name");
            	if(null==filePath){
            		filePath=DateUntil.getNowTime("yyyyMMddHHmmss");
            		RedisManager.setValueByKeyAndGroup("GROUP_0", "floder_name", filePath);
            	}
            	BASE64Decoder decoder = new BASE64Decoder();  
                 //Base64解码  
                byte[] b = decoder.decodeBuffer(base64);  
                for(int i=0;i<b.length;++i){  
                	if(b[i]<0){//调整异常数据  
                		b[i]+=256;  
                	}  
                }  
                /*
                OutputStream out = new FileOutputStream(new File("/Users/jack/Desktop/"+filePath+"."+imgType));      
                out.write(b);  
                out.flush();  
                out.close();*/  
                String oldPath=RedisManager.getValueByKeyAndGroup("GROUP_0", bookId+"_path");
                if(!StringUtils.isBlank(oldPath)){
                	filePath=oldPath;
                }
            	FtpUtil f = new FtpUtil();
            	f.connectServer(FTPFactory.FTPBean);
            	ByteArrayInputStream in=new ByteArrayInputStream(b);
        		boolean con=f.uploadFile(in,bookId+"."+imgType,filePath);
        		if (con) {
					RedisManager.setValueByKeyAndGroup("GROUP_0", bookId, filePath+"/"+bookId+"."+imgType);
					RedisManager.setValueByKeyAndGroup("GROUP_0", bookId+"_path", filePath);
					rtn.setSuccess(true);
				}
        		in.close();
        		f.closeServer();
            } catch (Exception e) {  
                log.error("file upload error",e);
                rtn.setSuccess(false);
    			rtn.setErrorCode(ErrorCodeEnum.Sorry_info.getErrorCode());
    			rtn.setErrorDesc(ErrorCodeEnum.Sorry_info.getErrorMessage());
            }  
        }else{
        	rtn.setSuccess(false);
        	rtn.setErrorCode(ErrorCodeEnum.Error_input_stream.getErrorCode());
        	rtn.setErrorDesc(ErrorCodeEnum.Error_input_stream.getErrorMessage());
        }
        return rtn;  
    }  
	
	/*** 
     * 上传文件 用@RequestParam注解来指定表单上的file为MultipartFile 
     *  
     * @param file 
     * @return 
     */  
    @RequestMapping("fileUpload")  
    @ResponseBody
    public SingleResultDO<Boolean> fileUpload(@RequestParam("file") MultipartFile file) {  
    	SingleResultDO<Boolean> rtn=new SingleResultDO<Boolean>();
    	rtn.setSuccess(true);
    	rtn.setResult(true);
        // 判断文件是否为空  
        if (!file.isEmpty()) {  
            try {
                // 转存文件  
            	String filePath=null;
            	if(RedisManager.getCountByKeyAndInc("GROUP_0", "floder_content")>100){
            		//该文件夹的图片数量大于100   清零，重新创建新的文件夹
            		RedisManager.setValueByKeyAndGroup("GROUP_0", "floder_content", "1");
            		RedisManager.setValueByKeyAndGroup("GROUP_0", "floder_name", DateUntil.getNowTime("yyyyMMddHHmmss"));
            	}
            	filePath=RedisManager.getValueByKeyAndGroup("GROUP_0", "floder_name");
            	if(null==filePath){
            		filePath=DateUntil.getNowTime("yyyyMMddHHmmss");
            		RedisManager.setValueByKeyAndGroup("GROUP_0", "floder_name", filePath);
            	}
            	InputStream in=file.getInputStream();
            	FtpUtil f = new FtpUtil();
            	f.connectServer(FTPFactory.FTPBean);
        		boolean con=f.uploadFile(in,filePath+".png","/thi/");
        		in.close();
        		f.closeServer();
            } catch (Exception e) {  
                log.error("file upload error",e);
                rtn.setSuccess(false);
                rtn.setResult(false);
    			rtn.setErrorCode(ErrorCodeEnum.Sorry_info.getErrorCode());
    			rtn.setErrorDesc(ErrorCodeEnum.Sorry_info.getErrorMessage());
            }  
        }else{
        	rtn.setSuccess(false);
        	rtn.setResult(false);
        	rtn.setErrorCode(ErrorCodeEnum.Error_input_stream.getErrorCode());
        	rtn.setErrorDesc(ErrorCodeEnum.Error_input_stream.getErrorMessage());
        }
        return rtn;  
    }  
}
