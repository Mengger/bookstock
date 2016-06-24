package com.chillax.dao;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.chillax.entry.EmployeeInfo;

@Repository("employee_info_dao")
public interface EmployeeDao {

	@Select("<script>select * from employee_info where 1=1 "
			+ "<if test=\"id !=null \">and id = #{id} </if>"
			+ "<if test=\"name !=null \">and name = #{name} </if>"
			+ "<if test=\"idCard !=null \">and id_card = #{idCard} </if>"
			+ "<if test=\"birthPlace !=null \">and birth_place = #{birthPlace} </if>"
			+ "<if test=\"managerId !=null \">and manager_id = #{managerId} </if>"
			+ "<if test=\"areaId !=null \">and area_id = #{areaId} </if>"
			+ "<if test=\"schoolId !=null \">and school_id = #{schoolId} </if>"
			+ "<if test=\"photoPath !=null \">and photo_path = #{photoPath} </if>"
			+ "<if test=\"idCardPath !=null \">and id_Card_Path = #{idCardPath} </if>"
			+ "<if test=\"info !=null \">and info = #{info} </if>"
			+ "</script>")
	@Results({
		@Result(property="createTime",column="create_time"),
		@Result(property="modifyTime",column="modify_time"),
		@Result(property="id",column="id"),
		@Result(property="name",column="name"),
		@Result(property="idCard",column="id_card"),
		@Result(property="birthPlace",column="birth_place"),
		@Result(property="managerId",column="manager_id"),
		@Result(property="areaId",column="area_id"),
		@Result(property="schoolId",column="school_id"),
		@Result(property="photoPath",column="photo_path"),
		@Result(property="idCardPath",column="id_Card_Path"),
		@Result(property="info",column="info"),
	})
	List<EmployeeInfo> queryEmployInfoList(EmployeeInfo employee);
	
	
	
	@Update("<script>update employee_info "
			+"<set>"
				+ "<if test=\"name !=null \"> name = #{name} ,</if>"
				+ "<if test=\"pwd !=null \"> pwd = #{pwd} ,</if>"
				+ "<if test=\"idCard !=null \"> id_card = #{idCard} ,</if>"
				+ "<if test=\"birthPlace !=null \"> birth_place = #{birthPlace} ,</if>"
				+ "<if test=\"managerId !=null \"> manager_id = #{managerId} ,</if>"
				+ "<if test=\"areaId !=null \"> area_id = #{areaId} ,</if>"
				+ "<if test=\"schoolId !=null \"> school_id = #{schoolId} ,</if>"
				+ "<if test=\"photoPath !=null \"> photo_path = #{photoPath} ,</if>"
				+ "<if test=\"idCardPath !=null \"> id_Card_Path = #{idCardPath} ,</if>"
				+ "<if test=\"info !=null \"> info = #{info} ,</if>"
			+"</set>"
			+"where id = #{id}"
			+ "</script>")
	int updateEmployInfo(EmployeeInfo EmployeeInfo);
}
