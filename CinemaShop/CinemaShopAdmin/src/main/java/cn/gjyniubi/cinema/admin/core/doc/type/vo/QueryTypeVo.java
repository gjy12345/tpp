package cn.gjyniubi.cinema.admin.core.doc.type.vo;

import cn.gjyniubi.cinema.common.domain.BaseTableQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class QueryTypeVo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryTypeVo extends BaseTableQuery {
    @Size(max = 30)
    private String name;
    @Min(0)
    @Max(1)
    private Integer status;
}
