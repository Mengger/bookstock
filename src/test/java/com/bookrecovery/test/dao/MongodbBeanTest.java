package com.bookrecovery.test.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.alibaba.fastjson.JSON;
import com.bookrecovery.dao.BookInfoCache;
import com.bookrecovery.entry.BookInfo;
import com.bookrecovery.test.TestLoadBean;

public class MongodbBeanTest extends TestLoadBean {

	@Autowired
	private BookInfoCache bookInfoCache;
	
	@Test
	public void testInsertInfo(){
		BookInfo bean=new BookInfo();
		bean.setAuthor("lisi");
		bean.setBookConcerm("concern");
		bean.setBookId(10978711111l);
		bean.setBookName("bookname");
		System.out.println(JSON.toJSONString(bookInfoCache.save(bean)));
	}
	
	@Test
	public void testQueryInfo(){
		Query query=new Query();
		Criteria criteria = new Criteria();
		criteria.where("author").is("lisi");
		query.addCriteria(criteria);
		System.out.println(JSON.toJSONString(bookInfoCache.find(query)));
	}
}
