package com.chillax.controller;

import java.io.File;
import java.io.FileInputStream;

import javax.annotation.Resource;

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
import com.chillax.until.FtpUtil;
import com.chillax.until.redis.load.RedisManager;

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
	public SingleResultDO<BookInfo> saveBookInfo(String bookId,String bookProtocl,String bookName,
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
		bookInfoService.saveBookInfo(book);
		return null;
		
	}
	
	/*** 
     * 上传文件 用@RequestParam注解来指定表单上的file为MultipartFile 
     *  
     * @param file 
     * @return 
     */  
    @RequestMapping("fileUpload")  
    @ResponseBody
    public SingleResultDO<Boolean> fileUpload(@RequestParam("file") MultipartFile file,String bookId) {  
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
            		RedisManager.setValueByKeyAndGroup("GROUP_0", "floder_name", "");
            	}else{
            		
            	}
            	FtpUtil f = new FtpUtil();
        		boolean con=f.upload("audio"+bookId,file.getInputStream());
        		System.out.println(con);
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
