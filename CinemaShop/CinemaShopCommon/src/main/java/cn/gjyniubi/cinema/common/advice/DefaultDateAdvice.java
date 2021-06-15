package cn.gjyniubi.cinema.common.advice;

import cn.gjyniubi.cinema.common.exception.AssignmentException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/11
 * @Class DefaultDateAdvice
 */
@RestControllerAdvice
public class DefaultDateAdvice {

    private static final ThreadLocal<SimpleDateFormat> styleOne=
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static final ThreadLocal<SimpleDateFormat> styleTwo=
            ThreadLocal.withInitial(()->new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));

    private static final ThreadLocal<SimpleDateFormat> styleThree=
            ThreadLocal.withInitial(()->new SimpleDateFormat("yyyy/MM/dd"));

    private static final ThreadLocal<SimpleDateFormat> styleFour=
            ThreadLocal.withInitial(()->new SimpleDateFormat("yyyy-MM-dd"));
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if(binder.getConversionService() instanceof GenericConversionService){
            GenericConversionService genericConversionService= (GenericConversionService) binder.getConversionService();
            genericConversionService.addConverter(new DataConverter());
        }
    }

    private static class DataConverter implements Converter<String,Date>{

        @Override
        public Date convert(String source) {
            try {
                if(source.contains("/")){
                    return source.contains(" ")?styleTwo.get().parse(source):styleThree.get().parse(source);
                }else if(source.contains("-")){
                    return source.contains(" ")?styleOne.get().parse(source):styleFour.get().parse(source);
                }
            } catch (ParseException e) {
                throw new AssignmentException(e);
            }
            throw new AssignmentException("错误的时间格式:"+source);
        }
    }

}
