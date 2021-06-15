package cn.gjyniubi.cinema.admin.core.doc.film.vo;

import cn.gjyniubi.cinema.common.domain.BaseTableQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class QueryFilmVo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryFilmVo extends BaseTableQuery {
    @Min(1)
    private Integer id;
    @Size(max = 50)
    private String name;
    private Date begin;
    private Date end;
    @Min(0)
    @Max(1)
    private Integer status;
}
