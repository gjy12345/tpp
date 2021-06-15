package cn.gjyniubi.cinema.admin.core.doc.hall.service;

import cn.gjyniubi.cinema.common.domain.HallSite;
import cn.gjyniubi.cinema.admin.core.doc.hall.mapper.HallMapper;
import cn.gjyniubi.cinema.admin.core.doc.hall.vo.CreateHallVo;
import cn.gjyniubi.cinema.admin.core.doc.hall.vo.HallSiteVo;
import cn.gjyniubi.cinema.admin.core.doc.hall.vo.QueryHallVo;
import cn.gjyniubi.cinema.admin.core.doc.hall.vo.UpdateHallVo;
import cn.gjyniubi.cinema.common.annotations.TrimArgs;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.domain.TableData;
import cn.gjyniubi.cinema.common.dto.HallDto;
import cn.gjyniubi.cinema.common.dto.SimpleHallDto;
import cn.gjyniubi.cinema.common.entry.DocHall;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.exception.VerifyException;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.HttpRequestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author gujianyang
 * @Date 2021/5/19
 * @Class HallService
 */
@Service
public class HallService {

    @Autowired
    private HallMapper hallMapper;

    @Autowired
    private Gson gson;

    public TableData<HallDto> getHallList(QueryHallVo queryHallVo) {
        return TableData.buildData(
                hallMapper.selectHallList(queryHallVo),
                hallMapper.selectHallCount(queryHallVo)
        );
    }

    @TrimArgs
    public void updateHall(@TrimArgs UpdateHallVo updateHallVo) {
        DocHall docHall = hallMapper.selectById(updateHallVo.getId());
        if(docHall==null)
            throw new NoSuchDataException("此影厅不存在!");
        if(hallMapper.selectCount(new QueryWrapper<DocHall>()
                .eq("name",updateHallVo.getName())
                .eq("cinema_id",docHall.getCinemaId())
                .ne("id",updateHallVo.getId()))>0)
            throw new VerifyException("已存在名为:"+updateHallVo.getName()+" 的影厅!");
        BeanAssignment.copySameFields(updateHallVo,docHall);
        hallMapper.updateById(docHall);
    }

    @TrimArgs
    public void createHall(@TrimArgs CreateHallVo createHallVo) {
        if(hallMapper.selectCount(new QueryWrapper<DocHall>()
                .eq("cinema_id",createHallVo.getCinemaId())
                .eq("name",createHallVo.getName()))>0)
            throw new VerifyException("已存在名为:"+createHallVo.getName()+" 的影厅!");
        DocHall docHall=BeanAssignment.copySameFields(createHallVo,new DocHall());
        docHall.setCreateTime(new Date());
        docHall.setUpdateTime(docHall.getCreateTime());
        docHall.setCreateBy(HttpRequestUtil.getSysUserId());
        docHall.setLogicDel(UserContact.LOGIC_DEL_NO_DELETE);
        docHall.setUid(UUID.randomUUID().toString());
        hallMapper.insert(docHall);
    }

    public void deleteHall(Integer id) {
        // TODO: 2021/5/19 检测影厅是否已被排片，且放映没有结束
        if(hallMapper.deleteById(id)==0)
            throw new NoSuchDataException("没有此影厅");
    }

    public void updateSite(HallSiteVo hallSiteVo) {
        for (HallSite[] site : hallSiteVo.getSites()) {
            if(site.length==0)
                throw new VerifyException("每行不能为空!");
            for (HallSite hallSite : site) {
                if(hallSite.getType()==null||hallSite.getType()<-1||hallSite.getType()>3)
                    throw new VerifyException("错误的座位类型");
            }
        }
        DocHall docHall = hallMapper.selectById(hallSiteVo.getHallId());
        if(docHall==null)
            throw new VerifyException("此观影厅不存在!");
        docHall.setSites(hallSiteVo.getSites().length==0?null:gson.toJson(hallSiteVo.getSites()));
        hallMapper.updateById(docHall);
    }

    public HallSite[][] getSite(Integer hallId) {
        DocHall docHall = hallMapper.selectById(hallId);
        if(docHall==null)
            throw new VerifyException("此观影厅不存在!");
        return docHall.getSites()==null?null:gson.fromJson(docHall.getSites(),
                new TypeToken<HallSite[][]>(){}.getType());
    }

    public List<SimpleHallDto> getAllHallWithSimple(Integer cinemaId) {
        return hallMapper.selectSimpleHallList(cinemaId);
    }
}
