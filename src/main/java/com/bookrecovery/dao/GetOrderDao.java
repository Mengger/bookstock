package com.bookrecovery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.bookrecovery.entry.GetOrder;



@Repository("get_order_dao")

public interface GetOrderDao {

	@Select("<script>select * from get_order where 1=1 "
		+"<if test=\"orderId !=null \">and order_id = #{orderId} </if>"
		+"<if test=\"createTime !=null \">and create_time = #{createTime} </if>"
		+"<if test=\"modifyTime !=null \">and modify_time = #{modifyTime} </if>"
		+"<if test=\"parentId !=null \">and parent_id = #{parentId} </if>"
		+"<if test=\"bookId !=null \">and book_id = #{bookId} </if>"
		+"<if test=\"bookPrices !=null \">and book_prices = #{bookPrices} </if>"
		+"<if test=\"tip !=null \">and tip = #{tip} </if>"
		+"<if test=\"employeeId !=null \">and employee_id = #{employeeId} </if>"
		+"<if test=\"bookCount !=null \">and book_count = #{bookCount} </if>"
		+"<if test=\"status !=null \">and status = #{status} </if>"
	+"</script>")

	@Results({
		@Result(property="createTime",column="create_time"),
		@Result(property="modifyTime",column="modify_time"),
		@Result(property="parentId",column="parent_id"),
		@Result(property="orderId",column="order_id"),
		@Result(property="bookId",column="book_id"),
		@Result(property="bookPrices",column="book_prices"),
		@Result(property="tip",column="tip"),
		@Result(property="employeeId",column="employee_id"),
		@Result(property="bookCount",column="book_count"),
		@Result(property="status",column="status")
	})

	List<GetOrder> queryGetOrderList(GetOrder getOrder);

	@Update("<script>update get_order "
		+"<set>"
			+ "<if test=\"createTime !=null \"> create_time = #{createTime} ,</if>"
			+ "<if test=\"modifyTime !=null \"> modify_time = #{modifyTime} ,</if>"
			+ "<if test=\"parentId !=null \"> parent_id = #{parentId} ,</if>"
			+ "<if test=\"bookId !=null \"> book_id = #{bookId} ,</if>"
			+ "<if test=\"bookPrices !=null \"> book_prices = #{bookPrices} ,</if>"
			+ "<if test=\"tip !=null \"> tip = #{tip} ,</if>"
			+ "<if test=\"employeeId !=null \"> employee_id = #{employeeId} ,</if>"
			+ "<if test=\"bookCount !=null \"> book_count = #{bookCount} ,</if>"
			+ "<if test=\"status !=null \"> status = #{status} ,</if>"
		+"</set>"
		+"where 1=1 "
		+ "<if test=\"orderId !=null \"> and order_id = #{orderId} </if>"
	+ "</script>")

	int updateGetOrder(GetOrder getOrder);

	@Insert("insert into get_order (create_time , modify_time , parent_id , order_id , book_id , book_prices , tip , employee_id , book_count , status) "
				+"values( #{createTime} , #{modifyTime} , #{parentId} , #{orderId} , #{bookId} , #{bookPrices} , #{tip} , #{employeeId} , #{bookCount} , #{status} )")
	int saveGetOrder (GetOrder getOrder);

	@Delete("delete from get_order where order_id = #{orderId}")
	int deletegetOrder(GetOrder getOrder);

}