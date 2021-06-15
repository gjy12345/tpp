package cn.gjyniubi.cinema.admin.core.dashboard.service;

import cn.gjyniubi.cinema.admin.core.dashboard.domain.DataItem;
import cn.gjyniubi.cinema.admin.core.dashboard.dto.DataItemDto;
import cn.gjyniubi.cinema.admin.core.dashboard.mapper.DashboardMapper;
import cn.gjyniubi.cinema.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/6/2
 * @Class DashboardService
 */
@Service
public class DashboardService {

    @Autowired
    private DashboardMapper dashboardMapper;

    public List<DataItem> getDashboardData() {
        List<DataItem> result = new ArrayList<>();
        Date begin = DateTimeUtil.localDateTimeToDate(LocalDateTime.now().minusDays(7));
        Date end = new Date();
        result.add(transferData(dashboardMapper.selectDayNewCustomer(begin , end)));
        result.add(transferData(dashboardMapper.selectDayNewCommont(begin,end)));
        result.add(transferData(dashboardMapper.selectDayIncome(begin,end)));
        result.add(transferData(dashboardMapper.selectDayOrder(begin,end)));
        return result;
    }

    private DataItem transferData(List<DataItemDto> list){
        LocalDateTime day = LocalDateTime.now();
        Date temp;
        int listIndex=0;
        List<Integer> arr = new ArrayList<>();
        Integer dayCount;
        for(int i=6;i>=0;i--){
            temp = DateTimeUtil.localDateTimeToDate(day.minusDays(i));
            if(listIndex<list.size()&&DateTimeUtil.isSameDay(temp,list.get(listIndex).getDay())){
                dayCount=list.get(listIndex).getCount();
                listIndex++;
            }else
                dayCount = 0;
            arr.add(dayCount);
        }
        return DataItem.builder()
                .graphData(arr)
                .showCount(arr.get(arr.size()-1))
                .build();
    }

}
