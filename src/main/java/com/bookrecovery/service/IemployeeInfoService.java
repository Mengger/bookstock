package com.bookrecovery.service;

import java.util.List;

import com.bookrecovery.entry.EmployeeInfo;

public interface IemployeeInfoService {

	public List<EmployeeInfo> queryEmployeeInfoList(EmployeeInfo employee);
}