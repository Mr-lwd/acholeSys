package com.dbwork.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author Mrlwd
 * @since 2023-10-30
 */
@Getter
@Setter
@TableName("sys_calendar")
public class Calendar implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;
    @DateTimeFormat(pattern = "yy/MM/dd HH:mm:ss")
    private LocalDateTime start;
    @DateTimeFormat(pattern = "yy/MM/dd HH:mm:ss")
    private LocalDateTime end;

    private String details;

    private Integer drinking;

    private Integer timed;

    private Integer userid;
}
