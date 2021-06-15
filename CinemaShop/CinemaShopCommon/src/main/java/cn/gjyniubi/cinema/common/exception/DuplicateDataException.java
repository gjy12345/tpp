package cn.gjyniubi.cinema.common.exception;

/**
 * @Author gujianyang
 * @Date 2021/5/14
 * @Class DuplicateDataException
 * 重复数据
 */
public class DuplicateDataException extends RuntimeException{
    public DuplicateDataException(String message) {
        super(message);
    }
}
