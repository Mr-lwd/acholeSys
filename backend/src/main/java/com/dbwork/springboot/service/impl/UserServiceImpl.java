package com.dbwork.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dbwork.springboot.common.Constants;
import com.dbwork.springboot.controller.dto.UserDto;
import com.dbwork.springboot.entity.User;
import com.dbwork.springboot.exception.ServiceException;
import com.dbwork.springboot.mapper.UserMapper;
import com.dbwork.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbwork.springboot.utils.TokenUtils;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import static org.springframework.boot.Banner.Mode.LOG;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Mrlwd
 * @since 2023-10-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public UserDto login(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", userDto.getEmail());
        queryWrapper.eq("password", userDto.getPassword());
        queryWrapper.eq( "role", userDto.getRole());
        User one;
        try {
            one = getOne(queryWrapper);
        }catch(Exception e){
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }

        if(one!= null){
            BeanUtil.copyProperties(one, userDto, true);
            String token = TokenUtils.genToken(one.getId().toString(),one.getPassword());
            userDto.setToken(token);
            return userDto;
        }else{
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public User register(User user){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        User one;
        try {
            one = getOne(queryWrapper);
        }catch(Exception e){
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if(one == null){
            save(user);
        }else{
            throw new ServiceException(Constants.CODE_600, "用户已注册");
        }
        return user;
    }

}
