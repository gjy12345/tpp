package cn.gjyniubi.cinema.app.core.comment.mapper;

import cn.gjyniubi.cinema.app.core.comment.dto.SimpleCommentDto;
import cn.gjyniubi.cinema.common.mapper.CommonOrderCommentMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class CommentMapper
 */
@Repository
@Mapper
public interface CommentMapper extends CommonOrderCommentMapper {

    List<SimpleCommentDto> selectFilmRecentComment(@Param("filmId") Integer id);
}
