package cn.gjyniubi.cinema.app.core.cinema.mapper;

import cn.gjyniubi.cinema.common.entry.FilmSchedule;
import cn.gjyniubi.cinema.common.mapper.CommonFilmScheduleMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/28
 * @Class FilmScheduleMapper
 */
@Repository
@Mapper
public interface FilmScheduleMapper extends CommonFilmScheduleMapper {

    List<FilmSchedule> selectCinemaSchedules(@Param("cinemaId") Integer cinemaId, @Param("end") Date end);

}
