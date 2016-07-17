package com.bookrecovery.entry.Vo;

import java.util.Date;

import com.bookrecovery.entry.BookInfo;

public class BookInfoVo extends BookInfo {

	/**
	 * 书本来源
	 * 0.来自DB
	 * 1.来自Internet
	 */
	private Integer bookFrom;

	/**
	 * 查询结果是否成功
	 * 0.失败
	 * 1.成功
	 */
	private boolean successByInternet;
	
	
	public boolean isSuccessByInternet() {
		return successByInternet;
	}

	public void setSuccessByInternet(boolean isSuccessByInternet) {
		this.successByInternet = isSuccessByInternet;
	}

	public Integer getBookFrom() {
		return bookFrom;
	}

	public void setBookFrom(Integer bookFrom) {
		this.bookFrom = bookFrom;
	}
	
	public BookInfoVo setBookInfo(BookInfo bookInfo) {
		return new BookInfoVo(bookInfo);
	}

	public BookInfoVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BookInfoVo(BookInfo bookInfo) {
		super(bookInfo);
	}

}
