package com.dbwork.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbwork.springboot.common.Result;
import com.dbwork.springboot.controller.dto.CalendarDto;
import com.dbwork.springboot.entity.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.dbwork.springboot.service.ICalendarService;
import com.dbwork.springboot.entity.Calendar;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Mrlwd
 * @since 2023-10-30
 */
@RestController
@RequestMapping("/calendar")
public class CalendarController {
    @Resource
    private ICalendarService calendarService;

    //新增或更新
    @PostMapping("/save")
    public boolean save(@RequestBody CalendarDto calendarDto) {
        Calendar one = new Calendar();
//        if(calendarDto.getTimed().equals(1)){
            String startStr = calendarDto.getStart();
            LocalDateTime startTime = LocalDateTime.parse(startStr, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
            String endStr = calendarDto.getEnd();
            LocalDateTime endTime = LocalDateTime.parse(endStr, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
            one.setStart(startTime);
            one.setEnd(endTime);
//        }
        one.setName(calendarDto.getName());
        one.setDetails(calendarDto.getDetails());
        one.setDrinking(calendarDto.getDrinking());
        one.setTimed(calendarDto.getTimed());
        one.setUserid(calendarDto.getUserid());
        return calendarService.saveOrUpdate(one);
    }
    //保存detail
    @GetMapping("/savedetails")
    public boolean saveDetails(@RequestParam Integer id,
                               @RequestParam String details) {
        Calendar one = calendarService.getById(id);
        one.setDetails(details);
        return calendarService.saveOrUpdate(one);
    }

    //删除
    @GetMapping("/delete/{id}")
    public boolean delete(@PathVariable Integer id) {
        return calendarService.removeById(id);
    }

    //查询全部
    @GetMapping
    public List<Calendar> findAll() {
        return calendarService.list();
    }

    //根据ID查询
    @GetMapping("/{id}")
    public Calendar findOne(@PathVariable Integer id) {
        return calendarService.getById(id);
    }

    //分页查询
    @GetMapping("/page/{userid}")
    public Result findPage(
            @PathVariable Integer userid,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String searchName
    ) {
        QueryWrapper<Calendar> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("userid", userid);
        if (!"".equals(searchName)) {
            queryWrapper.like("name", searchName);
        }
        return Result.success(calendarService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/all/{userid}")
    public Result getAll(
            @PathVariable Integer userid
    ) {
        QueryWrapper<Calendar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid);
        return Result.success(calendarService.list(queryWrapper));
    }
}

