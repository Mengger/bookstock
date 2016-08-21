package com.bookrecovery.test.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.bookrecovery.dao.GetOrderDao;
import com.bookrecovery.entry.GetOrder;
import com.bookrecovery.entry.result.PageRequest;
import com.bookrecovery.entry.result.PageResultDO;
import com.bookrecovery.test.TestLoadBean;

public class GetOrderDaoTest extends TestLoadBean {

	@Resource
	private GetOrderDao getOrderDao;
	
	@Test
	public void testInsert() throws InterruptedException{
		long orderId = 100000040L;
		for (int i = 0; i < 19; i++) {
			Thread.sleep(300L);
			GetOrder a = new GetOrder();
			a.setCreateTime(new Date(1471933490000l));
			a.setModifyTime(new Date(1471933490000l));
			a.setBookId("9787121257919");
			a.setBookPrices(250);
			a.setTip(25);
			a.setOrderId(String.valueOf(orderId));
			orderId++;
			a.setParentId("0");
			a.setBookCount(1);
			a.setEmployeeId(10000);
			getOrderDao.saveGetOrder(a);
		}
	}
	
	@Test
	public void testQuery(){
		PageRequest pageRequest = new PageRequest(0, 10);
		PageResultDO<GetOrder> rtn=new PageResultDO<GetOrder>();
		GetOrder a = new GetOrder();
		a.setEmployeeId(10000);
		//a.setPageRequest(pageRequest);
		a.setCreateBeginAfter(new Date(1571933490000l));
		a.setCreateBeginBefore(new Date(1071933490000l));
		//rtn.setTotalElements(45L);
		List<GetOrder> aaaa=getOrderDao.queryGetOrderList(a);
		System.out.println(JSONObject.toJSONString(aaaa));
		System.out.println(aaaa.size());
	}
	
	
	@Test
	public void testQueryCount(){
		PageRequest pageRequest = new PageRequest(0, 10);
		GetOrder a = new GetOrder();
		a.setEmployeeId(10000);
		//a.setPageRequest(pageRequest);
		a.setCreateBeginAfter(new Date(1571933490000l));
		a.setCreateBeginBefore(new Date(1071933490000l));
		System.out.println(getOrderDao.queryGetOrderCount(a));
	} 
}
