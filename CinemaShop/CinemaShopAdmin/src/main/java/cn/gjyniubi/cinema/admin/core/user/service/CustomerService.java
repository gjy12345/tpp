package cn.gjyniubi.cinema.admin.core.user.service;

import cn.gjyniubi.cinema.admin.core.user.mapper.CustomerMapper;
import cn.gjyniubi.cinema.admin.core.user.vo.QueryCustomerVo;
import cn.gjyniubi.cinema.admin.core.user.vo.UpdateCustomerVo;
import cn.gjyniubi.cinema.common.annotations.TrimArgs;
import cn.gjyniubi.cinema.common.domain.TableData;
import cn.gjyniubi.cinema.common.entry.Customer;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.exception.VerifyException;
import cn.gjyniubi.cinema.common.util.AesUtil;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.FileUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class CustomerService
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    public TableData<Customer> getCustomerList(QueryCustomerVo queryCustomerVo) {
        return TableData.buildData(
                customerMapper.selectCustomerList(queryCustomerVo),
                customerMapper.selectCustomerCount(queryCustomerVo)
        );
    }

    public void resetPassword(Integer id, String password) {
        if(customerMapper.update(null,new UpdateWrapper<Customer>().eq("id",id)
            .set("password", AesUtil.encryptAES(password)))==0)
            throw new NoSuchDataException("没有此消费者!");
    }

    @TrimArgs
    public void updateCustomer(UpdateCustomerVo updateCustomerVo) {
        if(updateCustomerVo.getAvatar()!=null&& !FileUtil.isSafeImageFile(updateCustomerVo.getAvatar())){
            throw new VerifyException("错误的图片!");
        }
        Customer customer = customerMapper.selectById(updateCustomerVo.getId());
        if(customer==null)
            throw new NoSuchDataException("此消费者不存在!");
        BeanAssignment.copySameFields(updateCustomerVo,customer);
        customerMapper.updateById(customer);
    }

    public void deleteCustomer(Integer id) {
        if (customerMapper.deleteById(id)==0) {
            throw new NoSuchDataException("删除失败，不存在此消费者!");
        }
    }
}
