package cn.gjyniubi.cinema.app.core.home.mapper;

import cn.gjyniubi.cinema.app.core.home.dto.SimpleFilmDto;
import cn.gjyniubi.cinema.common.domain.BaseListQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/25
 * @Class HomeMapper
 */
@Repository
@Mapper
public interface HomeMapper {

    List<SimpleFilmDto> selectNowShowingFilm(BaseListQuery baseListQuery);

    List<SimpleFilmDto> selectWillShowFilm(BaseListQuery baseListQuery);
}
