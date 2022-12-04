package cn.addenda.se.lock;

/**
 * @author addenda
 * @datetime 2022/11/30 19:16
 */
public class LockException extends RuntimeException {

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }
}
