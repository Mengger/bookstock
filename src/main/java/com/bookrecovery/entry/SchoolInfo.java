package com.bookrecovery.entry;

public class SchoolInfo {

	/**
	*创建时间
	*/
	private java.util.Date createTime;

	/**
	*修改时间
	*/
	private java.util.Date modifyTime;

	/**
	*学校id
	*/
	private String schoolId;

	/**
	*学校名称
	*/
	private String schoolName;

	/**
	*学校地址(主要地址)
	*/
	private String schoolAddress;

	/**
	*学校区块id
	*/
	private String areaId;

	/**
	*区块名称
	*/
	private String areaName;

	/**
	*区块地址
	*/
	private String areaAddress;

	/**
	*区块经纬度(经度｜纬度)
	*/
	private String pox;

	/**
	*区块负责任id
	*/
	private String headId;


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

	public void setSchoolId(String schoolId){
		this.schoolId = schoolId;
	}

	public String getSchoolId(){
		return this.schoolId;
	}

	public void setSchoolName(String schoolName){
		this.schoolName = schoolName;
	}

	public String getSchoolName(){
		return this.schoolName;
	}

	public void setSchoolAddress(String schoolAddress){
		this.schoolAddress = schoolAddress;
	}

	public String getSchoolAddress(){
		return this.schoolAddress;
	}

	public void setAreaId(String areaId){
		this.areaId = areaId;
	}

	public String getAreaId(){
		return this.areaId;
	}

	public void setAreaName(String areaName){
		this.areaName = areaName;
	}

	public String getAreaName(){
		return this.areaName;
	}

	public void setAreaAddress(String areaAddress){
		this.areaAddress = areaAddress;
	}

	public String getAreaAddress(){
		return this.areaAddress;
	}

	public void setPox(String pox){
		this.pox = pox;
	}

	public String getPox(){
		return this.pox;
	}

	public void setHeadId(String headId){
		this.headId = headId;
	}

	public String getHeadId(){
		return this.headId;
	}

}