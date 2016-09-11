package com.bookrecovery.service;

import java.util.List;

import com.bookrecovery.entry.EmployeeInfo;

public interface IemployeeInfoService {

	public List<EmployeeInfo> queryEmployeeInfoList(EmployeeInfo employee);
	
	/**
	 * 修改雇员信息  通过主键
	 * @param employee
	 * @return
	 */
	public boolean modifyEmployeeInfoById(EmployeeInfo employee);
}
