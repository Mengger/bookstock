package com.bookrecovery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.bookrecovery.entry.SaleOrder;



@Repository("sale_order_dao")

public interface SaleOrderDao {

	@Select("<script>select * from sale_order where 1=1 "
		+"<if test=\"createTime !=null \">and create_time = #{createTime} </if>"
		+"<if test=\"modifyTime !=null \">and modify_time = #{modifyTime} </if>"
		+"<if test=\"orderId !=null \">and order_id = #{orderId} </if>"
		+"<if test=\"bookId !=null \">and book_id = #{bookId} </if>"
		+"<if test=\"prices !=null \">and prices = #{prices} </if>"
		+"<if test=\"orderNum !=null \">and order_num = #{orderNum} </if>"
		+"<if test=\"buyerId !=null \">and buyer_id = #{buyerId} </if>"
		+"<if test=\"status !=null \">and status = #{status} </if>"
	+"</script>")

	@Results({
		@Result(property="createTime",column="create_time"),
		@Result(property="modifyTime",column="modify_time"),
		@Result(property="orderId",column="order_id"),
		@Result(property="bookId",column="book_id"),
		@Result(property="prices",column="prices"),
		@Result(property="orderNum",column="order_num"),
		@Result(property="buyerId",column="buyer_id"),
		@Result(property="status",column="status")
	})

	List<SaleOrder> querySaleOrderList(SaleOrder saleOrder);

	@Update("<script>update sale_order "
		+"<set>"
			+ "<if test=\"createTime !=null \"> create_time = #{createTime} ,</if>"
			+ "<if test=\"modifyTime !=null \"> modify_time = #{modifyTime} ,</if>"
			+ "<if test=\"orderId !=null \"> order_id = #{orderId} ,</if>"
			+ "<if test=\"bookId !=null \"> book_id = #{bookId} ,</if>"
			+ "<if test=\"prices !=null \"> prices = #{prices} ,</if>"
			+ "<if test=\"orderNum !=null \"> order_num = #{orderNum} ,</if>"
			+ "<if test=\"buyerId !=null \"> buyer_id = #{buyerId} ,</if>"
			+ "<if test=\"status !=null \"> status = #{status} ,</if>"
		+"</set>"
		+"where 1=1 "
	+ "</script>")

	int updateSaleOrder(SaleOrder saleOrder);

	@Insert("insert into sale_order (create_time , modify_time , order_id , book_id , prices , order_num , buyer_id , status) "
				+"values( #{createTime} , #{modifyTime} , #{orderId} , #{bookId} , #{prices} , #{orderNum} , #{buyerId} , #{status} )")
	int saveSaleOrder (SaleOrder saleOrder);

	@Delete("delete from sale_order where ")
	int deletesaleOrder(SaleOrder saleOrder);

}