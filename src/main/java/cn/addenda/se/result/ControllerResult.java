package cn.addenda.se.result;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @Author ISJINHAO
 * @Date 2022/2/7 16:49
 */
public class ControllerResult<T> implements Serializable {

    private String requestId;

    private boolean success = false;

    private int errorCode;

    private String errorMsg;

    private String version;

    private long ts = System.currentTimeMillis();

    private T result;

    public ControllerResult() {
    }

    public ControllerResult(boolean success, T result) {
        this.success = success;
        this.result = result;
    }

    public static <R> ControllerResult<R> create(ServiceResult<R> serviceResult) {
        return create(serviceResult, a -> a);
    }

    public static <R, T> ControllerResult<R> create(ServiceResult<T> serviceResult, Function<T, R> function) {
        ControllerResult<R> controllerResult = new ControllerResult<>();
        controllerResult.setSuccess(ServiceResult.STATUS_SUCCESS.equals(serviceResult.getStatus()));
        controllerResult.setResult(function.apply(serviceResult.getResult()));
        if (!controllerResult.isSuccess()) {
            controllerResult.setErrorMsg(serviceResult.getErrorMsg());
        }
        return controllerResult;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
