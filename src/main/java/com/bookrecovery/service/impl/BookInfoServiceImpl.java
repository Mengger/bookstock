package com.bookrecovery.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bookrecovery.Vo.BookInfoVo;
import com.bookrecovery.dao.BookInfoDao;
import com.bookrecovery.entry.BookInfo;
import com.bookrecovery.service.IbookInfoService;
import com.bookrecovery.until.HttpRequest;
import com.bookrecovery.until.StringRegexUntil;

@Service(value = "bookInfoService")
public class BookInfoServiceImpl implements IbookInfoService {

	private static final Logger log = LoggerFactory.getLogger(BookInfoServiceImpl.class);

	@Resource
	private BookInfoDao bookInfoDao;

	@Override
	public BookInfo queryDBbyBookId(String id) {
		BookInfo bookInfo = new BookInfo();
		bookInfo.setBookId(id);
		List<BookInfo> getList=bookInfoDao.queryBookInfoList(bookInfo);
		if(getList!=null&&getList.size()>0){
			return getList.get(0);
		}
		return null;
	}

	@Override
	public BookInfo queryInternetByBookId(BookInfo bookInfo) {
		String id = bookInfo.getBookId();
		// 将id先转化成ASCII，然后在变成十六进制
		StringBuffer rtn = new StringBuffer();
		for (int i = 0; i < id.length(); i++) {
			rtn.append("k").append(Integer.toHexString(id.substring(i, i + 1).getBytes()[0]).toString().toLowerCase());
		}
		// 第一次向孔夫子旧书网 发送请求 获取查询结果
		String queryResult = HttpRequest.sendGet("http://search.kongfz.com/product/y0z" + rtn.append("/").toString(),
				null);
		// 正则表达式对结果筛选 取第一个
		String first = StringRegexUntil.regexString(queryResult, "<div class=\"result_tit\">((?!</div>).)*</div>")[0];
		/*
		 * for(String a:StringRegexUntil.regexString(queryResult,
		 * "<div class=\"result_tit\">((?!</div>).)*</div>")){
		 * System.out.println("$$$$$$$$$$$$"+a); } String []
		 * aaaa=StringRegexUntil.regexString(first, "href=\"http((?!\").)*\"");
		 * for (String aa:aaaa) { System.out.println("##############"+aa); }
		 */
		// 第二次正则表达式 筛选出第一个结果
		String secondPath = StringRegexUntil.regexString(first, "href=\"http((?!\").)*\"")[0].replace("href=\"", "")
				.replaceAll("\"", "");
		// 第二次请求孔夫子旧书网 获取书本详情
		queryResult = HttpRequest.sendGet(secondPath, null);
		String bookAllInfo = StringRegexUntil.regexString(queryResult,
				"<div class=\"book_attr clearfix\">((?!(<div>((?!<div>).)*</div>)).)*</div>")[0];
		String bookName = null;
		String[] bookInfoas = StringRegexUntil.regexString(queryResult, "<h1 itemprop=\"name\">((?!</h1>).)*</h1>");
		if (bookInfoas.length > 0)
			bookName = bookInfoas[0].replaceAll("<h1 itemprop=\"name\">", "").replaceAll("</h1>", "");
		bookInfo.setBookName(bookName);

		String bookInfos[] = StringRegexUntil.regexString(bookAllInfo, "<p>((?!</p>).)*</p>");
		for (String info : bookInfos) {
			try {
				info = info.replaceAll("&nbsp;", "").replaceAll("<p>", "").replaceAll("</p>", "").replaceAll(" ", "");
				if (info.contains("作者")) {
					if (StringUtils.isBlank(bookInfo.getAuthor()))
						bookInfo.setAuthor(info.replace("作者：", "").replace("：", ""));
				} else if (info.contains("出版社")) {
					if (StringUtils.isBlank(bookInfo.getBookConcerm())) {
						bookInfo.setBookConcerm(info.replace("出版社：", "").replace("：", ""));
					}
				} else if (info.contains("页数")) {
					if (StringUtils.isBlank(bookInfo.getPageNum()))
						bookInfo.setPageNum(info.replaceAll("页数", "").replace("：", ""));
				} else if (info.contains("ISBN") || info.contains("ISRC") || info.contains("ISSN")) {
					bookInfo.setBookId(info.replaceAll("ISBN", "").replace("ISRC", "").replace("ISSN", "").replace("：", ""));
					if (info.contains("ISBN")) {
						bookInfo.setBookProtocl("ISBN");
					} else if (info.contains("ISRC")) {
						bookInfo.setBookProtocl("ISRC");
					} else if (info.contains("ISSN")) {
						bookInfo.setBookProtocl("ISSN");
					}
				} else if (info.contains("原书售价：")) {
					int a = info.indexOf("原书售价：");
					String noDealPrice = info.substring(a, a + 11).replaceAll("元", "").replace("：", "")
							.replaceAll("s", "").replaceAll("原书售价", "");
					int price = 0;
					if (noDealPrice.contains(".")) {
						price = Integer.valueOf(noDealPrice.replace(".", "").replace("<", "").replace("/", ""));
					} else {
						price = Integer.valueOf(noDealPrice.replace(".", "").replace("<", "").replace("/", "")) * 100;
					}
					bookInfo.setOrderPrices(price);
					;
				}
			} catch (Exception e) {
				log.error("queryBook is error check regex ,bookid = " + id, e);
			}
		}
		String images = StringRegexUntil.regexString(queryResult,
				"<div class=\"pic_box\" id=\"bigBookImg\">((?!</div>).)*</div>")[0];
		bookInfo.setPhotoPath(StringRegexUntil.regexString(images, "src=\"((?!\").)*\"")[0].replaceAll("src=", "")
				.replaceAll("\"", ""));
		return bookInfo;
	}

	public String dealQueryResult(String[] input) {
		if (null != input) {
			return input[0].replaceAll(">", "").replaceAll("<", "");
		}
		return null;
	}

	public BookInfoVo queryDBThenInterNet(String id) {
		BookInfoVo bookInfo = null;
		BookInfo bookIn = queryDBbyBookId(id);
		if (null != bookIn) {
			bookInfo = new BookInfoVo(bookIn);
			bookInfo.setBookFrom(0);
		} else {
			bookInfo = new BookInfoVo();
			bookInfo.setBookId(id);
			try {
				bookInfo = bookInfo.setBookInfo(queryInternetByBookId(bookInfo));
				bookInfo.setSuccessByInternet(true);
			} catch (ArrayIndexOutOfBoundsException e) {
				bookInfo.setSuccessByInternet(true);
				log.error("queryBook is error check regex ,bookid = " + id, e);
			} catch (Exception e) {
				bookInfo.setSuccessByInternet(false);
				log.error("queryInternetByBookId error", e);
			}
			bookInfo.setBookFrom(1);
			if (StringUtils.isBlank(bookInfo.getAuthor()) && StringUtils.isBlank(bookInfo.getBookConcerm())
					&& StringUtils.isBlank(bookInfo.getBookName()) && StringUtils.isBlank(bookInfo.getBookProtocl())
					&& StringUtils.isBlank(bookInfo.getBookTypeId()) && StringUtils.isBlank(bookInfo.getPageNum())
					&& StringUtils.isBlank(bookInfo.getPhotoPath()) && bookInfo.getOrderPrices()==null) {
				bookInfo.setSuccessByInternet(false);
			}
		}
		return bookInfo;
	}

	public boolean saveBookInfo(BookInfo book) {
		book.setInfoCreateTime(new Date());
		int num = bookInfoDao.saveBookInfo(book);
		if (num > 0) {
			return true;
		}
		return false;
	}

}
