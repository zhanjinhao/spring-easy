package cn.addenda.se.result;

import cn.addenda.businesseasy.util.BEDateUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author ISJINHAO
 * @Date 2022/2/11 11:40
 */
public class ServiceResult<T> implements Serializable {

    /**
     * 调用方需要判断这个值，看看下一步怎么处理
     */
    private String status;

    private long startTs = System.currentTimeMillis();
    private long endTs = System.currentTimeMillis();

    private String failedMsg;

    private Integer failedCode;

    private Throwable throwable;

    private T result;

    public ServiceResult(String status, T result) {
        this.status = status;
        this.result = result;
    }

    public String getFailedMsg() {
        return failedMsg;
    }

    public void setFailedMsg(String failedMsg) {
        this.failedMsg = failedMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartTs(long startTs) {
        this.startTs = startTs;
    }

    public long getStartTs() {
        return startTs;
    }

    public void setEndTs(long endTs) {
        this.endTs = endTs;
    }

    public long getEndTs() {
        return endTs;
    }

    public Integer getFailedCode() {
        return failedCode;
    }

    public void setFailedCode(Integer failedCode) {
        this.failedCode = failedCode;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public String toString() {
        return "ServiceResult{" +
                "status='" + status + '\'' +
                ", startTs='" + covertTmToStr(startTs) + "'" +
                ", endTs='" + covertTmToStr(endTs) + "'" +
                ", failedMsg='" + failedMsg + '\'' +
                ", failedCode=" + failedCode +
                ", throwable=" + throwable +
                ", result=" + result +
                '}';
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    private String covertTmToStr(long tm) {
        LocalDateTime localDateTime = BEDateUtils.timestampToLocalDateTime(tm);
        return formatter.format(localDateTime);
    }

    public static <T> ServiceResult<T> success(T result) {
        return new ServiceResult<>(StatusConst.SUCCESS, result);
    }

    public static <T> ServiceResult<T> dispatch(T result, int failedCode, String failedMsg, Throwable throwable) {
        ServiceResult<T> tServiceResult = new ServiceResult<>(StatusConst.DISPATCH, result);
        tServiceResult.setFailedCode(failedCode);
        tServiceResult.setFailedMsg(failedMsg);
        tServiceResult.setThrowable(throwable);
        return tServiceResult;
    }

    public static <T> ServiceResult<T> failed(T result, String failedMsg) {
        ServiceResult<T> tServiceResult = new ServiceResult<>(StatusConst.FAILED, result);
        tServiceResult.setFailedMsg(failedMsg);
        tServiceResult.setFailedCode(ServiceException.DEFAULT_FAILED_CODE);
        return tServiceResult;
    }

    public static <T> ServiceResult<T> failed(T result, int failedCode, String failedMsg) {
        ServiceResult<T> tServiceResult = new ServiceResult<>(StatusConst.FAILED, result);
        tServiceResult.setFailedCode(failedCode);
        tServiceResult.setFailedMsg(failedMsg);
        return tServiceResult;
    }

    public static <T> ServiceResult<T> failed(T result, int failedCode, String failedMsg, Throwable throwable) {
        ServiceResult<T> tServiceResult = new ServiceResult<>(StatusConst.FAILED, result);
        tServiceResult.setFailedCode(failedCode);
        tServiceResult.setFailedMsg(failedMsg);
        tServiceResult.setThrowable(throwable);
        return tServiceResult;
    }

}
