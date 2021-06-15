package cn.gjyniubi.cinema.app.core.cinema.mapper;

import cn.gjyniubi.cinema.app.core.cinema.dto.SimpleCinemaDto;
import cn.gjyniubi.cinema.app.core.cinema.vo.CinemaListQuery;
import cn.gjyniubi.cinema.app.core.cinema.vo.ScheduleCinemaListQuery;
import cn.gjyniubi.cinema.common.domain.SimpleJoinParamQuery;
import cn.gjyniubi.cinema.common.mapper.CommonCinemaDocMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/26
 * @Class CinemaMapper
 */
@Repository
@Mapper
public interface CinemaMapper extends CommonCinemaDocMapper {

    List<SimpleCinemaDto> selectSimpleCinemaList(@Param("cq") CinemaListQuery cinemaListQuery,
                                                 @Param("jq") SimpleJoinParamQuery jq);

    List<SimpleCinemaDto> selectSimpleScheduleCinemaList(@Param("scq")ScheduleCinemaListQuery query,@Param("jq") SimpleJoinParamQuery position);
}
