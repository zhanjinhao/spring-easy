package cn.addenda.se.service;

import cn.addenda.se.multidatasource.MultiDataSourceConstant;
import cn.addenda.se.multidatasource.MultiDataSourceKey;
import cn.addenda.se.paramreslog.ParamResLog;
import cn.addenda.se.pojo.TxTest;
import cn.addenda.se.mapper.TxTestMapper;
import cn.addenda.se.result.ServiceResult;
import cn.addenda.se.result.ServiceResultConvertible;
import cn.addenda.se.result.ServiceResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author ISJINHAO
 * @Date 2022/2/26 23:01
 */
public class TxTestServiceImpl implements TxTestService {

    @Autowired
    private TxTestMapper txTestMapper;

    @Value("${db.username}")
    private String name;

    @Override
    @ParamResLog
    @ServiceResultConvertible(errorTo = ServiceResultConvertible.ERROR_TO_SUCCESS, exceptionClass = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Boolean> insert(TxTest txTest) {
        boolean success = txTestMapper.insert(txTest) > 0;
        return new ServiceResult<>(ServiceResultStatus.SUCCESS, success);
    }

}
