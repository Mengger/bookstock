package com.bookrecovery.test.dao;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.bookrecovery.test.TestLoadBean;
import com.bookrecovery.dao.BookInfoDao;
import com.bookrecovery.entry.BookInfo;

public class BookInfoDaoTest extends TestLoadBean{

	@Resource
	private BookInfoDao bookInfoDao;
	
	@Test
	public void testQueryBookInfoById(){
		BookInfo bookInfo =new BookInfo();
		bookInfo.setBookId("9787111212508");
		System.out.println(JSON.toJSON(bookInfoDao.queryBookInfoList(bookInfo)));
	}
	
}
