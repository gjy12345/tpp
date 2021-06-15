package cn.gjyniubi.cinema.admin.core.doc.cinema.service;

import cn.gjyniubi.cinema.admin.core.doc.cinema.dto.CinemaDto;
import cn.gjyniubi.cinema.admin.core.doc.cinema.mapper.CinemaMapper;
import cn.gjyniubi.cinema.admin.core.doc.cinema.vo.CreateCinemaVo;
import cn.gjyniubi.cinema.admin.core.doc.cinema.vo.QueryCinemaVo;
import cn.gjyniubi.cinema.admin.core.doc.cinema.vo.UpdateCinemaVo;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.domain.SimpleJoinParamQuery;
import cn.gjyniubi.cinema.common.domain.TableData;
import cn.gjyniubi.cinema.common.entry.DocCinema;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.exception.VerifyException;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.HttpRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @Author gujianyang
 * @Date 2021/5/16
 * @Class CinemaService
 */
@Service
public class CinemaService {

    @Autowired
    private CinemaMapper cinemaMapper;

    public TableData<CinemaDto> getCinemaList(QueryCinemaVo queryCinemaVo) {
        SimpleJoinParamQuery query = new SimpleJoinParamQuery(UserContact.POSITION_SEPARATOR,
                args -> StringUtils.isNotBlank((String) args),
                queryCinemaVo.getProvince(),
                queryCinemaVo.getCity(),
                queryCinemaVo.getCountry());
        return TableData.buildData(
                cinemaMapper.selectCinemaList(queryCinemaVo,query),
                cinemaMapper.selectCinemaCount(queryCinemaVo,query)
        );
    }

    public void deleteCinema(Integer id) {
        cinemaMapper.deleteById(id);
    }

    public void updateCinema(UpdateCinemaVo updateCinemaVo) {
        if(updateCinemaVo.getPosition().split("-").length<2)
            throw new VerifyException("位置信息有误!");
        DocCinema cinema = cinemaMapper.selectById(updateCinemaVo.getId());
        if(cinema==null)
            throw new NoSuchDataException("没有此影院信息");
        BeanAssignment.copySameFields(updateCinemaVo,cinema);
        cinema.setUpdateTime(new Date());
        cinemaMapper.updateById(cinema);
    }

    //存在一些城市，只有城市，没有县级分类
    public void createCinema(CreateCinemaVo createCinemaVo) {
        if(createCinemaVo.getPosition().split("-").length<2)
            throw new VerifyException("位置信息有误!");
        DocCinema cinema = BeanAssignment.copySameFields(createCinemaVo, new DocCinema());
        cinema.setCreateBy(HttpRequestUtil.getSysUserId());
        cinema.setCreateTime(new Date());
        cinema.setUpdateTime(cinema.getCreateTime());
        cinema.setUid(UUID.randomUUID().toString());
        cinema.setLogicDel(UserContact.LOGIC_DEL_NO_DELETE);
        cinemaMapper.insert(cinema);
    }

}
