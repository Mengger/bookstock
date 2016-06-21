package com.chillax.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chillax.dao.EmployeeDao;
import com.chillax.entry.EmployeeInfo;
import com.chillax.service.IemployeeInfoServiceImpl;

@Service(value="employeeInfoService")
public class EmployeeInfoService implements IemployeeInfoServiceImpl {

	private static final Logger log = LoggerFactory.getLogger(EmployeeInfoService.class);
	@Autowired
	public EmployeeDao employeeDao;
	
	@Override
	public List<EmployeeInfo> queryEmployeeInfoList(EmployeeInfo employee) {
		List<EmployeeInfo> rtn=null;
		try {
			rtn=employeeDao.queryEmployInfoList(employee);
		} catch (Exception e) {
			log.error("queryEmployeeInfoList error",e);
		}
		return rtn;
	}

}
