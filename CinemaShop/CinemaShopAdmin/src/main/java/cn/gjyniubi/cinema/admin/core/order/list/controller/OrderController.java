package cn.gjyniubi.cinema.admin.core.order.list.controller;

import cn.gjyniubi.cinema.admin.core.order.list.dto.OrderDto;
import cn.gjyniubi.cinema.admin.core.order.list.service.OrderService;
import cn.gjyniubi.cinema.admin.core.order.list.vo.OrderListQuery;
import cn.gjyniubi.cinema.common.domain.TableData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author gujianyang
 * @Date 2021/6/1
 * @Class OrderController
 */
@Api(tags = "订单相关接口")
@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    public TableData<OrderDto> getOrderList(@Validated OrderListQuery query){
        return orderService.getOrderList(query);
    }

    @ApiOperation("退款")
    @PostMapping("/returnMoney")
    public void returnMoney(@RequestParam Integer id){
        orderService.returnMoney(id);
    }
}
