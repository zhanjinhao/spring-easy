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
        } catch (Throwable throwable) {
            reportAsRuntimeException(throwable);
            throw SystemException.unExpectedException();
        }
    }

    public interface ArgResLogExecutor<R> {

        R process() throws Throwable;
    }

    public interface VoidArgResLogExecutor {

        void process() throws Throwable;
    }

}
