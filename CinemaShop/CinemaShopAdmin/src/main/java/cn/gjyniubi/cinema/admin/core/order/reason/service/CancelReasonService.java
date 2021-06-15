package cn.gjyniubi.cinema.admin.core.order.reason.service;

import cn.gjyniubi.cinema.admin.core.order.reason.dto.OrderCancelReasonDto;
import cn.gjyniubi.cinema.admin.core.order.reason.mapper.CancelReasonMapper;
import cn.gjyniubi.cinema.admin.core.order.reason.vo.CreateReasonVo;
import cn.gjyniubi.cinema.admin.core.order.reason.vo.ReasonQueryVo;
import cn.gjyniubi.cinema.admin.core.order.reason.vo.UpdateReasonVo;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.domain.TableData;
import cn.gjyniubi.cinema.common.entry.OrderCancelReason;
import cn.gjyniubi.cinema.common.exception.DuplicateDataException;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.HttpRequestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/13
 * @Class CancelReasonService
 */
@AllArgsConstructor
@Service
public class CancelReasonService {

    private final CancelReasonMapper cancelReasonMapper;

    public TableData<OrderCancelReasonDto> getReasonList(ReasonQueryVo queryVo) {
        return TableData.buildData(
                cancelReasonMapper.selectReasonList(queryVo),
                cancelReasonMapper.selectReasonListCount(queryVo)
        );
    }

    @Transactional
    public void updateReason(UpdateReasonVo updateReasonVo) {
        OrderCancelReason reason = cancelReasonMapper.selectById(updateReasonVo.getId());
        if(reason==null)
            throw new NoSuchDataException("没有编号为:"+updateReasonVo.getId()+" 的退款理由!");
        BeanAssignment.copySameFields(updateReasonVo,reason);
        reason.setUpdateTime(new Date());
        cancelReasonMapper.updateById(reason);
    }

    @Transactional
    public void deleteReason(Integer id){
        //逻辑删除
        if (cancelReasonMapper.deleteById(id)==0) {
            throw new NoSuchDataException("删除失败，此退款原因可能已经被删除!");
        }
    }

    @Transactional
    public void createNewReason(CreateReasonVo createReasonVo) {
        createReasonVo.setReason(createReasonVo.getReason().trim());
        if (cancelReasonMapper.selectOne(new QueryWrapper<OrderCancelReason>()
                .eq("reason",createReasonVo.getReason()))!=null) {
            throw new DuplicateDataException("此退款理由已存在!");
        }
        OrderCancelReason reason=new OrderCancelReason();
        reason.setUpdateTime(new Date());
        reason.setCreateBy(HttpRequestUtil.getSysUserId());
        reason.setCreateTime(reason.getUpdateTime());
        reason.setLogicDel(UserContact.LOGIC_DEL_NO_DELETE);
        reason.setStatus(createReasonVo.getStatus());
        reason.setReason(createReasonVo.getReason());
        cancelReasonMapper.insert(reason);
    }
}
