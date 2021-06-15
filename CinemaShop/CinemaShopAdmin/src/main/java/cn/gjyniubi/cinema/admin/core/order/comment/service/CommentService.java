package cn.gjyniubi.cinema.admin.core.order.comment.service;

import cn.gjyniubi.cinema.admin.core.doc.film.mapper.FilmDocMapper;
import cn.gjyniubi.cinema.admin.core.order.comment.dto.CommentDto;
import cn.gjyniubi.cinema.admin.core.order.comment.mapper.CommentMapper;
import cn.gjyniubi.cinema.admin.core.order.comment.vo.CommentListQueryVo;
import cn.gjyniubi.cinema.common.domain.TableData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class CommentService
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private FilmDocMapper filmDocMapper;

    public void deleteComment(Integer id) {
        commentMapper.deleteById(id);
    }

    public TableData<CommentDto> getCommentList(CommentListQueryVo commentListQueryVo) {
        return TableData.buildData(
                commentMapper.selectCommentList(commentListQueryVo),
                commentMapper.selectCommentCount(commentListQueryVo)
        );
    }

}
