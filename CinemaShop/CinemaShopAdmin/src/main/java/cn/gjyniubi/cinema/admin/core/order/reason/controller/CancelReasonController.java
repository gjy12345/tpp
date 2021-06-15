package cn.gjyniubi.cinema.admin.core.order.reason.controller;

import cn.gjyniubi.cinema.admin.core.order.reason.dto.OrderCancelReasonDto;
import cn.gjyniubi.cinema.admin.core.order.reason.service.CancelReasonService;
import cn.gjyniubi.cinema.admin.core.order.reason.vo.CreateReasonVo;
import cn.gjyniubi.cinema.admin.core.order.reason.vo.ReasonQueryVo;
import cn.gjyniubi.cinema.admin.core.order.reason.vo.UpdateReasonVo;
import cn.gjyniubi.cinema.common.domain.TableData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author gujianyang
 * @Date 2021/5/13
 * @Class CancelReasonController
 */
@Api(tags="退款理由")
@CrossOrigin
@RequestMapping("/order/reason")
@RestController
@AllArgsConstructor
public class CancelReasonController {

    private final CancelReasonService cancelReasonService;

    @ApiOperation("获取退款原因")
    @GetMapping("/list")
    public TableData<OrderCancelReasonDto> getList(@Validated ReasonQueryVo queryVo){
        return cancelReasonService.getReasonList(queryVo);
    }

    @ApiOperation("更新退款理由")
    @PostMapping("/update")
    public void updateReason(@Validated @RequestBody UpdateReasonVo updateReasonVo){
        cancelReasonService.updateReason(updateReasonVo);
    }

    @ApiOperation("删除退款理由")
    @PostMapping("/delete")
    public void deleteReason(@RequestParam Integer id){
        cancelReasonService.deleteReason(id);
    }

    @ApiOperation("创建新的退款理由")
    @PostMapping("/create")
    public void createReason(@Validated @RequestBody CreateReasonVo createReasonVo){
        cancelReasonService.createNewReason(createReasonVo);
    }

}
