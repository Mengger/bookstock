package com.bookrecovery.test.dao;

import java.util.Date;

import javax.annotation.Resource;
import org.junit.Test;

import com.bookrecovery.test.TestLoadBean;
import com.alibaba.fastjson.JSONObject;
import com.bookrecovery.dao.EmployeeInfoDao;
import com.bookrecovery.entry.EmployeeInfo;
import com.bookrecovery.entry.result.SingleResultDO;

public class EmployeeInfoDaoTest extends TestLoadBean {

	@Resource
	private EmployeeInfoDao employeeInfoDao;
	
	@Test
	public void update(){
		EmployeeInfo aa=new EmployeeInfo();
		aa.setId(10000L);
		aa.setPwd("123abc");
		aa.setQq("1234567890");
		aa.setBirthPlace("河南省 郑州市 你猜县");
		aa.setIsReal(true);
		aa.setEMail("23433453453@qq.com");
		aa.setName("张三");
		aa.setBirthday(new Date());
		aa.setCreateTime(new Date());
		aa.setModifyTime(new Date());
		System.out.println(JSONObject.toJSONString(aa));
		System.out.println(employeeInfoDao.saveEmployeeInfo(aa));
	}
	
	public static void main(String[] args) {
		EmployeeInfo aa=new EmployeeInfo();
		aa.setId(10000L);
		aa.setPwd("123abc");
		aa.setQq("1234567890");
		aa.setBirthPlace("河南省 郑州市 你猜县");
		aa.setIsReal(true);
		aa.setEMail("23433453453@qq.com");
		aa.setName("张三");
		aa.setBirthday(new Date());
		aa.setCreateTime(new Date());
		aa.setModifyTime(new Date());
		SingleResultDO<EmployeeInfo> rtn=new SingleResultDO<EmployeeInfo>();
		rtn.setResult(aa);
		System.out.println(JSONObject.toJSONString(rtn));
	}
}
