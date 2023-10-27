package com.dbwork.springboot.service;

import com.dbwork.springboot.controller.dto.UserDto;
import com.dbwork.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mrlwd
 * @since 2023-10-25
 */
public interface IUserService extends IService<User> {

    UserDto login(UserDto userDto);

    User register(User user);
}
