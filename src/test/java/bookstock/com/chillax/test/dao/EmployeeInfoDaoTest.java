package bookstock.com.chillax.test.dao;

import javax.annotation.Resource;

import org.junit.Test;

import com.chillax.dao.EmployeeDao;
import com.chillax.entry.EmployeeInfo;

import bookstock.com.chillax.test.TestLoadBean;

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
