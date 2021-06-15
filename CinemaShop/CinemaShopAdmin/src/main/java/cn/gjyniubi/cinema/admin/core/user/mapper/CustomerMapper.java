package cn.gjyniubi.cinema.admin.core.user.mapper;

import cn.gjyniubi.cinema.admin.core.user.vo.QueryCustomerVo;
import cn.gjyniubi.cinema.common.entry.Customer;
import cn.gjyniubi.cinema.common.mapper.CommonCustomerMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class CustomerMapper
 */
@Repository
@Mapper
public interface CustomerMapper extends CommonCustomerMapper {

    List<Customer> selectCustomerList(QueryCustomerVo queryCustomerVo);

    Integer selectCustomerCount(QueryCustomerVo queryCustomerVo);
}
