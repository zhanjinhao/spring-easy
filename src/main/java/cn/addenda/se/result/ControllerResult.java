package cn.addenda.se.result;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 请求异常时：
 * <p>
 * 业务异常时：
 *
 * @Author ISJINHAO
 * @Date 2022/2/7 16:49
 */
public class ControllerResult<T> implements Serializable {

    private String reqId;
    private String version;

    private long ts = System.currentTimeMillis();
    private T result;

    /**
     * 请求状态
     */
    private String reqStatus = StatusConst.FAILED;
    /**
     * 请求错误原因
     */
    private String reqErrorMsg;

    /**
     * 业务状态
     */
    private String bizStatus = StatusConst.FAILED;
    /**
     * 业务失败code
     */
    private Integer bizFailedCode;
    /**
     * 业务失败原因
     */
    private String bizFailedMsg;

    public ControllerResult() {
    }

    /**
     * 用于简便构建 请求成功&业务成功 时的结果对象
     */
    public ControllerResult(T result) {
        this.reqStatus = StatusConst.SUCCESS;
        this.bizStatus = StatusConst.SUCCESS;
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
        controllerResult.setReqStatus(StatusConst.SUCCESS);
        controllerResult.setBizStatus(serviceResult.getStatus());
        if (StatusConst.FAILED.equals(controllerResult.getBizStatus())) {
            controllerResult.setBizFailedCode(serviceResult.getFailedCode());
            controllerResult.setBizFailedMsg(serviceResult.getFailedMsg());
        }
        return controllerResult;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
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

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    public String getReqErrorMsg() {
        return reqErrorMsg;
    }

    public void setReqErrorMsg(String reqErrorMsg) {
        this.reqErrorMsg = reqErrorMsg;
    }

    public String getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    public Integer getBizFailedCode() {
        return bizFailedCode;
    }

    public void setBizFailedCode(Integer bizFailedCode) {
        this.bizFailedCode = bizFailedCode;
    }

    public String getBizFailedMsg() {
        return bizFailedMsg;
    }

    public void setBizFailedMsg(String bizFailedMsg) {
        this.bizFailedMsg = bizFailedMsg;
    }

}
