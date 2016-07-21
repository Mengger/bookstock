package com.bookrecovery.dao;

import org.springframework.stereotype.Repository;

@Repository("bookInfoCache")
public class BookInfoCache extends MongodbBaseDao{

	@Override
	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return BookInfoCache.class;
	}


}
