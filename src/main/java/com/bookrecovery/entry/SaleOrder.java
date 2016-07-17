package com.bookrecovery.entry;

import java.util.Date;

public class SaleOrder {

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date modifyTime;

	/**
	 * 订单号
	 */
	private String orderId;

	/**
	 * 书编号
	 */
	private String bookId;

	/**
	 * 价格（分)
	 */
	private Integer prices;

	/**
	 * 数量
	 */
	private String orderNum ;

	/**
	 * 买家id
	 */
	private String buyerId;

	/**
	 * 订单状态(0.待交货，-1.点单作废 1.订单完成)
	 */
	private Integer status;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public Integer getPrices() {
		return prices;
	}

	public void setPrices(Integer prices) {
		this.prices = prices;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public SaleOrder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SaleOrder(Date createTime, Date modifyTime, String orderId, String bookId, Integer prices, String orderNum,
			String buyerId, Integer status) {
		super();
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.orderId = orderId;
		this.bookId = bookId;
		this.prices = prices;
		this.orderNum = orderNum;
		this.buyerId = buyerId;
		this.status = status;
	}
	
	
}
