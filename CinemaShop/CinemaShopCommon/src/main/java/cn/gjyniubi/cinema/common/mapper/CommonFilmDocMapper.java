package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.entry.DocFilm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class CommonFilmMapper
 */
@Repository
@Mapper
public interface CommonFilmDocMapper extends BaseMapper<DocFilm> {
    @Select("select * from doc_film where id =#{id}")
    DocFilm selectByIdWithAllLogicStatus(@Param("id") Integer filmId);

    @Update("UPDATE doc_film set score = (SELECT round(avg(score),1) from order_comment where film_id = #{id}) " +
            "where id = #{id}")
    int updateScore(@Param("id") Integer id);
}
