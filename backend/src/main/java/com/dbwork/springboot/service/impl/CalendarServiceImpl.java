package com.dbwork.springboot.service.impl;

import com.dbwork.springboot.entity.Calendar;
import com.dbwork.springboot.mapper.CalendarMapper;
import com.dbwork.springboot.service.ICalendarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Mrlwd
 * @since 2023-10-30
 */
@Service
public class CalendarServiceImpl extends ServiceImpl<CalendarMapper, Calendar> implements ICalendarService {

}
