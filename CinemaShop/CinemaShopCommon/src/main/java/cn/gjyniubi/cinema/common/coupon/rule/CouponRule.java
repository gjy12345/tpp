package cn.gjyniubi.cinema.common.coupon.rule;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class CouponRule
 */
public interface CouponRule <T>{

    //根据规则计算
    BigDecimal calc(List<T> rules,BigDecimal money);

}
