package cn.addenda.se.propertyrefresh;

import cn.addenda.se.SpringEasyException;

/**
 * @Author ISJINHAO
 * @Date 2022/4/4 11:42
 */
public class PropertyRefreshException extends SpringEasyException {

    public PropertyRefreshException(String message) {
        super(message);
    }

    public PropertyRefreshException(String message, Throwable cause) {
        super(message, cause);
    }
}
