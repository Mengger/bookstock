package com.bookrecovery.test.dao;

import javax.annotation.Resource;

import org.junit.Test;

import com.bookrecovery.test.TestLoadBean;
import com.bookrecovery.dao.EmployeeDao;
import com.bookrecovery.entry.EmployeeInfo;

public class EmployeeInfoDaoTest extends TestLoadBean {

	@Resource
	private EmployeeDao test;
	
	@Test
	public void update(){
		EmployeeInfo aa=new EmployeeInfo();
		aa.setId("1001");
		aa.setPwd("222");
		aa.setName("wudi");
		System.out.println(test.updateEmployInfo(aa));
	}
}
