package cn.addenda.se.mapper;


import cn.addenda.se.multidatasource.MultiDataSourceConstant;
import cn.addenda.se.multidatasource.MultiDataSourceKey;
import cn.addenda.se.pojo.TxTest;

/**
 * @author ISJINHAO
 * @date 2020/7/27
 */
public interface TxTestMapper {

    @MultiDataSourceKey(mode = MultiDataSourceConstant.SLAVE)
    Integer insert(TxTest txTest);

}
