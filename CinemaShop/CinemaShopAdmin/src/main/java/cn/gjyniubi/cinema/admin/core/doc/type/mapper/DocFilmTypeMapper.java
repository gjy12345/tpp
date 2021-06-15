package cn.gjyniubi.cinema.admin.core.doc.type.mapper;

import cn.gjyniubi.cinema.admin.core.doc.type.dto.FilmTypeDto;
import cn.gjyniubi.cinema.admin.core.doc.type.vo.QueryTypeVo;
import cn.gjyniubi.cinema.common.mapper.CommonDocFilmTypeMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class FilmTypeMapper
 */
@Mapper
@Repository
public interface DocFilmTypeMapper extends CommonDocFilmTypeMapper {

    List<FilmTypeDto> selectTypeList(QueryTypeVo queryTypeVo);

    Integer selectTypeListCount(QueryTypeVo queryTypeVo);

}
