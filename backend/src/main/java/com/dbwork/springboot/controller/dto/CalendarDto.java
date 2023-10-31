package com.dbwork.springboot.controller.dto;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CalendarDto {
    private String name;

    private String start;

    private String end;

    private String details;

    private Integer drinking;

    private Integer timed;

    private Integer userid;
}
