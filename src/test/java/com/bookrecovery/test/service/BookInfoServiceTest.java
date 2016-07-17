package com.bookrecovery.test.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.bookrecovery.test.TestLoadBean;
import com.bookrecovery.service.IbookInfoService;

public class BookInfoServiceTest extends TestLoadBean{

	@Resource
	private IbookInfoService bookInfoService;

	@Test
	public void testQueryDBInternet(){
		System.out.println(JSON.toJSON(bookInfoService.queryDBThenInterNet("9787111212508")));
	}
}
