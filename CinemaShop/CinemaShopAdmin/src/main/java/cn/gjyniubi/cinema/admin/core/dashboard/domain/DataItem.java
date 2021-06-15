package cn.gjyniubi.cinema.admin.core.dashboard.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/6/2
 * @Class DataItem
 */
@Builder
@Data
public class DataItem {
    private Integer showCount;
    private List<Integer> graphData;
}
