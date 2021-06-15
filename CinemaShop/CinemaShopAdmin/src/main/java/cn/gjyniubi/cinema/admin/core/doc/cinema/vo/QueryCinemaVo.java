package cn.gjyniubi.cinema.admin.core.doc.cinema.vo;

import cn.gjyniubi.cinema.common.domain.BaseTableQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class QueryCinemaVo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryCinemaVo extends BaseTableQuery {
    @Min(1)
    private Integer id;
    @Size(max = 20)
    private String name;
    @Min(0)
    @Max(1)
    private Integer status;
    private String province;
    private String city;
    private String country;
}
