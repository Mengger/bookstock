package com.bookrecovery.entry;

public class EmployeeInfo {

	/**
	*创建时间
	*/
	private java.util.Date createTime;

	/**
	*修改时间
	*/
	private java.util.Date modifyTime;

	/**
	*雇员id
	*/
	private String id;

	/**
	*雇员名字
	*/
	private String name;

	/**
	*身份证号码
	*/
	private String idCard;

	/**
	*出生地
	*/
	private String birthPlace;

	/**
	*密码
	*/
	private String pwd;

	/**
	*经理id
	*/
	private String managerId;

	/**
	*区域id
	*/
	private String areaId;

	/**
	*学校id
	*/
	private String schoolId;

	/**
	*图片
	*/
	private String photoPath;

	/**
	*身份证图片
	*/
	private String idCardPath;

	/**
	*备注信息(地址)
	*/
	private String info;


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

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}

	public void setIdCard(String idCard){
		this.idCard = idCard;
	}

	public String getIdCard(){
		return this.idCard;
	}

	public void setBirthPlace(String birthPlace){
		this.birthPlace = birthPlace;
	}

	public String getBirthPlace(){
		return this.birthPlace;
	}

	public void setPwd(String pwd){
		this.pwd = pwd;
	}

	public String getPwd(){
		return this.pwd;
	}

	public void setManagerId(String managerId){
		this.managerId = managerId;
	}

	public String getManagerId(){
		return this.managerId;
	}

	public void setAreaId(String areaId){
		this.areaId = areaId;
	}

	public String getAreaId(){
		return this.areaId;
	}

	public void setSchoolId(String schoolId){
		this.schoolId = schoolId;
	}

	public String getSchoolId(){
		return this.schoolId;
	}

	public void setPhotoPath(String photoPath){
		this.photoPath = photoPath;
	}

	public String getPhotoPath(){
		return this.photoPath;
	}

	public void setIdCardPath(String idCardPath){
		this.idCardPath = idCardPath;
	}

	public String getIdCardPath(){
		return this.idCardPath;
	}

	public void setInfo(String info){
		this.info = info;
	}

	public String getInfo(){
		return this.info;
	}

}