package cn.addenda.se.result;

import cn.addenda.businesseasy.util.BEDateUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author ISJINHAO
 * @Date 2022/2/11 11:40
 */
public class ServiceResult<T> implements Serializable {

    /**
     * 成功
     */
    public static final String STATUS_SUCCESS = "SUCCESS";

    /**
     * 发生错误，异常被处理
     */
    public static final String STATUS_ERROR = "ERROR";

    /**
     * 状态ErrorMsg定，需要调用方判断ErrorMsg。
     * 这个状态是调用方代码设置的，不会根据异常在AOP中自动赋值。
     */
    public static final String STATUS_DISPATCH = "DISPATCH";

    /**
     * 调用方需要判断这个值，看看下一步怎么处理
     */
    private String status = STATUS_SUCCESS;

    private long startTs = System.currentTimeMillis();
    private long endTs = System.currentTimeMillis();

    private String errorMsg;

    private T result;

    public ServiceResult() {
    }

    public ServiceResult(T result) {
        this.result = result;
    }

    public ServiceResult(String status, T result) {
        this.status = status;
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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

    @Override
    public String toString() {
        return "ServiceResult{" +
                "status=" + status +
                ", errorMsg='" + errorMsg + '\'' +
                ", startTs=" + covertTmToStr(startTs) +
                ", endTs=" + covertTmToStr(endTs) +
                ", result=" + result +
                '}';
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    private String covertTmToStr(long tm) {
        LocalDateTime localDateTime = BEDateUtil.timestampToLocalDateTime(tm);
        return formatter.format(localDateTime);
    }

    public static <T> ServiceResult<T> success(T result) {
        return new ServiceResult<>(STATUS_SUCCESS, result);
    }

    public static <T> ServiceResult<T> error(T result, String errorMsg) {
        ServiceResult<T> tServiceResult = new ServiceResult<>(STATUS_ERROR, result);
        tServiceResult.setErrorMsg(errorMsg);
        return tServiceResult;
    }

}
