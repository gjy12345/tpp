package cn.gjyniubi.cinema.app.core.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/6/1
 * @Class ListOrderDto
 */
@Data
public class ListOrderDto {
    @JsonIgnore
    private Integer id;
    private String hallName;
    private String cinemaName;
    private Integer status;
    private Date createTime;
    private Date payTime;
    private String orderNum;
    private String filmName;
    private Integer commonStatus;
    private Date begin;
    private Date end;
}
