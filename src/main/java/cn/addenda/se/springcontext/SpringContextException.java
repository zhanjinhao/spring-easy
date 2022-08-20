package cn.addenda.se.springcontext;

import cn.addenda.se.SpringEasyException;

/**
 * @author addenda
 * @datetime 2022/8/7 12:06
 */
public class SpringContextException extends SpringEasyException {

    public SpringContextException(String message) {
        super(message);
    }

    public SpringContextException(String message, Throwable cause) {
        super(message, cause);
    }

}
