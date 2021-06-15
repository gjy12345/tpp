package cn.gjyniubi.cinema.app.core.order.controller;

import cn.gjyniubi.cinema.app.core.order.dto.ListOrderDto;
import cn.gjyniubi.cinema.app.core.order.dto.OrderDto;
import cn.gjyniubi.cinema.app.core.order.service.OrderService;
import cn.gjyniubi.cinema.app.core.order.vo.CreateOrderVo;
import cn.gjyniubi.cinema.common.domain.BaseListQuery;
import cn.gjyniubi.cinema.common.domain.JsonResult;
import cn.gjyniubi.cinema.common.domain.ListData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class OrderController
 */
@Api(tags = "订单相关")
@CrossOrigin
@RequestMapping("/order")
@RestController
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping("/createOrder")
    public JsonResult<String> createOrder(@Validated @RequestBody CreateOrderVo createOrderVo) throws InterruptedException {
        return JsonResult.buildSuccess(orderService.createOrder(createOrderVo));
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/getOrderInfo")
    public OrderDto getOrderInfo(@NotBlank @RequestParam String orderNum){
        return orderService.getOrderInfo(orderNum);
    }

    @ApiOperation("支付订单")
    @PostMapping("/pay")
    public void pay(@NotBlank @RequestParam String orderNum){
        orderService.pay(orderNum);
    }

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    public ListData<ListOrderDto> getOrderList(BaseListQuery query){
        return orderService.getOrderList(query);
    }

}
