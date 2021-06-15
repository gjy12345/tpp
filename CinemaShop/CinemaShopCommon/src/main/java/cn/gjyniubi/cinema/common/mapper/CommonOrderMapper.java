package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.entry.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class CommonOrderMapper
 */
@Repository
@Mapper
public interface CommonOrderMapper extends BaseMapper<Order> {

    @Select("SELECT count(*) from `order`\n" +
            "LEFT JOIN film_schedule on film_schedule.id = `order`.schedule_id\n" +
            "where (`order`.status = 1 or `order`.status=2) and  DATE(`order`.create_time) = CURDATE() and film_schedule.film_id = #{filmId}")
    Long selectTodaySaleTicket(@Param("filmId") Integer filmId);

    @Select("SELECT count(*) from `order`\n" +
            "LEFT JOIN film_schedule on film_schedule.id = `order`.schedule_id\n" +
            "where (`order`.status = 1 or `order`.status=2) and  film_schedule.film_id = #{filmId}")
    Long selectSaleTicket(@Param("filmId") Integer filmId);

    @Select("SELECT sum(`order`.price) from `order`\n" +
            "LEFT JOIN film_schedule on film_schedule.id = `order`.schedule_id\n" +
            "where (`order`.status = 1 or `order`.status=2) and  film_schedule.film_id = #{filmId}")
    Long selectFilmSaleSum(@Param("filmId") Integer filmId);
}
