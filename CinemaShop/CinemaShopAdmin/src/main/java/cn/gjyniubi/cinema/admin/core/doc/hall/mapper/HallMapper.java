package cn.gjyniubi.cinema.admin.core.doc.hall.mapper;

import cn.gjyniubi.cinema.common.dto.HallDto;
import cn.gjyniubi.cinema.admin.core.doc.hall.vo.QueryHallVo;
import cn.gjyniubi.cinema.common.mapper.CommonDocHallMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/19
 * @Class HallMapper
 */
@Mapper
@Repository
public interface HallMapper extends CommonDocHallMapper {

    List<HallDto> selectHallList(QueryHallVo queryHallVo);

    Integer selectHallCount(QueryHallVo queryHallVo);

}
