package cn.gjyniubi.cinema.admin.core.doc.hall.vo;

import cn.gjyniubi.cinema.common.domain.HallSite;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author gujianyang
 * @Date 2021/5/19
 * @Class HallSiteVo
 */
@Data
public class HallSiteVo {
    @NotNull
    @Min(1)
    private Integer hallId;
    private HallSite[][] sites;
}
