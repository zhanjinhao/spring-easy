# 功能描述
项目的目的是抽象出来Spring-Mybatis开发中的一些公共处理方案。

## fieldfilling

### 业务描述

#### insert

自动给 create_user，create_time，del_fg，remark字段赋值

#### select

增加条件del_fg = 0

#### update

自动修改 modify_user，modify_time，remark的值

#### delete

转为update语句，并且修改modify_user，modify_time，remark，del_fg的值

### 使用

cn.addenda.daoeasy.fieldfilling.interceptor.FieldFillingInterceptor是Mybatis的插件实现，需要保证它最后一个被执行。
填充的值从cn.addenda.daoeasy.fieldfilling.FieldFillingContext相应的方法里获取。

### 填充的字段
```sql
alter table XXX
add column create_user varchar(10) null;

alter table XXX
add column create_time bigint null;

alter table XXX
add column modify_user varchar(10) null;

alter table XXX
add column modify_time bigint null;

alter table XXX
add column remark varchar(50) null;

alter table XXX
add column del_fg tinyint default 0;
```




## idfilling

服务化的系统中，表都是需要有主键的，且不依赖数据库的主键生成策略（Oracle的序列，MySQL的自增主键）而是使用外部的ID生成算法。
ID填充就是将这些算法生成的ID填充到SQL语句中。

ID是填充到Entity里的，所以只有 Mapper#insert(TUser tUser); 这样的写法才能生效


## multidatasource

多数据源是Spring提供的一个功能，一个事务应该对应一个数据源。

## spring

提供一些和Spring Context相关的工具类，方便开发。


## transaction

Spring提供的注解 @Transactional 能很好地帮助我们解决方法级别的事务，但是对于代码级别的事务仍然需要复杂的代码，
事务模块提供了简单易用的手动事务，完全兼容@Transactional。


## cdc

业务上有时候需要将表数据的变更推送至另外的服务，常用的方式是引入阿里的 cancel，但是对于小公司和项目来说，成本太高，
所以就开发了嵌入式的cdc实现，并且保证了日志的有序性。


## propertyRefresh

Spring外部化配置是不能被@Value注解刷新的。此模块为了刷新@Value注解而开发。采用Event的方式是为了解决property值的可重复读问题。

# 依赖
SQL解析器是个人开发的，运行此项目需要加入依赖。
地址：https://github.com/zhanjinhao/relational-operations。

# todo
DistributedLock Aspect.
cdc增加区分row模式和statement模式