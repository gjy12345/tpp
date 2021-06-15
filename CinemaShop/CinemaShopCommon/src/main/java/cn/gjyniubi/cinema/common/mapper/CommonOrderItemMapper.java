package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.entry.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class CommonOrderItemMapper
 */
@Repository
@Mapper
public interface CommonOrderItemMapper extends BaseMapper<OrderItem> {
}
