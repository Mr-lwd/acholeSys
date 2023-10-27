package com.dbwork.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbwork.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

//@Mapper
public interface UserpMapper extends BaseMapper<User> {

//    @Select("SELECT * FROsM sys_user")
//    List<User> findAll();
//
//    @Insert("INSERT into sys_user(name, email, password, phone) VALUES (#{name}, #{email} ,#{password},#{phone}); ")
//    int insert(User user);
//
//    int update(User user);
//
//    @Delete("delete from sys_user where id = #{id}")
//    Integer deleteById(@Param("id") Integer id);
//
//    @Select("select * from sys_user limit #{pageNum}, #{pageSize}")
//    List<User> selectPage(Integer pageNum, Integer pageSize);
//
//    @Select("select count(*) from sys_user")
//    Integer selectTotal();
}
