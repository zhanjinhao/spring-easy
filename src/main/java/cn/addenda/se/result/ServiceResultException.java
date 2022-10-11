package cn.addenda.se.result;

import cn.addenda.se.SpringEasyException;

/**
 * @author ISJINHAO
 * @date 2022/4/8
 */
public class ServiceResultException extends SpringEasyException {

    public ServiceResultException(String message) {
        super(message);
    }

    public ServiceResultException(String message, Throwable e) {
        super(message, e);
    }

}
