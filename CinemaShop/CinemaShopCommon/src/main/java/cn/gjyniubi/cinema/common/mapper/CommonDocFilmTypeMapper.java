package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.entry.DocFilmType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class CommonDocFilmTypeMapper
 */
@Mapper
@Repository
public interface CommonDocFilmTypeMapper extends BaseMapper<DocFilmType> {

}
