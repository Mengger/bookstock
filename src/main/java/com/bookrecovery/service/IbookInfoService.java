package com.bookrecovery.service;

import com.bookrecovery.entry.BookInfo;
import com.bookrecovery.entry.Vo.BookInfoVo;

public interface IbookInfoService {

	/**
	 * 根据bookid查询数据库中的bookinfo
	 * @return
	 */
	public BookInfo queryDBbyBookId(String id);
	
	/**
	 * 在internet中查询书本详情
	 * @return
	 */
	public BookInfo queryInternetByBookId(String id);
	
	/**
	 * 先查数据库，如果没有，再爬虫 二手书网站
	 * @param id
	 * @return
	 */
	public BookInfoVo queryDBThenInterNet(String id);
	
	/**
	 * 将图书信息保存入库，将图片放入ftp中
	 * @param book
	 * @return
	 */
	public boolean saveBookInfo(BookInfo book);
}
