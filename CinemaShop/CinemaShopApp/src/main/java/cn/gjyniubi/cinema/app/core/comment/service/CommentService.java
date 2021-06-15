package cn.gjyniubi.cinema.app.core.comment.service;

import cn.gjyniubi.cinema.app.core.cinema.mapper.FilmScheduleMapper;
import cn.gjyniubi.cinema.app.core.comment.dto.SimpleCommentDto;
import cn.gjyniubi.cinema.app.core.comment.mapper.CommentMapper;
import cn.gjyniubi.cinema.app.core.comment.vo.CreateCommentVo;
import cn.gjyniubi.cinema.app.core.order.mapper.OrderMapper;
import cn.gjyniubi.cinema.common.annotations.TrimArgs;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.entry.DocFilm;
import cn.gjyniubi.cinema.common.entry.FilmSchedule;
import cn.gjyniubi.cinema.common.entry.Order;
import cn.gjyniubi.cinema.common.entry.OrderComment;
import cn.gjyniubi.cinema.common.enums.OrderStatus;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.exception.VerifyException;
import cn.gjyniubi.cinema.common.mapper.CommonFilmDocMapper;
import cn.gjyniubi.cinema.common.util.CustomerUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class CommentService
 */
@Service
public class CommentService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private FilmScheduleMapper filmScheduleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommonFilmDocMapper commonFilmDocMapper;

    @Value("${project.auto-calc-film-score:false}")
    private boolean autoCalc;

    @TrimArgs
    @Transactional(rollbackFor = Exception.class)
    public void createComment(@TrimArgs CreateCommentVo createCommentVo) {
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("order_num",createCommentVo.getOrderNum()));
        if(order==null)
            throw new NoSuchDataException("此订单不存在 !");
        if(!order.getCusId().equals(CustomerUtil.getCurrentCustomer().getId()))
            throw new VerifyException("您无法评论其他人订单");
        if(order.getStatus()!= OrderStatus.USED.getCode())
            throw new VerifyException("请先观影后再近些评论!");
        if(order.getCommonStatus()!= UserContact.NOT_COMMENT)
            throw new VerifyException("此订单已完成评论!");
        FilmSchedule schedule = filmScheduleMapper.selectById(order.getScheduleId());
        OrderComment comment = new OrderComment();
        comment.setContent(createCommentVo.getContent());
        comment.setCreateTime(new Date());
        comment.setCusId(CustomerUtil.getCurrentCustomer().getId());
        comment.setFilmId(schedule.getFilmId());
        comment.setLogicDel(UserContact.LOGIC_DEL_NO_DELETE);
        comment.setOrderId(order.getId());
        comment.setScore(createCommentVo.getScore());
        comment.setScheduleId(schedule.getId());
        commentMapper.insert(comment);
        if (orderMapper.update(null,new UpdateWrapper<Order>()
                .eq("id",order.getId())
                .eq("common_status", UserContact.NOT_COMMENT)
                .set("common_status",UserContact.HAS_COMMENT))==0) {
            throw new VerifyException("请勿重复评论!");
        }
        if(autoCalc){
            commonFilmDocMapper.updateScore(comment.getFilmId());
        }
    }


    public List<SimpleCommentDto> getFilmRecentComments(String filmUid) {
        DocFilm film = commonFilmDocMapper.selectOne(new QueryWrapper<DocFilm>().eq("uid", filmUid));
        if(film==null)
            throw new NoSuchDataException("没有此电影!");
        return commentMapper.selectFilmRecentComment(film.getId());
    }
}
