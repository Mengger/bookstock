package com.bookrecovery.entry;

public class BuyerInfo {

	/**
	*创建时间
	*/
	private java.util.Date createTime;

	/**
	*修改时间
	*/
	private java.util.Date modifyTime;

	/**
	*买家id
	*/
	private String buyerId;

	/**
	*买家姓名
	*/
	private String buyerName;

	/**
	*买家身份证
	*/
	private String buyerIdCard;

	/**
	*买家地址
	*/
	private String buyerAddress;

	/**
	*买家电话
	*/
	private String buyerPhone;

	/**
	*qq
	*/
	private String buyerQq;

	/**
	*e_mail
	*/
	private String buyerEMail;


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

	public void setBuyerId(String buyerId){
		this.buyerId = buyerId;
	}

	public String getBuyerId(){
		return this.buyerId;
	}

	public void setBuyerName(String buyerName){
		this.buyerName = buyerName;
	}

	public String getBuyerName(){
		return this.buyerName;
	}

	public void setBuyerIdCard(String buyerIdCard){
		this.buyerIdCard = buyerIdCard;
	}

	public String getBuyerIdCard(){
		return this.buyerIdCard;
	}

	public void setBuyerAddress(String buyerAddress){
		this.buyerAddress = buyerAddress;
	}

	public String getBuyerAddress(){
		return this.buyerAddress;
	}

	public void setBuyerPhone(String buyerPhone){
		this.buyerPhone = buyerPhone;
	}

	public String getBuyerPhone(){
		return this.buyerPhone;
	}

	public void setBuyerQq(String buyerQq){
		this.buyerQq = buyerQq;
	}

	public String getBuyerQq(){
		return this.buyerQq;
	}

	public void setBuyerEMail(String buyerEMail){
		this.buyerEMail = buyerEMail;
	}

	public String getBuyerEMail(){
		return this.buyerEMail;
	}

}