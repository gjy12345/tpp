package cn.gjyniubi.cinema.admin.core.order.comment.controller;

import cn.gjyniubi.cinema.admin.core.order.comment.dto.CommentDto;
import cn.gjyniubi.cinema.admin.core.order.comment.service.CommentService;
import cn.gjyniubi.cinema.admin.core.order.comment.vo.CommentListQueryVo;
import cn.gjyniubi.cinema.common.domain.TableData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class CommentController
 */
@Api(tags = "订单评论")
@Validated
@CrossOrigin
@RestController
@RequestMapping("/order/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    public TableData<CommentDto> getCommentList(@Validated CommentListQueryVo commentListQueryVo){
        return commentService.getCommentList(commentListQueryVo);
    }

    @PostMapping("/delete")
    public void deleteComment(@RequestParam Integer id){
        commentService.deleteComment(id);
    }

}
