package cn.gjyniubi.cinema.common.exception;

/**
 * @Author gujianyang
 * @Date 2021/2/13
 * @Class AssignmentException
 */
public class AssignmentException extends RuntimeException{
    public AssignmentException(String message) {
        super(message);
    }

    public AssignmentException(Throwable cause) {
        super(cause);
    }
}
