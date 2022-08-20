package cn.addenda.se.result;

import java.io.Serializable;

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

    private long date = System.currentTimeMillis();

    private T result;

    public ControllerResult() {
    }

    public ControllerResult(boolean success, T result) {
        this.success = success;
        this.result = result;
    }

    public ControllerResult(ServiceResult<T> serviceResult) {
        this.success = serviceResult.getServiceResultStatus().equals(ServiceResultStatus.SUCCESS);
        this.result = serviceResult.getResult();
        this.errorMsg = serviceResult.getErrorMsg();
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
