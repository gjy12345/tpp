package cn.gjyniubi.cinema.common.coupon.rule.impl;

import cn.gjyniubi.cinema.common.coupon.model.FullRule;
import cn.gjyniubi.cinema.common.coupon.rule.CouponRule;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class FullReductionRule
 * 满减规则
 */
public class FullReductionRule implements CouponRule<FullRule> {

    @Override
    public BigDecimal calc(List<FullRule> rules, BigDecimal money) {
        FullRule bestRule=null;
        for (FullRule rule : rules) {
            //当前金额满足
            if(rule.getFull().compareTo(money)<=0){
                if(bestRule==null)
                    bestRule=rule;
                else if(bestRule.getFull().compareTo(rule.getFull())>=0)
                    bestRule=rule;
            }
        }
        money=bestRule!=null?money.subtract(bestRule.getReduce()):money;
        if(money.compareTo(BigDecimal.ZERO)<0)
            money=BigDecimal.ZERO;
        return money;
    }

}
