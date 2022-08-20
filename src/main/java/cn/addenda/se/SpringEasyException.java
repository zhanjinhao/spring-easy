package cn.addenda.se;

/**
 * @author addenda
 * @datetime 2022/8/7 12:06
 */
public class SpringEasyException extends RuntimeException {

    public SpringEasyException(String message) {
        super(message);
    }

    public SpringEasyException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpringEasyException(Throwable cause) {
        super(cause);
    }

}
