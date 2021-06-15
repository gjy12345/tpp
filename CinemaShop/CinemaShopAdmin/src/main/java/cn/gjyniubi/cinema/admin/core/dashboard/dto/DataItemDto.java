package cn.gjyniubi.cinema.admin.core.dashboard.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/6/2
 * @Class DataItemDto
 */
@Data
public class DataItemDto {
    private Date day;
    private Integer count;
}
