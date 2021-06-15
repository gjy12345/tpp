package cn.gjyniubi.cinema.admin.core.doc.hall.vo;

import cn.gjyniubi.cinema.common.domain.BaseTableQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author gujianyang
 * @Date 2021/5/19
 * @Class QueryHallVo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryHallVo extends BaseTableQuery {
    @NotNull
    @Min(1)
    private Integer cinemaId;
    @Min(0)
    @Max(1)
    private Integer status;
    @Size(max = 50)
    private String name;
}
