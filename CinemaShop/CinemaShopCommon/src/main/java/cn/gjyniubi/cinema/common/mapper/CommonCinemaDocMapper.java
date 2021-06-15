package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.entry.DocCinema;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author gujianyang
 * @Date 2021/5/16
 * @Class CommonCinemaDocMapper
 */
@Repository
@Mapper
public interface CommonCinemaDocMapper extends BaseMapper<DocCinema> {
    @Select("select * from doc_cinema where id =#{id}")
    DocCinema selectByIdWithAllLogicStatus(@Param("id") Integer cinemaId);
}
