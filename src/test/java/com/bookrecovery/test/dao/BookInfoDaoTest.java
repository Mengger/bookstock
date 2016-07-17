package com.bookrecovery.test.dao;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.bookrecovery.test.TestLoadBean;
import com.bookrecovery.dao.BookInfoDao;

public class BookInfoDaoTest extends TestLoadBean{

	@Resource
	private BookInfoDao bookInfoDao;
	
	@Test
	public void testQueryBookInfoById(){
		System.out.println(JSON.toJSON(bookInfoDao.queryBookInfoById("9787111212508")));
	}
	
}
