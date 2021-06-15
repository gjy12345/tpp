package cn.gjyniubi.cinema.admin.core.doc.cinema.mapper;

import cn.gjyniubi.cinema.admin.core.doc.cinema.dto.CinemaDto;
import cn.gjyniubi.cinema.admin.core.doc.cinema.vo.QueryCinemaVo;
import cn.gjyniubi.cinema.common.domain.SimpleJoinParamQuery;
import cn.gjyniubi.cinema.common.mapper.CommonCinemaDocMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class CinemaMapper
 */
@Repository
@Mapper
public interface CinemaMapper extends CommonCinemaDocMapper {

    List<CinemaDto> selectCinemaList(@Param("qc") QueryCinemaVo queryCinemaVo,@Param("lq") SimpleJoinParamQuery query);

    Integer selectCinemaCount(@Param("qc") QueryCinemaVo queryCinemaVo,@Param("lq") SimpleJoinParamQuery query);

}
