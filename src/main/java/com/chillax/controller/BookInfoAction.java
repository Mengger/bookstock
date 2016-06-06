package com.chillax.controller;

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
    public String fileUpload(@RequestParam("file") MultipartFile file) {  
        // 判断文件是否为空  
    	System.out.println("***************************************************************");
        if (!file.isEmpty()) {  
            try {  
            	System.out.println("***************************************************************");
            	System.out.println("***************************************************************");
            	System.out.println("***************************************************************");
                // 转存文件  
                file.getInputStream();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        // 重定向  
        return "true";  
    }  
}
