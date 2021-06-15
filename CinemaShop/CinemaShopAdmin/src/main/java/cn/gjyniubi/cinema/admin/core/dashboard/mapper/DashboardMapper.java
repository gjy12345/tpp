package cn.gjyniubi.cinema.admin.core.dashboard.mapper;

import cn.gjyniubi.cinema.admin.core.dashboard.dto.DataItemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/6/2
 * @Class DashboardMapper
 */
@Repository
@Mapper
public interface DashboardMapper {

    List<DataItemDto> selectDayNewCustomer(@Param("begin") Date begin,@Param("end") Date end);

    List<DataItemDto> selectDayIncome(@Param("begin") Date begin,@Param("end") Date end);

    List<DataItemDto> selectDayOrder(@Param("begin") Date begin,@Param("end") Date end);

    List<DataItemDto> selectDayNewCommont(@Param("begin") Date begin,@Param("end") Date end);
}
