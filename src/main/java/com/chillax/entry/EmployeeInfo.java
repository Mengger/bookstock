package com.chillax.entry;

import java.util.Date;

/**
 * 雇员信息
 * @author jack
 *
 */
public class EmployeeInfo {

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 修改时间
	 */
	private Date modifyTime;
	
	/**
	 * 雇员id
	 */
	private String id ;
	
	/**
	 * 雇员名字
	 */
	private String name;
	
	/**
	 * 身份证号码
	 */
	private String idCard;
	
	/**
	 * 出生地
	 */
	private String birthPlace ;
	
	/**
	 * 密码
	 */
	private String pwd;
	
	/**
	 * 经理id
	 */
	private String managerId ;
	
	/**
	 * 区域id
	 */
	private String areaId ;
	
	/**
	 * 学校id
	 */
	private String schoolId;
	
	/**
	 * 图片
	 */
	private String photoPath;
	
	/**
	 * 身份证图片
	 */
	private String idCardPath;
	
	/**
	 * 备注信息(地址)
	 */
	private String info;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getIdCardPath() {
		return idCardPath;
	}

	public void setIdCardPath(String idCardPath) {
		this.idCardPath = idCardPath;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public EmployeeInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmployeeInfo(Date createTime, Date modifyTime, String id, String name, String idCard, String birthPlace,
			String pwd, String managerId, String areaId, String schoolId, String photoPath, String idCardPath,
			String info) {
		super();
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.id = id;
		this.name = name;
		this.idCard = idCard;
		this.birthPlace = birthPlace;
		this.pwd = pwd;
		this.managerId = managerId;
		this.areaId = areaId;
		this.schoolId = schoolId;
		this.photoPath = photoPath;
		this.idCardPath = idCardPath;
		this.info = info;
	}
	
	
}
