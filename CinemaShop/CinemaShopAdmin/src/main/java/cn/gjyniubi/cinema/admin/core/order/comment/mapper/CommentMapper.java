package cn.gjyniubi.cinema.admin.core.order.comment.mapper;

import cn.gjyniubi.cinema.admin.core.order.comment.dto.CommentDto;
import cn.gjyniubi.cinema.admin.core.order.comment.vo.CommentListQueryVo;
import cn.gjyniubi.cinema.common.mapper.CommonOrderCommentMapper;
import org.apache.ibatis.annotations.Mapper;
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

    List<CommentDto> selectCommentList(CommentListQueryVo commentListQueryVo);

    Integer selectCommentCount(CommentListQueryVo commentListQueryVo);

}
