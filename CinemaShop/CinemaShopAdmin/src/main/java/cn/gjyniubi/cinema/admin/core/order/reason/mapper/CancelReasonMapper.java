package cn.gjyniubi.cinema.admin.core.order.reason.mapper;

import cn.gjyniubi.cinema.admin.core.order.reason.dto.OrderCancelReasonDto;
import cn.gjyniubi.cinema.admin.core.order.reason.vo.ReasonQueryVo;
import cn.gjyniubi.cinema.common.entry.OrderCancelReason;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/13
 * @Class CancelReasonMapper
 */
@Mapper
@Repository
public interface CancelReasonMapper extends BaseMapper<OrderCancelReason> {

    List<OrderCancelReasonDto> selectReasonList(ReasonQueryVo queryVo);

    Integer selectReasonListCount(ReasonQueryVo queryVo);

}
