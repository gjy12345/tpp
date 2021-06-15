package cn.gjyniubi.cinema.admin.core.doc.film.mapper;

import cn.gjyniubi.cinema.admin.core.doc.film.dto.FilmDto;
import cn.gjyniubi.cinema.admin.core.doc.film.vo.QueryFilmVo;
import cn.gjyniubi.cinema.common.mapper.CommonFilmDocMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class FilmMapper
 */
@Mapper
@Repository
public interface FilmDocMapper extends CommonFilmDocMapper {

    List<FilmDto> selectFilmList(QueryFilmVo queryFilmVo);

    Integer selectFilmCount(QueryFilmVo queryFilmVo);

}
