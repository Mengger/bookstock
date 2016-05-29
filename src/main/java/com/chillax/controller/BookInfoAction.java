package com.chillax.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chillax.entry.BookInfo;
import com.chillax.entry.menu.ErrorCodeEnum;
import com.chillax.entry.result.SingleResultDO;
import com.chillax.service.IbookInfoService;

@Controller
public class BookInfoAction {

	private static final Logger log = LoggerFactory.getLogger(BookInfoAction.class);

	@Autowired
	private IbookInfoService bookInfoService;
	
	@RequestMapping(method = RequestMethod.GET , value="/bookInfo.json")
	@ResponseBody
	public SingleResultDO<BookInfo> queryBookInfoById(String bookId){
		SingleResultDO<BookInfo> rtn=new SingleResultDO<BookInfo>();
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
}
