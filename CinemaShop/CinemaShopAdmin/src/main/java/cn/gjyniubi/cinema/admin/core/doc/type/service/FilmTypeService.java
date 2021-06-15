package cn.gjyniubi.cinema.admin.core.doc.type.service;

import cn.gjyniubi.cinema.admin.core.doc.type.dto.FilmTypeDto;
import cn.gjyniubi.cinema.admin.core.doc.type.mapper.DocFilmTypeMapper;
import cn.gjyniubi.cinema.admin.core.doc.type.vo.CreateTypeVo;
import cn.gjyniubi.cinema.admin.core.doc.type.vo.QueryTypeVo;
import cn.gjyniubi.cinema.admin.core.doc.type.vo.UpdateTypeVo;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.domain.TableData;
import cn.gjyniubi.cinema.common.dto.SimpleFilmTypeDto;
import cn.gjyniubi.cinema.common.entry.DocFilmType;
import cn.gjyniubi.cinema.common.entry.FilmType;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.exception.VerifyException;
import cn.gjyniubi.cinema.common.mapper.CommonFilmTypesMapper;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.HttpRequestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class FilmTypeService
 */
@Service
public class FilmTypeService {

    @Autowired
    private DocFilmTypeMapper filmTypeMapper;

    @Autowired
    private CommonFilmTypesMapper commonFilmTypesMapper;

    public TableData<FilmTypeDto> getList(QueryTypeVo queryTypeVo) {
        return TableData.buildData(
                setFilmTypeFilmCount(filmTypeMapper.selectTypeList(queryTypeVo)),
                filmTypeMapper.selectTypeListCount(queryTypeVo)
        );
    }

    private List<FilmTypeDto> setFilmTypeFilmCount(List<FilmTypeDto> list){
        for (FilmTypeDto typeDto : list) {
            typeDto.setCount(commonFilmTypesMapper.
                    selectCount(new QueryWrapper<FilmType>().eq("film_type_id",typeDto.getId())));
        }
        return list;
    }

    public void deleteType(Integer id) {
        filmTypeMapper.deleteById(id);
    }

    public void updateType(UpdateTypeVo updateTypeVo) {
        DocFilmType type = filmTypeMapper.selectById(updateTypeVo.getId());
        if(type==null)
            throw new NoSuchDataException("没有此条电影类别数据!");
        updateTypeVo.setName(updateTypeVo.getName().trim());
        if(!updateTypeVo.getName().equalsIgnoreCase(type.getName())
        &&filmTypeMapper.selectCount(new QueryWrapper<DocFilmType>().eq("name",
                updateTypeVo.getName()))>0){
            throw new VerifyException("已存在同名类型!");
        }
        BeanAssignment.copySameFields(updateTypeVo,type);
        type.setUpdateTime(new Date());
        filmTypeMapper.updateById(type);
    }

    public void createType(CreateTypeVo createTypeVo) {
        createTypeVo.setName(createTypeVo.getName().trim());
        if(filmTypeMapper.selectCount(new QueryWrapper<DocFilmType>().eq("name",
                createTypeVo.getName()))>0)
            throw new VerifyException("已存在同名类型!");
        DocFilmType filmType = BeanAssignment.copySameFields(createTypeVo, new DocFilmType());
        filmType.setCreateBy(HttpRequestUtil.getSysUserId());
        filmType.setLogicDel(UserContact.LOGIC_DEL_NO_DELETE);
        filmType.setCreateTime(new Date());
        filmType.setUpdateTime(filmType.getCreateTime());
        filmTypeMapper.insert(filmType);
    }

    public List<SimpleFilmTypeDto> getAllTypes() {
        return commonFilmTypesMapper.selectAllFilmSimpleTypes();
    }
}
