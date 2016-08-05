package com.bookrecovery.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookrecovery.Vo.BookInfoVo;
import com.bookrecovery.entry.BookInfo;
import com.bookrecovery.entry.enums.ErrorCodeEnum;
import com.bookrecovery.entry.result.SingleResultDO;
import com.bookrecovery.service.IbookInfoService;
import com.bookrecovery.until.DateUntil;
import com.bookrecovery.until.FtpUtil;
import com.bookrecovery.until.ftp.FTPFactory;
import com.bookrecovery.until.redis.load.RedisManager;

import sun.misc.BASE64Decoder;  

@Controller
@RequestMapping
public class BookInfoAction {

	private static final Logger log = LoggerFactory.getLogger(BookInfoAction.class);

	@Resource
	private IbookInfoService bookInfoService;
	
	@RequestMapping(method = RequestMethod.GET,value="/imgStream")
	public void GetImageStream(String ftpPath,HttpServletRequest request, HttpServletResponse response){
		String []pathInfos=ftpPath.split(":");
		FtpUtil f = new FtpUtil();
    	try {
			f.connectServer(FTPFactory.FTPBeans.get(pathInfos[0]));
			f.copyStreamFromFTP(pathInfos[1], response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				f.closeServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(method = RequestMethod.POST , value="/bookInfo")
	@ResponseBody
	public SingleResultDO<BookInfoVo> queryBookInfoById(String bookId,HttpServletRequest request){
		SingleResultDO<BookInfoVo> rtn=new SingleResultDO<BookInfoVo>();
		if(null==bookId){
			rtn.setSuccess(false);
			rtn.setErrorCode(ErrorCodeEnum.Error_input.getErrorCode());
			rtn.setErrorDesc(ErrorCodeEnum.Error_input.getErrorMessage());
		}
		try {
			BookInfoVo bookInfo=bookInfoService.queryDBThenInterNet(bookId);
			if(bookInfo.getBookCreateWay()!=null&&bookInfo.getBookCreateWay().equals(2)){
				bookInfo.setLocalPhotoPath("../imgStream.action?ftpPath="+bookInfo.getLocalPhotoPath());
			}
			rtn.setResult(bookInfo);
		} catch (Exception e) {
			log.error("queryBookInfoById error ____bookId:"+bookId,e);
			rtn.setSuccess(false);
			rtn.setErrorCode(ErrorCodeEnum.Sorry_info.getErrorCode());
			rtn.setErrorDesc(ErrorCodeEnum.Sorry_info.getErrorMessage());
		}
		return rtn;
	}
	
	@RequestMapping(method = RequestMethod.POST , value="/saveInfo")
	@ResponseBody
	public SingleResultDO<String> saveBookInfo(String bookId,String bookProtocl,String bookName,String loaclPath,
			String bookConcern,String bookAuthor,int orderPrices,String bookPageNum,String bookPhotoPath){
		SingleResultDO<String> rtn=new SingleResultDO<String>();
		Long bookid=null;
		try {
			bookid=Long.valueOf(bookId);
		} catch (Exception e) {
			rtn.setSuccess(false);
			rtn.setErrorCode(ErrorCodeEnum.Error_input.getErrorCode());
			rtn.setErrorDesc(ErrorCodeEnum.Error_input.getErrorMessage());
			return rtn;
		}
		BookInfo book=new BookInfo();
		book.setBookId(bookid);
		book.setBookProtocl(bookProtocl);
		book.setBookName(bookName);
		book.setBookConcerm(bookConcern);
		book.setOrderPrices(orderPrices);
		book.setAuthor(bookAuthor);
		book.setPageNum(bookPageNum);
		book.setPhotoPath(bookPhotoPath);
		String ftpPath;
		try {
			ftpPath = RedisManager.getValueByKeyAndGroup("GROUP_0", bookId.toString());
			if(!StringUtils.isBlank(ftpPath)){
				book.setLocalPhotoPath(FTPFactory.FTPDefultConfig+":"+ftpPath);
				book.setBookCreateWay(2);//客户自己上传图片
				RedisManager.removeByKeyAndGroup("GROUP_0", bookId.toString());
				RedisManager.removeByKeyAndGroup("GROUP_0", bookId+"_path");
			}
		} catch (Exception e) {
			log.error("redis is error",e);
		}
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
        	String imgType=imgInfo[0].substring(imgInfo[0].indexOf("/")+1, imgInfo[0].indexOf(";"));
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
                String oldPath=RedisManager.getValueByKeyAndGroup("GROUP_0", bookId+"_path");
                if(!StringUtils.isBlank(oldPath)){
                	filePath=oldPath;
                }
            	FtpUtil f = new FtpUtil();
            	f.connectServer(FTPFactory.FTPUploadDefult);
            	ByteArrayInputStream in=new ByteArrayInputStream(b);
        		boolean con=f.uploadFile(in,bookId+"."+imgType,filePath);
        		if (con) {
					RedisManager.setValueByKeyAndGroupDedultTime("GROUP_0", bookId, filePath+"/"+bookId+"."+imgType);
					RedisManager.setValueByKeyAndGroupDedultTime("GROUP_0", bookId+"_path", filePath);
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
}
