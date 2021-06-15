package cn.gjyniubi.cinema.common.dto;

import cn.gjyniubi.cinema.common.domain.HallSite;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/5/20
 * @Class ScheduleHallSite
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleHallSite extends HallSite {
    protected String price;
    protected Boolean sale;
    protected Boolean enable;
    protected Integer row;
    protected Integer column;
}
