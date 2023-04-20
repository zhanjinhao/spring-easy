package cn.addenda.se;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

/**
 * @author addenda
 * @since 2023/4/17 9:26
 */
public class DynamicProxyExceptionTest {

    public static void main(String[] args) {
        System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        AccountService accountService = new AccountServiceImpl();
        AccountService accountServiceProxy = (AccountService) Proxy.newProxyInstance(
            accountService.getClass().getClassLoader(),
            accountService.getClass().getInterfaces(),
            new AccountProxy(accountService));

        try {
            accountServiceProxy.getAccount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
// 自定义异常

class CustomException extends RuntimeException {

    private static final long serialVersionUID = -5427543428947291283L;

    public CustomException(String message) {
        super(message);
    }
}

// 接口

interface AccountService {

    void getAccount() throws SQLException;
}

// 实现类

class AccountServiceImpl implements AccountService {

    @Override
    public void getAccount() throws SQLException {
        throw new SQLException("for purpose!");
    }
}

/**
 * JDK动态代理类
 */
class AccountProxy implements InvocationHandler {

    private Object target;

    public AccountProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before invoke !");
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e;
        } finally {
            System.out.println("after invoke !");
        }
    }
}
