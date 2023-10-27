package com.dbwork.springboot.controller.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

//前端登录
@Data
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private String token;
}
