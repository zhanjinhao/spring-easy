# 功能描述
项目的目的是抽象出来Spring开发中的一些公共处理方案。

## multidatasource

多数据源是Spring提供的一个功能，这里进行了一下封装：

参考：cn.addenda.se.multidatasource.MultiDataSourceConvertibleTest。


## propertyRefresh

Spring外部化配置是不能被@Value注解刷新的。此模块为了刷新@Value注解而开发。采用Event的方式是为了解决property值的可重复读问题。

参考：cn.addenda.se.propertyrefresh.PropertyRefreshApplication。


## result

业务类抛出的异常通常会通过增加全局拦截器的方式的处理，进而不将内部信息暴露出去。
但是有时候需要将异常更定制化，尤其是异常信息那一块。此模块是根据自己的实践经验开发出来的一种定制化处理方案。

## springcontenxt

提供一些和Spring Context相关的工具类，方便开发。

参考：
- cn.addenda.se.springcontext.ApplicationContextUtilTest
- cn.addenda.se.springcontext.ValueResolverUtilTest

## transaction

Spring提供的注解 @Transactional 能很好地帮助我们解决方法级别的事务，但是对于代码级别的事务仍然需要复杂的代码，
事务模块提供了简单易用的手动事务，完全兼容@Transactional。


# todo
DistributedLock Aspect.
