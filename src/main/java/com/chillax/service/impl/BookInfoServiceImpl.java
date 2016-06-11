package com.chillax.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chillax.dao.BookInfoDao;
import com.chillax.entry.BookInfo;
import com.chillax.entry.Vo.BookInfoVo;
import com.chillax.service.IbookInfoService;
import com.chillax.until.HttpRequest;
import com.chillax.until.StringRegexUntil;

@Service(value="bookInfoService")
public class BookInfoServiceImpl implements IbookInfoService {

	private static final Logger log = LoggerFactory.getLogger(BookInfoServiceImpl.class);
	
	@Resource
	private BookInfoDao bookInfoDao;
	
	@Override
	public BookInfo queryDBbyBookId(String id) {
		// TODO Auto-generated method stub
		return bookInfoDao.queryBookInfoById(id);
	}

	@Override
	public BookInfo queryInternetByBookId(String id) {
		// TODO Auto-generated method stub
		BookInfo bookInfo=new BookInfo();
		//将id先转化成ASCII，然后在变成十六进制
		StringBuffer rtn=new StringBuffer();
    	for(int i=0;i<id.length();i++){
    		rtn.append("k").append(Integer.toHexString(id.substring(i, i+1).getBytes()[0]).toString().toLowerCase());
    	}
        //第一次向孔夫子旧书网 发送请求 获取查询结果
        String queryResult=HttpRequest.sendGet("http://search.kongfz.com/product/y0z",rtn.append("/").toString());
        //正则表达式对结果筛选 取第一个
        String first=StringRegexUntil.regexString(queryResult, "<div class=\"big_pic\">((?!</div>).)*</div>")[0];
        //第二次正则表达式 筛选出第一个结果
        String secondPath=StringRegexUntil.regexString(first, "href=\"http((?!\").)*\"")[0].replace("href=\"", "").replaceAll("\"", "");
        //第二次请求孔夫子旧书网 获取书本详情
        queryResult=HttpRequest.sendGet(secondPath,"");
        String bookAllInfo=StringRegexUntil.regexString(queryResult, "<div class=\"book_attr clearfix\">((?!(<div>((?!<div>).)*</div>)).)*</div>")[0];
        
        String bookName=null;
        String[] bookInfoas=StringRegexUntil.regexString(queryResult, "<h1 itemprop=\"name\">((?!</h1>).)*</h1>");
        if(bookInfoas.length>0)
        bookName=bookInfoas[0].replaceAll("<h1 itemprop=\"name\">", "").replaceAll("</h1>", "");
        
        bookInfo.setBookName(bookName);
        String bookInfos[]=StringRegexUntil.regexString(bookAllInfo, "<p>((?!</p>).)*</p>");
        for(String info:bookInfos){
        	info=info.replaceAll("&nbsp;", "").replaceAll("<p>", "").replaceAll("</p>", "").replaceAll(" ", "");
        	if(info.contains("作者")){
        		bookInfo.setAuthor(dealQueryResult(StringRegexUntil.regexString(info, ">.*<")));
        	}else if(info.contains("出版社")){
        		bookInfo.setBookConcerm(info.replace("出版社：", "").replace("：", ""));
        	}else if(info.contains("页数")){
        		bookInfo.setPageNum(info.replaceAll("页数", "").replace("：", ""));
        	}else if(info.contains("ISBN")||info.contains("ISRC")||info.contains("ISSN")){
        		bookInfo.setBookId(info.replaceAll("ISBN", "").replace("ISRC", "").replace("ISSN", "").replace("：", ""));
        		if(info.contains("ISBN")){
           			bookInfo.setBookProtocl("ISBN");
        		}else if(info.contains("ISRC")){
           			bookInfo.setBookProtocl("ISRC");
        		}else if(info.contains("ISSN")){
           			bookInfo.setBookProtocl("ISSN");
        		}
        	}else if(info.contains("原书售价：")){
        		int a=info.indexOf("原书售价：");
        		String noDealPrice=info.substring(a,a+11).replaceAll("元","").replace("：", "").replaceAll("s", "").replaceAll("原书售价", "");
        		int price=0;
        		if(noDealPrice.contains(".")){
        			price=Integer.valueOf(noDealPrice.replace(".", "").replace("<", "").replace("/", ""));
        		}else{
        			price=Integer.valueOf(noDealPrice.replace(".", "").replace("<", "").replace("/", ""))*100;
        		}
        		bookInfo.setOrderPrices(price);;
        	}
        }
        String images=StringRegexUntil.regexString(queryResult, "<div class=\"pic_box\" id=\"bigBookImg\">((?!</div>).)*</div>")[0];
        bookInfo.setPhotoPath(StringRegexUntil.regexString(images, "src=\"((?!\").)*\"")[0].replaceAll("src=", "").replaceAll("\"", ""));
        return bookInfo;
	}
	
	public String dealQueryResult(String[] input){
		if(null!=input){
			return input[0].replaceAll(">", "").replaceAll("<", "");
		}
		return null;
	}

	public BookInfoVo queryDBThenInterNet(String id) {
		BookInfoVo bookInfo=null;
		BookInfo bookIn=queryDBbyBookId(id);
		if(null!=bookIn){
			bookInfo=new BookInfoVo(bookIn);
			bookInfo.setBookFrom(0);
		}else{
			bookInfo=new BookInfoVo();
			try {
				bookInfo=bookInfo.setBookInfo(queryInternetByBookId(id));
				bookInfo.setSuccessByInternet(true);
			}catch (ArrayIndexOutOfBoundsException e){
				bookInfo.setSuccessByInternet(false);
				log.error("queryInternetByBookId error,_____id____"+id+" is not exit");
			}catch (Exception e) {
				bookInfo.setSuccessByInternet(false);
				log.error("queryInternetByBookId error",e);
			}
			bookInfo.setBookFrom(1);
		}
		return bookInfo;
	}

	public boolean saveBookInfo(BookInfo book) {
		int num=bookInfoDao.saveBookInfo(book);
		if(num>0){
			return true;
		}
		return false;
	}

}
