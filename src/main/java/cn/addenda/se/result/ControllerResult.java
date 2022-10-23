package cn.addenda.se.result;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 请求异常时：
 *
 * 业务异常时：
 *
 * @Author ISJINHAO
 * @Date 2022/2/7 16:49
 */
public class ControllerResult<T> implements Serializable {

    private String requestId;
    private String version;

    private long ts = System.currentTimeMillis();
    private T result;

    /**
     * 请求状态
     */
    private String requestStatus = StatusConst.FAILED;
    /**
     * 请求错误原因
     */
    private String requestErrorMsg;

    /**
     * 业务状态
     */
    private String businessStatus = StatusConst.FAILED;
    /**
     * 业务失败code
     */
    private Integer businessFailedCode;
    /**
     * 业务失败原因
     */
    private String businessFailedMsg;

    public ControllerResult() {
    }

    /**
     * 用于简便构建 请求成功&业务成功 时的结果对象
     */
    public ControllerResult(T result) {
        this.requestStatus = StatusConst.SUCCESS;
        this.businessStatus = StatusConst.SUCCESS;
        this.result = result;
    }

    public static <R> ControllerResult<R> create(ServiceResult<R> serviceResult) {
        return create(serviceResult, a -> a);
    }

    public static <R, T> ControllerResult<R> create(ServiceResult<T> serviceResult, Function<T, R> function) {
        ControllerResult<R> controllerResult = new ControllerResult<>();
        T result = serviceResult.getResult();
        if (result != null) {
            controllerResult.setResult(function.apply(result));
        }
        controllerResult.setRequestStatus(StatusConst.SUCCESS);
        controllerResult.setBusinessStatus(serviceResult.getStatus());
        if (StatusConst.FAILED.equals(controllerResult.getBusinessStatus())) {
            controllerResult.setBusinessFailedCode(serviceResult.getFailedCode());
            controllerResult.setBusinessFailedMsg(serviceResult.getFailedMsg());
        }
        return controllerResult;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestErrorMsg() {
        return requestErrorMsg;
    }

    public void setRequestErrorMsg(String requestErrorMsg) {
        this.requestErrorMsg = requestErrorMsg;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public Integer getBusinessFailedCode() {
        return businessFailedCode;
    }

    public void setBusinessFailedCode(Integer businessFailedCode) {
        this.businessFailedCode = businessFailedCode;
    }

    public String getBusinessFailedMsg() {
        return businessFailedMsg;
    }

    public void setBusinessFailedMsg(String businessFailedMsg) {
        this.businessFailedMsg = businessFailedMsg;
    }

}
