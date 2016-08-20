package com.bookrecovery.entry;

public class SaleOrder {

	/**
	*创建时间
	*/
	private java.util.Date createTime;

	/**
	*修改时间
	*/
	private java.util.Date modifyTime;

	/**
	*订单号
	*/
	private String orderId;

	/**
	*书编号
	*/
	private String bookId;

	/**
	*价格（分)
	*/
	private Integer prices;

	/**
	*数量
	*/
	private String orderNum;

	/**
	*买家id
	*/
	private String buyerId;

	/**
	*订单状态(0.待交货，-1.点单作废 1.订单完成)
	*/
	private Integer status;


	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}

	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	public void setModifyTime(java.util.Date modifyTime){
		this.modifyTime = modifyTime;
	}

	public java.util.Date getModifyTime(){
		return this.modifyTime;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return this.orderId;
	}

	public void setBookId(String bookId){
		this.bookId = bookId;
	}

	public String getBookId(){
		return this.bookId;
	}

	public void setPrices(Integer prices){
		this.prices = prices;
	}

	public Integer getPrices(){
		return this.prices;
	}

	public void setOrderNum(String orderNum){
		this.orderNum = orderNum;
	}

	public String getOrderNum(){
		return this.orderNum;
	}

	public void setBuyerId(String buyerId){
		this.buyerId = buyerId;
	}

	public String getBuyerId(){
		return this.buyerId;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

}