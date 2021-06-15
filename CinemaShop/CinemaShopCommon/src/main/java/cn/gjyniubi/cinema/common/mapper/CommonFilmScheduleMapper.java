package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.entry.FilmSchedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author gujianyang
 * @Date 2021/5/20
 * @Class CommonFilmScheduleMapper
 */
@Repository
@Mapper
public interface CommonFilmScheduleMapper extends BaseMapper<FilmSchedule> {
    @Select("select * from film_schedule where id = #{id}")
    FilmSchedule selectByIdWithAllLogicStatus(@Param("id") Integer scheduleId);
}
