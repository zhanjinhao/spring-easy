package cn.addenda.se.result;

/**
 * @author addenda
 * @datetime 2022/10/8 17:28
 */
public class ServiceException extends RuntimeException {

    private String excMsg;

    public ServiceException() {
    }

    public ServiceException(String excMsg) {
        this.excMsg = excMsg;
    }

    public ServiceException(String message, String excMsg) {
        super(message);
        this.excMsg = excMsg;
    }

    public ServiceException(String message, Throwable cause, String excMsg) {
        super(message, cause);
        this.excMsg = excMsg;
    }

    public ServiceException(Throwable cause, String excMsg) {
        super(cause);
        this.excMsg = excMsg;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String excMsg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.excMsg = excMsg;
    }

    public String getExcMsg() {
        return excMsg;
    }

    public void setExcMsg(String excMsg) {
        this.excMsg = excMsg;
    }
}
