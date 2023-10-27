package com.dbwork.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbwork.springboot.common.Constants;
import com.dbwork.springboot.common.Result;
import com.dbwork.springboot.controller.dto.UserDto;
import com.dbwork.springboot.utils.TokenUtils;
import jdk.nashorn.internal.parser.Token;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.dbwork.springboot.service.IUserService;
import com.dbwork.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Mrlwd
 * @since 2023-10-25
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto) {
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(email) || StrUtil.isBlank(password)) {
//            TokenUtils.genToken(user.getId(), user.getPassword());
            return Result.error(Constants.CODE_400, "参数缺少");
        }
        UserDto dto = userService.login(userDto);
        return Result.success(dto);
    }


    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (StrUtil.isBlank(user.getName()) || StrUtil.isBlank(user.getEmail())
                || StrUtil.isBlank(user.getPhone()) || StrUtil.isBlank(user.getPassword())
                || StrUtil.isBlank(user.getRole())){
            return Result.error(Constants.CODE_400, "参数缺少");
        }
        return Result.success(userService.register(user));
    }

    @PostMapping("/updatePasswd")
    public Result updatePasswd(@RequestParam Integer id,
                               @RequestParam String oldPasswd,
                               @RequestParam String newPasswd) {
        User one = userService.getById(id);
        if(!one.getPassword().equals(oldPasswd)){
            return Result.error(Constants.CODE_500, "旧密码错误");
        }else{
            one.setPassword(newPasswd);
            return Result.success(userService.saveOrUpdate(one));
        }
    }


    //新增或更新
    @PostMapping
    public Result save(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user));
    }


    //删除
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    //查询全部
    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    //根据ID查询
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    //分页查询
    @GetMapping("/page")
    public Result findPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String searchName
    ) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(searchName)) {
            queryWrapper.like("name", searchName);
        }
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

