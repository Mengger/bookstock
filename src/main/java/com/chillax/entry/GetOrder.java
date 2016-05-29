package com.chillax.entry;

import java.util.Date;

/**
 * 收书订单详情表
 * @author jack
 *
 */
public class GetOrder {

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 修改时间
	 */
	private Date modifyTime; 
	
	/**
	 * 父订单id
	 */
	private String parentId ;
	
	/**
	 * 子订单id
	 */
	private String orderId ;
	
	/**
	 * 图书编号
	 */
	private String bookId ;
	
	/**
	 * 回收价格
	 */
	private Integer bookPrices ;
	
	/**
	 * 回收小费
	 */
	private Integer tip;
	
	/**
	 * 雇员id
	 */
	private String employeeId ;
	
	/**
	 * 图书数量
	 */
	private Integer bookCount;
	
	/**
	 * 订单状态(-1.订单作废 0.读取完毕 1.雇员回收完成)
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public Integer getBookPrices() {
		return bookPrices;
	}

	public void setBookPrices(Integer bookPrices) {
		this.bookPrices = bookPrices;
	}

	public Integer getTip() {
		return tip;
	}

	public void setTip(Integer tip) {
		this.tip = tip;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getBookCount() {
		return bookCount;
	}

	public void setBookCount(Integer bookCount) {
		this.bookCount = bookCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public GetOrder(Date createTime, Date modifyTime, String parentId, String orderId, String bookId,
			Integer bookPrices, Integer tip, String employeeId, Integer bookCount, Integer status) {
		super();
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.parentId = parentId;
		this.orderId = orderId;
		this.bookId = bookId;
		this.bookPrices = bookPrices;
		this.tip = tip;
		this.employeeId = employeeId;
		this.bookCount = bookCount;
		this.status = status;
	}

	public GetOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
