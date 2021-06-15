package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.entry.OrderComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class CommonOrderCommentMapper
 */
@Repository
@Mapper
public interface CommonOrderCommentMapper extends BaseMapper<OrderComment> {
}
