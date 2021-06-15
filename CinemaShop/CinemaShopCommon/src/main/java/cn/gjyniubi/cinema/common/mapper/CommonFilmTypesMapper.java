package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.dto.SimpleFilmTypeDto;
import cn.gjyniubi.cinema.common.entry.FilmType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class CommonFilmTypesMapper
 */
@Repository
@Mapper
public interface CommonFilmTypesMapper extends BaseMapper<FilmType> {

    @Select("select doc_film_type.`name`,doc_film_type.id from film_types\n" +
            "left join doc_film_type on film_types.film_type_id=doc_film_type.id\n" +
            "where film_types.film_id = #{id}\n" +
            "order by doc_film_type.id desc")
    List<SimpleFilmTypeDto> selectFilmSimpleTypes(@Param("id") Integer id);

    @Select("select doc_film_type.`name`,doc_film_type.id from doc_film_type" +
            " where logic_del !=1 and `status` = 1 " +
            " order by id desc")
    List<SimpleFilmTypeDto> selectAllFilmSimpleTypes();
}
