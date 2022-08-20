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

    // 调用方需要判断这个值，看看下一步怎么处理
    private ServiceResultStatus serviceResultStatus = ServiceResultStatus.SUCCESS;

    private String errorMsg;

    private long startTm = System.currentTimeMillis();
    private long endTm = System.currentTimeMillis();

    private T result;

    public ServiceResult(ServiceResultStatus serviceResultStatus, T result) {
        this.serviceResultStatus = serviceResultStatus;
        this.result = result;
    }

    public ServiceResult(T result) {
        this.result = result;
    }

    public ServiceResult() {
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

    public ServiceResultStatus getServiceResultStatus() {
        return serviceResultStatus;
    }

    public void setServiceResultStatus(ServiceResultStatus serviceResultStatus) {
        this.serviceResultStatus = serviceResultStatus;
    }

    public void setStartTm(long startTm) {
        this.startTm = startTm;
    }

    public long getStartTm() {
        return startTm;
    }

    public void setEndTm(long endTm) {
        this.endTm = endTm;
    }

    public long getEndTm() {
        return endTm;
    }

    @Override
    public String toString() {
        return "ServiceResult{" +
                "serviceResultStatus=" + serviceResultStatus +
                ", errorMsg='" + errorMsg + '\'' +
                ", startTm=" + covertTmToStr(startTm) +
                ", endTm=" + covertTmToStr(endTm) +
                ", result=" + result +
                '}';
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    private String covertTmToStr(long tm) {
        LocalDateTime localDateTime = BEDateUtil.timestampToLocalDateTime(tm);
        return formatter.format(localDateTime);
    }

}
