package cn.gjyniubi.cinema.app.core.order.dto;

import cn.gjyniubi.cinema.app.core.cinema.dto.CinemaDto;
import cn.gjyniubi.cinema.app.core.film.dto.FilmInfoDto;
import cn.gjyniubi.cinema.common.entry.Order;
import cn.gjyniubi.cinema.common.entry.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class OrderDto
 */
@Builder
@Data
public class OrderDto {
    private Order order;
    private List<OrderItem> items;
    private FilmInfoDto filmInfoDto;
    private CinemaDto cinemaDto;
    private Date begin;
    private Date end;
    private String hallName;
}
