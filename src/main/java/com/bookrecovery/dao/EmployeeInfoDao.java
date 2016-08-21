package com.bookrecovery.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.bookrecovery.entry.EmployeeInfo;



@Repository("employee_info_dao")

public interface EmployeeInfoDao {

	@Select("<script>select * from employee_info where 1=1 "
		+"<if test=\"id !=null \">and id = #{id} </if>"
		+"<if test=\"createTime !=null \">and create_time = #{createTime} </if>"
		+"<if test=\"modifyTime !=null \">and modify_time = #{modifyTime} </if>"
		+"<if test=\"name !=null \">and name = #{name} </if>"
		+"<if test=\"idCard !=null \">and id_card = #{idCard} </if>"
		+"<if test=\"birthPlace !=null \">and birth_place = #{birthPlace} </if>"
		+"<if test=\"pwd !=null \">and pwd = #{pwd} </if>"
		+"<if test=\"qq !=null \">and qq = #{qq} </if>"
		+"<if test=\"eMail !=null \">and e_mail = #{eMail} </if>"
		+"<if test=\"telephone !=null \">and telephone = #{telephone} </if>"
		+"<if test=\"isReal !=null \">and is_real = #{isReal} </if>"
		+"<if test=\"managerId !=null \">and manager_id = #{managerId} </if>"
		+"<if test=\"areaId !=null \">and area_id = #{areaId} </if>"
		+"<if test=\"schoolId !=null \">and school_id = #{schoolId} </if>"
		+"<if test=\"photoPath !=null \">and photo_path = #{photoPath} </if>"
		+"<if test=\"idCardPath !=null \">and id_card_path = #{idCardPath} </if>"
		+"<if test=\"info !=null \">and info = #{info} </if>"
	+"</script>")

	@Results({
		@Result(property="createTime",column="create_time"),
		@Result(property="modifyTime",column="modify_time"),
		@Result(property="id",column="id"),
		@Result(property="name",column="name"),
		@Result(property="idCard",column="id_card"),
		@Result(property="birthPlace",column="birth_place"),
		@Result(property="pwd",column="pwd"),
		@Result(property="qq",column="qq"),
		@Result(property="eMail",column="e_mail"),
		@Result(property="telephone",column="telephone"),
		@Result(property="isReal",column="is_real"),
		@Result(property="managerId",column="manager_id"),
		@Result(property="areaId",column="area_id"),
		@Result(property="schoolId",column="school_id"),
		@Result(property="photoPath",column="photo_path"),
		@Result(property="idCardPath",column="id_card_path"),
		@Result(property="info",column="info")
	})

	List<EmployeeInfo> queryEmployeeInfoList(EmployeeInfo employeeInfo);

	@Update("<script>update employee_info "
		+"<set>"
			+ "<if test=\"createTime !=null \"> create_time = #{createTime} ,</if>"
			+ "<if test=\"modifyTime !=null \"> modify_time = #{modifyTime} ,</if>"
			+ "<if test=\"name !=null \"> name = #{name} ,</if>"
			+ "<if test=\"idCard !=null \"> id_card = #{idCard} ,</if>"
			+ "<if test=\"birthPlace !=null \"> birth_place = #{birthPlace} ,</if>"
			+ "<if test=\"pwd !=null \"> pwd = #{pwd} ,</if>"
			+ "<if test=\"qq !=null \"> qq = #{qq} ,</if>"
			+ "<if test=\"eMail !=null \"> e_mail = #{eMail} ,</if>"
			+ "<if test=\"telephone !=null \"> telephone = #{telephone} ,</if>"
			+ "<if test=\"isReal !=null \"> is_real = #{isReal} ,</if>"
			+ "<if test=\"managerId !=null \"> manager_id = #{managerId} ,</if>"
			+ "<if test=\"areaId !=null \"> area_id = #{areaId} ,</if>"
			+ "<if test=\"schoolId !=null \"> school_id = #{schoolId} ,</if>"
			+ "<if test=\"photoPath !=null \"> photo_path = #{photoPath} ,</if>"
			+ "<if test=\"idCardPath !=null \"> id_card_path = #{idCardPath} ,</if>"
			+ "<if test=\"info !=null \"> info = #{info} ,</if>"
		+"</set>"
		+"where 1=1 "
		+ "<if test=\"id !=null \"> and id = #{id} </if>"
	+ "</script>")

	int updateEmployeeInfo(EmployeeInfo employeeInfo);

	@Insert("insert into employee_info (create_time , modify_time , id , name , id_card , birth_place , pwd , qq , e_mail , telephone , is_real , manager_id , area_id , school_id , photo_path , id_card_path , info) "
				+"values( #{createTime} , #{modifyTime} , #{id} , #{name} , #{idCard} , #{birthPlace} , #{pwd} , #{qq} , #{eMail} , #{telephone} , #{isReal} , #{managerId} , #{areaId} , #{schoolId} , #{photoPath} , #{idCardPath} , #{info} )")
	int saveEmployeeInfo (EmployeeInfo employeeInfo);

	@Delete("delete from employee_info where id = #{id}")
	int deleteemployeeInfo(EmployeeInfo employeeInfo);

}