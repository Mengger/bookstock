package com.chillax.dao;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.chillax.entry.BookInfo;

@Repository("book_info_dao")
public interface BookInfoDao {
	
	@Select("select * from book_info where book_id=#{bookId}")
    BookInfo queryBookInfoById(String bookId);  
}
