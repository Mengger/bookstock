package com.chillax.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.chillax.entry.EmployeeInfo;

@Repository("employee_dao")
public interface EmployeeDao {

	@Select("select * from employee_info where 1=1")
	List<EmployeeInfo> queryEmployInfoList();
	
}
