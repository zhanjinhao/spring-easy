package cn.addenda.se.multidatasource;


import cn.addenda.se.SpringEasyException;

/**
 * @author ISJINHAO
 * @date 2022/4/8
 */
public class MultiDataSourceException extends SpringEasyException {

    public MultiDataSourceException(String message) {
        super(message);
    }

    public MultiDataSourceException(String message, Throwable e) {
        super(message, e);
    }

}
