package cn.addenda.se.argreslog;

import cn.addenda.se.result.SystemException;

/**
 * @author addenda
 * @datetime 2023/3/9 11:17
 */
public class ArgResLogUtils extends ArgResLogSupport {

    public static void doLog(VoidArgResLogExecutor executor, Object... arguments) {
        doLog(null, executor, arguments);
    }

    public static void doLog(String callerInfo, VoidArgResLogExecutor executor, Object... arguments) {
        doLog(callerInfo, ArgResLogAttr.defaultAttr(), executor, arguments);
    }

    public static void doLog(String callerInfo, ArgResLogAttr attr, VoidArgResLogExecutor executor, Object... arguments) {
        doLog(callerInfo, attr,
            () -> {
                executor.process();
                return null;
            }, arguments);
    }

    public static <R> R doLog(ArgResLogExecutor<R> executor, Object... arguments) {
        return doLog(null, executor, arguments);
    }

    public static <R> R doLog(String callerInfo, ArgResLogExecutor<R> executor, Object... arguments) {
        return doLog(callerInfo, ArgResLogAttr.defaultAttr(), executor, arguments);
    }

    public static <R> R doLog(String callerInfo, ArgResLogAttr attr, ArgResLogExecutor<R> executor, Object... arguments) {
        try {
            return invoke(attr, arguments, executor::process, callerInfo);
        }
        // invoke在三种情况下会发生异常：
        // 第一种情况：ArgRes的使用不正确，此时内部异常为SystemException，这类异常直接扔出去。
        // 第二种情况：executor内部的异常，此时原始异常会被包装成ArgResLogException。
        //           当原始异常为RuntimeException时，将原始异常抛出。
        // 第三种情况：ArgRes功能本身出现的异常，这类异常直接扔出去。理论上不会发生。此时不走catch块。
        catch (ArgResLogException argResLogException) {
            Throwable throwable = unWarp(argResLogException);
            if (throwable instanceof SystemException) {
                throw argResLogException;
            } else if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw argResLogException;
        }
    }

    public interface ArgResLogExecutor<R> {

        R process() throws Throwable;
    }

    public interface VoidArgResLogExecutor {

        void process() throws Throwable;
    }

}
