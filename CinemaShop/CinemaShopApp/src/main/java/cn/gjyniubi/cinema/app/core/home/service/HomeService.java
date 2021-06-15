package cn.gjyniubi.cinema.app.core.home.service;

import cn.gjyniubi.cinema.app.core.home.dto.SimpleFilmDto;
import cn.gjyniubi.cinema.app.core.home.mapper.HomeMapper;
import cn.gjyniubi.cinema.common.domain.BaseListQuery;
import cn.gjyniubi.cinema.common.domain.ListData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author gujianyang
 * @Date 2021/5/25
 * @Class HomeService
 */
@Service
public class HomeService {

    @Autowired
    private HomeMapper homeMapper;

    public ListData<SimpleFilmDto> getNowShowingFilm(BaseListQuery baseListQuery) {
        return ListData.buildListData(homeMapper.selectNowShowingFilm(baseListQuery));
    }

    public ListData<SimpleFilmDto> getWillShowFilm(BaseListQuery baseListQuery) {
        return ListData.buildListData(homeMapper.selectWillShowFilm(baseListQuery));
    }
}
