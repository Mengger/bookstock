package com.bookrecovery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.bookrecovery.entry.BuyerInfo;



@Repository("buyer_info_dao")

public interface BuyerInfoDao {

	@Select("<script>select * from buyer_info where 1=1 "
		+"<if test=\"createTime !=null \">and create_time = #{createTime} </if>"
		+"<if test=\"modifyTime !=null \">and modify_time = #{modifyTime} </if>"
		+"<if test=\"buyerId !=null \">and buyer_id = #{buyerId} </if>"
		+"<if test=\"buyerName !=null \">and buyer_name = #{buyerName} </if>"
		+"<if test=\"buyerIdCard !=null \">and buyer_id_card = #{buyerIdCard} </if>"
		+"<if test=\"buyerAddress !=null \">and buyer_address = #{buyerAddress} </if>"
		+"<if test=\"buyerPhone !=null \">and buyer_phone = #{buyerPhone} </if>"
		+"<if test=\"buyerQq !=null \">and buyer_qq = #{buyerQq} </if>"
		+"<if test=\"buyerEMail !=null \">and buyer_e_mail = #{buyerEMail} </if>"
	+"</script>")

	@Results({
		@Result(property="createTime",column="create_time"),
		@Result(property="modifyTime",column="modify_time"),
		@Result(property="buyerId",column="buyer_id"),
		@Result(property="buyerName",column="buyer_name"),
		@Result(property="buyerIdCard",column="buyer_id_card"),
		@Result(property="buyerAddress",column="buyer_address"),
		@Result(property="buyerPhone",column="buyer_phone"),
		@Result(property="buyerQq",column="buyer_qq"),
		@Result(property="buyerEMail",column="buyer_e_mail")
	})

	List<BuyerInfo> queryBuyerInfoList(BuyerInfo buyerInfo);

	@Update("<script>update buyer_info "
		+"<set>"
			+ "<if test=\"createTime !=null \"> create_time = #{createTime} ,</if>"
			+ "<if test=\"modifyTime !=null \"> modify_time = #{modifyTime} ,</if>"
			+ "<if test=\"buyerId !=null \"> buyer_id = #{buyerId} ,</if>"
			+ "<if test=\"buyerName !=null \"> buyer_name = #{buyerName} ,</if>"
			+ "<if test=\"buyerIdCard !=null \"> buyer_id_card = #{buyerIdCard} ,</if>"
			+ "<if test=\"buyerAddress !=null \"> buyer_address = #{buyerAddress} ,</if>"
			+ "<if test=\"buyerPhone !=null \"> buyer_phone = #{buyerPhone} ,</if>"
			+ "<if test=\"buyerQq !=null \"> buyer_qq = #{buyerQq} ,</if>"
			+ "<if test=\"buyerEMail !=null \"> buyer_e_mail = #{buyerEMail} ,</if>"
		+"</set>"
		+"where 1=1 "
	+ "</script>")

	int updateBuyerInfo(BuyerInfo buyerInfo);

	@Insert("insert into buyer_info (create_time , modify_time , buyer_id , buyer_name , buyer_id_card , buyer_address , buyer_phone , buyer_qq , buyer_e_mail) "
				+"values( #{createTime} , #{modifyTime} , #{buyerId} , #{buyerName} , #{buyerIdCard} , #{buyerAddress} , #{buyerPhone} , #{buyerQq} , #{buyerEMail} )")
	int saveBuyerInfo (BuyerInfo buyerInfo);

	@Delete("delete from buyer_info where ")
	int deletebuyerInfo(BuyerInfo buyerInfo);

}