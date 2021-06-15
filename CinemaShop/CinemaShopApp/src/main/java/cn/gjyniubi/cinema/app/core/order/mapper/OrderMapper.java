package cn.gjyniubi.cinema.app.core.order.mapper;

import cn.gjyniubi.cinema.app.core.order.dto.ListOrderDto;
import cn.gjyniubi.cinema.common.domain.BaseListQuery;
import cn.gjyniubi.cinema.common.mapper.CommonOrderMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/6/1
 * @Class OrderMapper
 */
@Mapper
@Repository
public interface OrderMapper extends CommonOrderMapper {

    List<ListOrderDto> selectOrderList(@Param("q") BaseListQuery query,@Param("cusId") Integer cusId);

}
