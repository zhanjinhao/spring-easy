package cn.addenda.se.transaction;

import cn.addenda.se.SpringEasyException;

/**
 * @author ISJINHAO
 * @date 2022/4/8
 */
public class TransactionException extends SpringEasyException {

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable e) {
        super(message, e);
    }

}
