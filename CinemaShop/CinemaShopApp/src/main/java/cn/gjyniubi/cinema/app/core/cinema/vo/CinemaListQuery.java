package cn.gjyniubi.cinema.app.core.cinema.vo;

import cn.gjyniubi.cinema.common.domain.BaseListQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * @Author gujianyang
 * @Date 2021/5/26
 * @Class CinemaListQuery
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CinemaListQuery extends BaseListQuery {
    @Min(1)
    private Integer area;
}
