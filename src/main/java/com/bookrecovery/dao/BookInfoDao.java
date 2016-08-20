package com.bookrecovery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.bookrecovery.entry.BookInfo;



@Repository("book_info_dao")

public interface BookInfoDao {

	@Select("<script>select * from book_info where 1=1 "
		+"<if test=\"bookId !=null \">and book_id = #{bookId} </if>"
		+"<if test=\"infoCreateTime !=null \">and info_create_time = #{infoCreateTime} </if>"
		+"<if test=\"infoModifyTime !=null \">and info_modify_time = #{infoModifyTime} </if>"
		+"<if test=\"bookProtocl !=null \">and book_protocl = #{bookProtocl} </if>"
		+"<if test=\"bookName !=null \">and book_name = #{bookName} </if>"
		+"<if test=\"author !=null \">and author = #{author} </if>"
		+"<if test=\"bookConcerm !=null \">and book_concerm = #{bookConcerm} </if>"
		+"<if test=\"status !=null \">and status = #{status} </if>"
		+"<if test=\"bookTypeId !=null \">and book_type_id = #{bookTypeId} </if>"
		+"<if test=\"orderPrices !=null \">and order_prices = #{orderPrices} </if>"
		+"<if test=\"usedPrices !=null \">and used_prices = #{usedPrices} </if>"
		+"<if test=\"photoPath !=null \">and photo_path = #{photoPath} </if>"
		+"<if test=\"localPhotoPath !=null \">and local_photo_path = #{localPhotoPath} </if>"
		+"<if test=\"photoUse !=null \">and photo_use = #{photoUse} </if>"
		+"<if test=\"pageNum !=null \">and page_num = #{pageNum} </if>"
		+"<if test=\"createrId !=null \">and creater_id = #{createrId} </if>"
		+"<if test=\"infoStatus !=null \">and info_status = #{infoStatus} </if>"
		+"<if test=\"bookCreateWay !=null \">and book_create_way = #{bookCreateWay} </if>"
	+"</script>")

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
		@Result(property="localPhotoPath",column="local_photo_path"),
		@Result(property="photoUse",column="photo_use"),
		@Result(property="pageNum",column="page_num"),
		@Result(property="createrId",column="creater_id"),
		@Result(property="infoStatus",column="info_status"),
		@Result(property="bookCreateWay",column="book_create_way")
	})

	List<BookInfo> queryBookInfoList(BookInfo bookInfo);

	@Update("<script>update book_info "
		+"<set>"
			+ "<if test=\"infoCreateTime !=null \"> info_create_time = #{infoCreateTime} ,</if>"
			+ "<if test=\"infoModifyTime !=null \"> info_modify_time = #{infoModifyTime} ,</if>"
			+ "<if test=\"bookProtocl !=null \"> book_protocl = #{bookProtocl} ,</if>"
			+ "<if test=\"bookName !=null \"> book_name = #{bookName} ,</if>"
			+ "<if test=\"author !=null \"> author = #{author} ,</if>"
			+ "<if test=\"bookConcerm !=null \"> book_concerm = #{bookConcerm} ,</if>"
			+ "<if test=\"status !=null \"> status = #{status} ,</if>"
			+ "<if test=\"bookTypeId !=null \"> book_type_id = #{bookTypeId} ,</if>"
			+ "<if test=\"orderPrices !=null \"> order_prices = #{orderPrices} ,</if>"
			+ "<if test=\"usedPrices !=null \"> used_prices = #{usedPrices} ,</if>"
			+ "<if test=\"photoPath !=null \"> photo_path = #{photoPath} ,</if>"
			+ "<if test=\"localPhotoPath !=null \"> local_photo_path = #{localPhotoPath} ,</if>"
			+ "<if test=\"photoUse !=null \"> photo_use = #{photoUse} ,</if>"
			+ "<if test=\"pageNum !=null \"> page_num = #{pageNum} ,</if>"
			+ "<if test=\"createrId !=null \"> creater_id = #{createrId} ,</if>"
			+ "<if test=\"infoStatus !=null \"> info_status = #{infoStatus} ,</if>"
			+ "<if test=\"bookCreateWay !=null \"> book_create_way = #{bookCreateWay} ,</if>"
		+"</set>"
		+"where 1=1 "
		+ "<if test=\"bookId !=null \"> and book_id = #{bookId} </if>"
	+ "</script>")

	int updateBookInfo(BookInfo bookInfo);

	@Insert("insert into book_info (info_create_time , info_modify_time , book_id , book_protocl , book_name , author , book_concerm , status , book_type_id , order_prices , used_prices , photo_path , local_photo_path , photo_use , page_num , creater_id , info_status , book_create_way) "
				+"values( #{infoCreateTime} , now() , #{bookId} , #{bookProtocl} , #{bookName} , #{author} , #{bookConcerm} , #{status} , #{bookTypeId} , #{orderPrices} , #{usedPrices} , #{photoPath} , #{localPhotoPath} , #{photoUse} , #{pageNum} , #{createrId} , #{infoStatus} , #{bookCreateWay} )")
	int saveBookInfo (BookInfo bookInfo);

	@Delete("delete from book_info where book_id = #{bookId}")
	int deletebookInfo(BookInfo bookInfo);

}