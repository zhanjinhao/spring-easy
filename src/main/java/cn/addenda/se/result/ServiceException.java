package cn.addenda.se.result;

/**
 * 业务异常和普通的异常相比，增加了 failedCode
 *
 * @author addenda
 * @datetime 2022/10/8 17:28
 */
public class ServiceException extends RuntimeException {

    public static final int DEFAULT_FAILED_CODE = 0;

    private final int failedCode;

    public ServiceException(String message) {
        super(message);
        this.failedCode = DEFAULT_FAILED_CODE;
    }

    public ServiceException(int failedCode) {
        this.failedCode = failedCode;
    }

    public ServiceException(String message, int failedCode) {
        super(message);
        this.failedCode = failedCode;
    }

    public ServiceException(String message, Throwable cause, int failedCode) {
        super(message, cause);
        this.failedCode = failedCode;
    }

    public ServiceException(Throwable cause, int failedCode) {
        super(cause);
        this.failedCode = failedCode;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int failedCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.failedCode = failedCode;
    }

    public int getFailedCode() {
        return failedCode;
    }

    public String printMessage() {
        return "errorCode: " + failedCode + ", message: " + getMessage();
    }
}
