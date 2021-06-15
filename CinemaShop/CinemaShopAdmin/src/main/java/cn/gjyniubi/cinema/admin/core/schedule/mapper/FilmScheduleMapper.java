package cn.gjyniubi.cinema.admin.core.schedule.mapper;

import cn.gjyniubi.cinema.admin.core.schedule.dto.FilmScheduleDto;
import cn.gjyniubi.cinema.admin.core.schedule.vo.QueryScheduleFilmVo;
import cn.gjyniubi.cinema.common.mapper.CommonFilmScheduleMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/20
 * @Class FilmScheduleMapper
 */
@Repository
@Mapper
public interface FilmScheduleMapper extends CommonFilmScheduleMapper {

    List<FilmScheduleDto> selectFilmScheduleList(QueryScheduleFilmVo queryScheduleFilmVo);

    Integer selectFilmScheduleCount(QueryScheduleFilmVo queryScheduleFilmVo);

}
