package com.chillax.entry;

import java.util.Date;

/**
 * 图书属性表
 * @author jack
 *
 */
public class BookInfo {

	/**
	 * 创建时间
	 */
	private Date infoCreateTime;
	
	/**
	 * 修改时间
	 */
	private Date infoModifyTime;
	
	/**
	 * 图书编号
	 */
	private String bookId;
	
	/**
	 * 编号协议(ISBN、ISSN、ISRC)
	 */
	private String bookProtocl;
	
	/**
	 * 图书名称
	 */
	private String bookName;
	
	/**
	 * 作者
	 */
	private String author;
	
	/**
	 * 出版社名称
	 */
	private String bookConcerm;
	
	/**
	 * 图书状态(1.正常  0.停收)
	 */
	private Integer status;
	
	/**
	 * 图书类型id
	 */
	private String bookTypeId;
	
	/**
	 * 定价(单位:分)
	 */
	private Integer orderPrices;

	/**
	 * 二手书价(单位:分)
	 */
	private Integer usedPrices;
	
	/**
	 * 照片地址
	 */
	private String photoPath;
	
	/**
	 * 页数
	 */
	private String pageNum;

	public Date getInfoCreateTime() {
		return infoCreateTime;
	}

	public void setInfoCreateTime(Date infoCreateTime) {
		this.infoCreateTime = infoCreateTime;
	}

	public Date getInfoModifyTime() {
		return infoModifyTime;
	}

	public void setInfoModifyTime(Date infoModifyTime) {
		this.infoModifyTime = infoModifyTime;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getBookProtocl() {
		return bookProtocl;
	}

	public void setBookProtocl(String bookProtocl) {
		this.bookProtocl = bookProtocl;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBookConcerm() {
		return bookConcerm;
	}

	public void setBookConcerm(String bookConcerm) {
		this.bookConcerm = bookConcerm;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBookTypeId() {
		return bookTypeId;
	}

	public void setBookTypeId(String bookTypeId) {
		this.bookTypeId = bookTypeId;
	}

	public Integer getOrderPrices() {
		return orderPrices;
	}

	public void setOrderPrices(Integer orderPrices) {
		this.orderPrices = orderPrices;
	}

	public Integer getUsedPrices() {
		return usedPrices;
	}

	public void setUsedPrices(Integer usedPrices) {
		this.usedPrices = usedPrices;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public BookInfo(Date infoCreateTime, Date infoModifyTime, String bookId, String bookProtocl, String bookName,
			String author, String bookConcerm, Integer status, String bookTypeId, Integer orderPrices,
			Integer usedPrices, String photoPath, String pageNum) {
		super();
		this.infoCreateTime = infoCreateTime;
		this.infoModifyTime = infoModifyTime;
		this.bookId = bookId;
		this.bookProtocl = bookProtocl;
		this.bookName = bookName;
		this.author = author;
		this.bookConcerm = bookConcerm;
		this.status = status;
		this.bookTypeId = bookTypeId;
		this.orderPrices = orderPrices;
		this.usedPrices = usedPrices;
		this.photoPath = photoPath;
		this.pageNum = pageNum;
	}

	public BookInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

