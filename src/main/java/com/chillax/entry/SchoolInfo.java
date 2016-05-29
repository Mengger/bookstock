package com.chillax.entry;

import java.util.Date;

public class SchoolInfo {

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date modifyTime ;

	/**
	 * 学校id
	 */
	private String schoolId;

	/**
	 * 学校名称
	 */
	private String schoolName ;

	/**
	 * 学校地址(主要地址)
	 */
	private String schoolAddress ;

	/**
	 * 学校区块id
	 */
	private String areaId ;

	/**
	 * 区块名称
	 */
	private String areaName;

	/**
	 * 区块地址
	 */
	private String areaAddress;

	/**
	 * 区块经纬度(经度｜纬度)
	 */
	private String pox;

	/**
	 * 区块负责任id
	 */
	private String headId ;

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

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSchoolAddress() {
		return schoolAddress;
	}

	public void setSchoolAddress(String schoolAddress) {
		this.schoolAddress = schoolAddress;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaAddress() {
		return areaAddress;
	}

	public void setAreaAddress(String areaAddress) {
		this.areaAddress = areaAddress;
	}

	public String getPox() {
		return pox;
	}

	public void setPox(String pox) {
		this.pox = pox;
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public SchoolInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SchoolInfo(Date createTime, Date modifyTime, String schoolId, String schoolName, String schoolAddress,
			String areaId, String areaName, String areaAddress, String pox, String headId) {
		super();
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.schoolId = schoolId;
		this.schoolName = schoolName;
		this.schoolAddress = schoolAddress;
		this.areaId = areaId;
		this.areaName = areaName;
		this.areaAddress = areaAddress;
		this.pox = pox;
		this.headId = headId;
	}
	
	
}
