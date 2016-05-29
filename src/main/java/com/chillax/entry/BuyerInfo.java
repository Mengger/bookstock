package com.chillax.entry;

import java.util.Date;

public class BuyerInfo {

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date modifyTime;

	/**
	 * 买家id
	 */
	private String buyerId;

	/**
	 * 买家姓名
	 */
	private String buyerName ;

	/**
	 * 买家身份证
	 */
	private String buyerIdCard;

	/**
	 * 买家地址
	 */
	private String buyerAddress;

	/**
	 * 买家电话
	 */
	private String buyerPhone;

	/**
	 * qq
	 */
	private String buyerQQ;

	/**
	 * e_mail
	 */
	private String buyerEMail;

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

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerIdCard() {
		return buyerIdCard;
	}

	public void setBuyerIdCard(String buyerIdCard) {
		this.buyerIdCard = buyerIdCard;
	}

	public String getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(String buyerAddress) {
		this.buyerAddress = buyerAddress;
	}

	public String getBuyerPhone() {
		return buyerPhone;
	}

	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}

	public String getBuyerQQ() {
		return buyerQQ;
	}

	public void setBuyerQQ(String buyerQQ) {
		this.buyerQQ = buyerQQ;
	}

	public String getBuyerEMail() {
		return buyerEMail;
	}

	public void setBuyerEMail(String buyerEMail) {
		this.buyerEMail = buyerEMail;
	}

	public BuyerInfo(Date createTime, Date modifyTime, String buyerId, String buyerName, String buyerIdCard,
			String buyerAddress, String buyerPhone, String buyerQQ, String buyerEMail) {
		super();
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.buyerId = buyerId;
		this.buyerName = buyerName;
		this.buyerIdCard = buyerIdCard;
		this.buyerAddress = buyerAddress;
		this.buyerPhone = buyerPhone;
		this.buyerQQ = buyerQQ;
		this.buyerEMail = buyerEMail;
	}

	public BuyerInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
