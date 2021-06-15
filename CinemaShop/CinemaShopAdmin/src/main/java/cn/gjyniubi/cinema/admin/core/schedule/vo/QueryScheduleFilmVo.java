package cn.gjyniubi.cinema.admin.core.schedule.vo;

import cn.gjyniubi.cinema.common.domain.BaseTableQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/20
 * @Class QueryScheduleFilmVo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryScheduleFilmVo extends BaseTableQuery {
    @Size(max = 50)
    private String cinemaName;
    @Size(max = 50)
    private String filmName;
    private Date begin;
    private Date end;
}
