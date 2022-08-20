package cn.addenda.se.result;

import cn.addenda.se.SpringEasyException;

/**
 * @author ISJINHAO
 * @date 2022/4/8
 */
public class ResultException extends SpringEasyException {

    public ResultException(String message) {
        super(message);
    }

    public ResultException(String message, Throwable e) {
        super(message, e);
    }

}
