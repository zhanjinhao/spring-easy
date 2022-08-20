package cn.addenda.se.service;

import cn.addenda.se.pojo.TxTest;
import cn.addenda.se.result.ServiceResult;

/**
 * @Author ISJINHAO
 * @Date 2022/2/26 23:00
 */
public interface TxTestService {

    ServiceResult<Boolean> insert(TxTest txTest);

}
