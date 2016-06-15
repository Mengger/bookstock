package com.chillax.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.chillax.entry.BookInfo;

@Repository("book_info_dao")
public interface BookInfoDao {
	
	@Select("select * from book_info where book_id=#{bookId}")
	@Results({
		@Result(property="infoCreateTime",column="info_create_time"),
		@Result(property="infoModifyTime",column="info_modify_time"),
		@Result(property="bookId",column="book_id"),
		@Result(property="bookProtocl",column="book_protocl"),
		@Result(property="bookName",column="book_name"),
		@Result(property="author",column="author"),
		@Result(property="bookConcerm",column="book_concerm"),
		@Result(property="status",column="status"),
		@Result(property="bookTypeId",column="book_type_id"),
		@Result(property="orderPrices",column="order_prices"),
		@Result(property="usedPrices",column="used_prices"),
		@Result(property="photoPath",column="photo_path"),
		@Result(property="pageNum",column="page_num")
	})
    BookInfo queryBookInfoById(String bookId);  
	
	@Insert("insert into book_info (info_create_time,info_modify_time,book_id,book_protocl,book_name,author,book_concerm,status,book_type_id, order_prices,used_prices,photo_path,local_photo_path,page_num)"
		+ "values(now(),now(),#{bookId},#{bookProtocl},#{bookName},#{author},#{bookConcerm},#{status},#{bookTypeId},#{orderPrices},#{usedPrices},#{photoPath},#{localPhotoPath},#{pageNum})")
	int saveBookInfo(BookInfo book);
	
	@Update("update book_info set info_modify_time = now(),book_protocl = #{bookProtocl} ,book_name = #{bookName},author =#{author},book_concerm = #{bookConcerm},status = #{status},"
			+ "book_type_id = #{bookTypeId}, order_prices = #{orderPrices},used_prices = #{usedPrices},photo_path = #{photoPath},page_num = #{pageNum}) where book_id = #{bookId}")
	int updateBookInfo(BookInfo book);
}
