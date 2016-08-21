package com.bookrecovery.entry;

import java.util.Date;

import com.bookrecovery.entry.result.PageRequest;

public class GetOrder {
	
	private Date createBeginBefore;
	private Date createBeginAfter;
	private PageRequest pageRequest;

	/**
	*创建时间
	*/
	private java.util.Date createTime;

	/**
	*修改时间
	*/
	private java.util.Date modifyTime;

	/**
	*父订单id
	*/
	private String parentId;

	/**
	*子订单id
	*/
	private String orderId;

	/**
	*图书编号
	*/
	private String bookId;

	/**
	*回收价格
	*/
	private Integer bookPrices;

	/**
	*回收小费
	*/
	private Integer tip;

	/**
	*雇员id
	*/
	private Integer employeeId;

	/**
	*图书数量
	*/
	private Integer bookCount;

	/**
	*订单状态(-1.订单作废 0.读取完毕 1.雇员回收完成)
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

	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	public String getParentId(){
		return this.parentId;
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

	public void setBookPrices(Integer bookPrices){
		this.bookPrices = bookPrices;
	}

	public Integer getBookPrices(){
		return this.bookPrices;
	}

	public void setTip(Integer tip){
		this.tip = tip;
	}

	public Integer getTip(){
		return this.tip;
	}

	public void setEmployeeId(Integer employeeId){
		this.employeeId = employeeId;
	}

	public Integer getEmployeeId(){
		return this.employeeId;
	}

	public void setBookCount(Integer bookCount){
		this.bookCount = bookCount;
	}

	public Integer getBookCount(){
		return this.bookCount;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public Date getCreateBeginBefore() {
		return createBeginBefore;
	}

	public void setCreateBeginBefore(Date createBeginBefore) {
		this.createBeginBefore = createBeginBefore;
	}

	public Date getCreateBeginAfter() {
		return createBeginAfter;
	}

	public void setCreateBeginAfter(Date createBeginAfter) {
		this.createBeginAfter = createBeginAfter;
	}

	public PageRequest getPageRequest() {
		return pageRequest;
	}

	public void setPageRequest(PageRequest pageRequest) {
		this.pageRequest = pageRequest;
	}

}