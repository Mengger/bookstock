package com.bookrecovery.dao;

import org.springframework.stereotype.Repository;

import com.bookrecovery.entry.BookInfo;

@Repository("bookInfoCache")
public class BookInfoCache extends MongodbBaseDao<BookInfo>{

	@Override
	protected Class<BookInfo> getEntityClass() {
		// TODO Auto-generated method stub
		return BookInfo.class;
	}


}
