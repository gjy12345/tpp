package cn.gjyniubi.cinema.common.service;

import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.entry.NumberRule;
import cn.gjyniubi.cinema.common.enums.NumberType;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.mapper.CommonNumberMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class NumberService
 */
@Service
public class CommonNumberService {

    @Autowired
    private CommonNumberMapper commonNumberMapper;

    //生成编号
    public String generateNumber(NumberType numberType, int retryCount) throws InterruptedException {
        if(retryCount<0)
            return null;
        NumberRule rule = commonNumberMapper.selectOne(new QueryWrapper<NumberRule>()
                .eq("type",numberType.getType()));
        if(rule==null)
            throw new NoSuchDataException("没有此编号规则");
        if(rule.getStatus().equals(UserContact.LOCKED)){
            Thread.sleep(50);
            return generateNumber(numberType,retryCount-1);
        }
        int count=0;
        try{
             count = commonNumberMapper.update(null,new UpdateWrapper<NumberRule>()
                    .set("`status`",UserContact.LOCKED)
                    .eq("type",numberType.getType())
                    .eq("`status`",UserContact.UNLOCKED));
            if(count == 0){
                Thread.sleep(50);
                return generateNumber(numberType,retryCount-1);
            }
            return generateNumber(rule);
        }finally {
            if(count>0){
                commonNumberMapper.update(null,new UpdateWrapper<NumberRule>()
                        .set("`status`",UserContact.UNLOCKED)
                        .eq("type",numberType.getType()));
            }
        }
    }

    @Transactional
    public String generateNumber(NumberRule rule){
        String number = String.format("%0"+rule.getLen()+"d",rule.getNumber());
        rule.setNumber(rule.getNumber()+1);
        commonNumberMapper.updateById(rule);
        return number;
    }

}
