package com.bookrecovery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.bookrecovery.entry.SchoolInfo;



@Repository("school_info_dao")

public interface SchoolInfoDao {

	@Select("<script>select * from school_info where 1=1 "
		+"<if test=\"createTime !=null \">and create_time = #{createTime} </if>"
		+"<if test=\"modifyTime !=null \">and modify_time = #{modifyTime} </if>"
		+"<if test=\"schoolId !=null \">and school_id = #{schoolId} </if>"
		+"<if test=\"schoolName !=null \">and school_name = #{schoolName} </if>"
		+"<if test=\"schoolAddress !=null \">and school_address = #{schoolAddress} </if>"
		+"<if test=\"areaId !=null \">and area_id = #{areaId} </if>"
		+"<if test=\"areaName !=null \">and area_name = #{areaName} </if>"
		+"<if test=\"areaAddress !=null \">and area_address = #{areaAddress} </if>"
		+"<if test=\"pox !=null \">and pox = #{pox} </if>"
		+"<if test=\"headId !=null \">and head_id = #{headId} </if>"
	+"</script>")

	@Results({
		@Result(property="createTime",column="create_time"),
		@Result(property="modifyTime",column="modify_time"),
		@Result(property="schoolId",column="school_id"),
		@Result(property="schoolName",column="school_name"),
		@Result(property="schoolAddress",column="school_address"),
		@Result(property="areaId",column="area_id"),
		@Result(property="areaName",column="area_name"),
		@Result(property="areaAddress",column="area_address"),
		@Result(property="pox",column="pox"),
		@Result(property="headId",column="head_id")
	})

	List<SchoolInfo> querySchoolInfoList(SchoolInfo schoolInfo);

	@Update("<script>update school_info "
		+"<set>"
			+ "<if test=\"createTime !=null \"> create_time = #{createTime} ,</if>"
			+ "<if test=\"modifyTime !=null \"> modify_time = #{modifyTime} ,</if>"
			+ "<if test=\"schoolId !=null \"> school_id = #{schoolId} ,</if>"
			+ "<if test=\"schoolName !=null \"> school_name = #{schoolName} ,</if>"
			+ "<if test=\"schoolAddress !=null \"> school_address = #{schoolAddress} ,</if>"
			+ "<if test=\"areaId !=null \"> area_id = #{areaId} ,</if>"
			+ "<if test=\"areaName !=null \"> area_name = #{areaName} ,</if>"
			+ "<if test=\"areaAddress !=null \"> area_address = #{areaAddress} ,</if>"
			+ "<if test=\"pox !=null \"> pox = #{pox} ,</if>"
			+ "<if test=\"headId !=null \"> head_id = #{headId} ,</if>"
		+"</set>"
		+"where 1=1 "
	+ "</script>")

	int updateSchoolInfo(SchoolInfo schoolInfo);

	@Insert("insert into school_info (create_time , modify_time , school_id , school_name , school_address , area_id , area_name , area_address , pox , head_id) "
				+"values( #{createTime} , #{modifyTime} , #{schoolId} , #{schoolName} , #{schoolAddress} , #{areaId} , #{areaName} , #{areaAddress} , #{pox} , #{headId} )")
	int saveSchoolInfo (SchoolInfo schoolInfo);

	@Delete("delete from school_info where ")
	int deleteschoolInfo(SchoolInfo schoolInfo);

}