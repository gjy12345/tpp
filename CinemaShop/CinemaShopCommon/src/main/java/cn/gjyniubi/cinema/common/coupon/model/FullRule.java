package cn.gjyniubi.cinema.common.coupon.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class FullRule
 */
@Data
public class FullRule {

    private BigDecimal full;//满
    private BigDecimal reduce;//减
}
