package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.dto.SimpleHallDto;
import cn.gjyniubi.cinema.common.entry.DocHall;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/19
 * @Class CommonDocHallMapper
 */
@Repository
@Mapper
public interface CommonDocHallMapper extends BaseMapper<DocHall> {

    @Select("select id,name from doc_hall where cinema_id = #{cinemaId} and logic_del = 0")
    List<SimpleHallDto> selectSimpleHallList(@Param("cinemaId") Integer cinemaId);

    @Select("select * from doc_hall where id = #{id}")
    DocHall selectByIdWithAllLogicStatus(@Param("id") Integer hallId);
}
