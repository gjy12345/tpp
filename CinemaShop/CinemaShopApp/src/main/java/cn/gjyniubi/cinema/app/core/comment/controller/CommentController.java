package cn.gjyniubi.cinema.app.core.comment.controller;

import cn.gjyniubi.cinema.app.core.comment.dto.SimpleCommentDto;
import cn.gjyniubi.cinema.app.core.comment.service.CommentService;
import cn.gjyniubi.cinema.app.core.comment.vo.CreateCommentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class CommentController
 */
@Api(tags = "评论相关")
@CrossOrigin
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation("发布评论")
    @PostMapping("/create")
    public void createComment(@Validated @RequestBody CreateCommentVo createCommentVo){
        commentService.createComment(createCommentVo);
    }


    @ApiOperation("获取最新十条")
    @GetMapping("/filmRecentComments")
    public List<SimpleCommentDto> getFilmRecentComments(@NotBlank @RequestParam String filmUid){
        return commentService.getFilmRecentComments(filmUid);
    }

}
