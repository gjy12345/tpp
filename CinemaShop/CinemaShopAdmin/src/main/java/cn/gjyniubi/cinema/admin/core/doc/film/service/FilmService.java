package cn.gjyniubi.cinema.admin.core.doc.film.service;

import cn.gjyniubi.cinema.admin.core.doc.film.vo.CreateFilmVo;
import cn.gjyniubi.cinema.admin.core.doc.film.dto.FilmDto;
import cn.gjyniubi.cinema.admin.core.doc.film.vo.UpdateFilmVo;
import cn.gjyniubi.cinema.admin.core.doc.film.mapper.FilmDocMapper;
import cn.gjyniubi.cinema.admin.core.doc.film.vo.QueryFilmVo;
import cn.gjyniubi.cinema.common.annotations.TrimArgs;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.domain.TableData;
import cn.gjyniubi.cinema.common.dto.SimpleFilmTypeDto;
import cn.gjyniubi.cinema.common.entry.DocFilm;
import cn.gjyniubi.cinema.common.entry.FilmType;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.exception.VerifyException;
import cn.gjyniubi.cinema.common.mapper.CommonFilmTypesMapper;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.FileUtil;
import cn.gjyniubi.cinema.common.util.HttpRequestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class FilmService
 */
@Service
public class FilmService {

    @Autowired
    private FilmDocMapper filmDocMapper;

    @Autowired
    private CommonFilmTypesMapper commonFilmTypesMapper;

    public TableData<FilmDto> getList(QueryFilmVo queryFilmVo) {
        return TableData.buildData(
                setFilmTypes(filmDocMapper.selectFilmList(queryFilmVo)),
                filmDocMapper.selectFilmCount(queryFilmVo)
        );
    }

    public void deleteFilm(Integer id) {
        filmDocMapper.deleteById(id);
    }

    private List<FilmDto> setFilmTypes(List<FilmDto> list){
        for (FilmDto dto : list) {
            dto.setFilmTypes(commonFilmTypesMapper.selectFilmSimpleTypes(dto.getId()));
        }
        return list;
    }

    @TrimArgs
    @Transactional(rollbackFor = Exception.class)
    public void updateFilm(@TrimArgs UpdateFilmVo updateFilmVo) {
        DocFilm docFilm = filmDocMapper.selectById(updateFilmVo.getId());
        if(docFilm==null)
            throw new NoSuchDataException("不存在此电影档案!");
        if(filmDocMapper.selectCount(new QueryWrapper<DocFilm>()
                .eq("name",updateFilmVo.getName())
                .ne("id",updateFilmVo.getId()))>0)
            throw new VerifyException("已存在此名称的电影");
        List<SimpleFilmTypeDto> filmTypeDto = commonFilmTypesMapper.selectFilmSimpleTypes(updateFilmVo.getId());
        Set<Integer> del=new HashSet<>();
        Set<Integer> add=new HashSet<>();
        Set<Integer> uploadTypes= new HashSet<>(updateFilmVo.getType());
        Set<Integer> dbTypes= filmTypeDto.stream().map(SimpleFilmTypeDto::getId).collect(Collectors.toSet());
        Set<Integer> all=new HashSet<>(uploadTypes);
        all.addAll(dbTypes);
        for (Integer type : all) {
            if(dbTypes.contains(type)&&uploadTypes.contains(type))
                continue;
            if(dbTypes.contains(type))
                del.add(type);
            else
                add.add(type);
        }
        if(updateFilmVo.getCover()!=null&&!FileUtil.isSafeImageFile(updateFilmVo.getCover())){
            throw new VerifyException("上传图片有误!");
        }
        BeanAssignment.copySameFields(updateFilmVo,docFilm);
        docFilm.setUpdateTime(new Date());
        filmDocMapper.updateById(docFilm);
        //更新类型
        if(!del.isEmpty())
            commonFilmTypesMapper.delete(new UpdateWrapper<FilmType>()
                .eq("film_id",updateFilmVo.getId()).in("film_type_id",del));
        FilmType filmType=new FilmType();
        filmType.setFilmId(updateFilmVo.getId());
        for (Integer type : add) {
            filmType.setFilmTypeId(type);
            commonFilmTypesMapper.insert(filmType);
        }
    }

    @TrimArgs
    @Transactional(rollbackFor = Exception.class)
    public void createFilm(@TrimArgs CreateFilmVo createFilmVo) {
        if(createFilmVo.getCover()!=null&&!FileUtil.isSafeImageFile(createFilmVo.getCover())){
            throw new VerifyException("上传图片有误!");
        }
        if(filmDocMapper.selectCount(new QueryWrapper<DocFilm>()
                .eq("name",createFilmVo.getName()))>0)
            throw new VerifyException("已存在此名称的电影");
        DocFilm docFilm = BeanAssignment.copySameFields(createFilmVo, new DocFilm());
        docFilm.setUid(UUID.randomUUID().toString());
        docFilm.setCreateBy(HttpRequestUtil.getSysUserId());
        docFilm.setCreateTime(new Date());
        docFilm.setUpdateTime(docFilm.getCreateTime());
        docFilm.setLogicDel(UserContact.LOGIC_DEL_NO_DELETE);
        filmDocMapper.insert(docFilm);
        FilmType filmType=new FilmType();
        filmType.setFilmId(docFilm.getId());
        for (Integer type : createFilmVo.getType()) {
            filmType.setFilmTypeId(type);
            commonFilmTypesMapper.insert(filmType);
        }
    }
}
