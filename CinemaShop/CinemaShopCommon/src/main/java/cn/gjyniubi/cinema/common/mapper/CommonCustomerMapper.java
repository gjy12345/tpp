package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.entry.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class CommonCustomerMapper
 */
@Repository
@Mapper
public interface CommonCustomerMapper extends BaseMapper<Customer> {
}
