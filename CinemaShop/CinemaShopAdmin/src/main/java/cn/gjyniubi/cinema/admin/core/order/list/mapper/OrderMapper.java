package cn.gjyniubi.cinema.admin.core.order.list.mapper;

import cn.gjyniubi.cinema.admin.core.order.list.dto.OrderDto;
import cn.gjyniubi.cinema.admin.core.order.list.vo.OrderListQuery;
import cn.gjyniubi.cinema.common.mapper.CommonOrderMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/6/1
 * @Class OrderMapper
 */
@Repository
@Mapper
public interface OrderMapper extends CommonOrderMapper {

    List<OrderDto> selectOrderList(OrderListQuery query);

    Integer selectOrderListCount(OrderListQuery query);
}
