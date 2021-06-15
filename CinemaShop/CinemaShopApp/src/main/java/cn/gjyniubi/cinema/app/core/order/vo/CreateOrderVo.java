package cn.gjyniubi.cinema.app.core.order.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class CreateOrderVo
 */
@Data
public class CreateOrderVo {
    @NotBlank
    private String scheduleUid;
    @NotEmpty
    private List<OrderSite> sites;
    @Size(max = 50)
    @NotBlank
    private String phone;

    @Data
    public static final class OrderSite{
        private Integer x;
        private Integer y;
    }
}
