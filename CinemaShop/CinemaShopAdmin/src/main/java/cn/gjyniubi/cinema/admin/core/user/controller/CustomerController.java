package cn.gjyniubi.cinema.admin.core.user.controller;

import cn.gjyniubi.cinema.admin.core.user.service.CustomerService;
import cn.gjyniubi.cinema.admin.core.user.vo.QueryCustomerVo;
import cn.gjyniubi.cinema.admin.core.user.vo.UpdateCustomerVo;
import cn.gjyniubi.cinema.common.domain.TableData;
import cn.gjyniubi.cinema.common.entry.Customer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class CustomerController
 */
@Api(tags = "消费者管理")
@RequestMapping("/customer")
@CrossOrigin
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @ApiOperation("获取消费者列表")
    @GetMapping("/list")
    public TableData<Customer> getCustomerList(@Validated QueryCustomerVo queryCustomerVo){
        return customerService.getCustomerList(queryCustomerVo);
    }

    @ApiOperation("重置密码")
    @PostMapping("/resetPwd")
    @Validated
    public void resetPassword(@Min(1) @RequestParam Integer id, @Size(max = 50) @RequestParam String password){
        customerService.resetPassword(id,password);
    }

    @PostMapping("/update")
    @ApiOperation("更新消费者")
    public void updateCustomer(@Validated @RequestBody UpdateCustomerVo updateCustomerVo){
        customerService.updateCustomer(updateCustomerVo);
    }

    @ApiOperation("删除消费者")
    @PostMapping("/delete")
    public void deleteCustomer(@RequestParam Integer id){
        customerService.deleteCustomer(id);
    }


}
