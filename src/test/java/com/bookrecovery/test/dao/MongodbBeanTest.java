package com.bookrecovery.test.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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
		bean.setBookId(1000000000L);
		bean.setAuthor("lisi");
		bean.setBookName("bookname");
		bookInfoCache.insert(bean);
	}
	
	@Test
	public void testQueryInfo(){
		Query query=new Query();
		Criteria criteria = new Criteria();
		criteria.where("author").is("lisi");
		query.addCriteria(criteria);
		System.out.println(JSON.toJSONString(bookInfoCache.find(query)));
		System.out.println(JSON.toJSONString(bookInfoCache.findOne(query)));
	}
	
	
	@Test
	public void testDeleteInfo(){
		Query query=new Query();
		Criteria criteria = new Criteria();
		criteria.where("author").is("lisi");
		query.addCriteria(criteria);
		System.out.println(JSON.toJSONString(bookInfoCache.find(query)));
		bookInfoCache.findAllAndRemove(query);
	}
	
	@Test
	public void testUpdateInfo(){
		Query query=new Query();
		Criteria criteria = new Criteria();
		criteria.where("author").is("wangwu");
		query.addCriteria(criteria);
		bookInfoCache.updateAll(query, Update.update("author", "lisi"));
	}
}
