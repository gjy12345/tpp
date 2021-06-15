package cn.gjyniubi.cinema.admin.core.order.list.service;

import cn.gjyniubi.cinema.admin.core.order.list.dto.OrderDto;
import cn.gjyniubi.cinema.admin.core.order.list.mapper.OrderMapper;
import cn.gjyniubi.cinema.admin.core.order.list.vo.OrderListQuery;
import cn.gjyniubi.cinema.common.domain.TableData;
import cn.gjyniubi.cinema.common.entry.Order;
import cn.gjyniubi.cinema.common.enums.OrderStatus;
import cn.gjyniubi.cinema.common.exception.VerifyException;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author gujianyang
 * @Date 2021/6/1
 * @Class OrderService
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public TableData<OrderDto> getOrderList(OrderListQuery query) {
        return TableData.buildData(
                orderMapper.selectOrderList(query),
                orderMapper.selectOrderListCount(query)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void returnMoney(Integer id) {
        Order order = orderMapper.selectById(id);
        if(order.getStatus()== OrderStatus.HAS_PAY.getCode()||order.getStatus()==OrderStatus.USED.getCode()){
            if (orderMapper.update(null,new UpdateWrapper<Order>()
                    .set("`status`", OrderStatus.RETURN.getCode())
                    .eq("id",order.getId())
                    .eq("`status`",order.getStatus()))==0) {
                throw new VerifyException("退款失败，请检查订单状态");
            }
        }else
            throw new VerifyException("此订单状态错误!");
    }
}
