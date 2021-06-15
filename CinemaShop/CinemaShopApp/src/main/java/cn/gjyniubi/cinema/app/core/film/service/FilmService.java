package cn.gjyniubi.cinema.app.core.film.service;

import cn.gjyniubi.cinema.app.core.film.dto.FilmInfoWithSaleDataDto;
import cn.gjyniubi.cinema.common.entry.DocFilm;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.mapper.CommonFilmDocMapper;
import cn.gjyniubi.cinema.common.mapper.CommonFilmTypesMapper;
import cn.gjyniubi.cinema.common.mapper.CommonOrderMapper;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author gujianyang
 * @Date 2021/5/27
 * @Class FilmService
 */
@Service
public class FilmService {

    @Autowired
    private CommonFilmDocMapper commonFilmDocMapper;

    @Autowired
    private CommonFilmTypesMapper commonFilmTypesMapper;

    @Autowired
    private CommonOrderMapper commonOrderMapper;

    public FilmInfoWithSaleDataDto getFilmInfo(String uid) {
        DocFilm docFilm = commonFilmDocMapper.selectOne(new QueryWrapper<DocFilm>().eq("uid", uid));
        if(docFilm==null){
            throw new NoSuchDataException("不存在此电影!");
        }
        FilmInfoWithSaleDataDto dto = BeanAssignment.copySameFields(docFilm,new FilmInfoWithSaleDataDto());
        dto.setTypes(commonFilmTypesMapper.selectFilmSimpleTypes(docFilm.getId()));
        dto.setToday(commonOrderMapper.selectTodaySaleTicket(docFilm.getId()));
        dto.setAllCount(commonOrderMapper.selectSaleTicket(docFilm.getId()));
        dto.setAllSum(commonOrderMapper.selectFilmSaleSum(docFilm.getId()));
        return dto;
    }
}
