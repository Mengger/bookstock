package com.bookrecovery.entry;

public class BookInfo {

	/**
	*创建时间
	*/
	private java.util.Date infoCreateTime;

	/**
	*修改时间
	*/
	private java.util.Date infoModifyTime;

	/**
	*图书编号
	*/
	private String bookId;

	/**
	*编号协议(1.ISBN、2.ISSN、3.ISRC)
	*/
	private String bookProtocl;

	/**
	*图书名称
	*/
	private String bookName;

	/**
	*作者
	*/
	private String author;

	/**
	*出版社名称
	*/
	private String bookConcerm;

	/**
	*图书状态(1.正常  0.停收 -1.待审核)
	*/
	private Integer status;

	/**
	*图书类型id
	*/
	private String bookTypeId;

	/**
	*定价(单位:分)
	*/
	private Integer orderPrices;

	/**
	*二手书价(单位:分)
	*/
	private Integer usedPrices;

	/**
	*照片地址
	*/
	private String photoPath;

	/**
	*本地FTP照片地址   FTP:/PATH/IMG(FTP代号:相对路径)
	*/
	private String localPhotoPath;

	/**
	*书本图片取值位置 0代表取别人的图片,1代表取自己FTP的
	*/
	private Integer photoUse;

	/**
	*页数
	*/
	private String pageNum;

	/**
	*雇员id
	*/
	private String createrId;

	/**
	*图书审核状态(1.已审核 0.待审核)
	*/
	private Integer infoStatus;

	/**
	*创建方式(1.internet 2.employee)
	*/
	private Integer bookCreateWay;


	public void setInfoCreateTime(java.util.Date infoCreateTime){
		this.infoCreateTime = infoCreateTime;
	}

	public java.util.Date getInfoCreateTime(){
		return this.infoCreateTime;
	}

	public void setInfoModifyTime(java.util.Date infoModifyTime){
		this.infoModifyTime = infoModifyTime;
	}

	public java.util.Date getInfoModifyTime(){
		return this.infoModifyTime;
	}

	public void setBookId(String bookId){
		this.bookId = bookId;
	}

	public String getBookId(){
		return this.bookId;
	}

	public void setBookProtocl(String bookProtocl){
		this.bookProtocl = bookProtocl;
	}

	public String getBookProtocl(){
		return this.bookProtocl;
	}

	public void setBookName(String bookName){
		this.bookName = bookName;
	}

	public String getBookName(){
		return this.bookName;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return this.author;
	}

	public void setBookConcerm(String bookConcerm){
		this.bookConcerm = bookConcerm;
	}

	public String getBookConcerm(){
		return this.bookConcerm;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setBookTypeId(String bookTypeId){
		this.bookTypeId = bookTypeId;
	}

	public String getBookTypeId(){
		return this.bookTypeId;
	}

	public void setOrderPrices(Integer orderPrices){
		this.orderPrices = orderPrices;
	}

	public Integer getOrderPrices(){
		return this.orderPrices;
	}

	public void setUsedPrices(Integer usedPrices){
		this.usedPrices = usedPrices;
	}

	public Integer getUsedPrices(){
		return this.usedPrices;
	}

	public void setPhotoPath(String photoPath){
		this.photoPath = photoPath;
	}

	public String getPhotoPath(){
		return this.photoPath;
	}

	public void setLocalPhotoPath(String localPhotoPath){
		this.localPhotoPath = localPhotoPath;
	}

	public String getLocalPhotoPath(){
		return this.localPhotoPath;
	}

	public void setPhotoUse(Integer photoUse){
		this.photoUse = photoUse;
	}

	public Integer getPhotoUse(){
		return this.photoUse;
	}

	public void setPageNum(String pageNum){
		this.pageNum = pageNum;
	}

	public String getPageNum(){
		return this.pageNum;
	}

	public void setCreaterId(String createrId){
		this.createrId = createrId;
	}

	public String getCreaterId(){
		return this.createrId;
	}

	public void setInfoStatus(Integer infoStatus){
		this.infoStatus = infoStatus;
	}

	public Integer getInfoStatus(){
		return this.infoStatus;
	}

	public void setBookCreateWay(Integer bookCreateWay){
		this.bookCreateWay = bookCreateWay;
	}

	public Integer getBookCreateWay(){
		return this.bookCreateWay;
	}
	
	public BookInfo() {
		super();
	}

	public BookInfo(BookInfo bookInfo) {
		this.infoCreateTime = bookInfo.getInfoCreateTime();
		this.infoModifyTime = bookInfo.getInfoModifyTime();
		this.bookId = bookInfo.getBookId();
		this.bookProtocl = bookInfo.getBookProtocl();
		this.bookName = bookInfo.getBookName();
		this.author = bookInfo.getAuthor();
		this.bookConcerm = bookInfo.getBookConcerm();
		this.status = bookInfo.getStatus();
		this.bookTypeId = bookInfo.getBookTypeId();
		this.orderPrices = bookInfo.getOrderPrices();
		this.usedPrices = bookInfo.getUsedPrices();
		this.photoPath = bookInfo.getPhotoPath();
		this.localPhotoPath = bookInfo.getLocalPhotoPath();
		this.photoUse = bookInfo.getPhotoUse();
		this.pageNum = bookInfo.getPageNum();
		this.createrId = bookInfo.getCreaterId();
		this.infoStatus = bookInfo.getInfoStatus();
		this.bookCreateWay = bookInfo.getBookCreateWay();
	}

}