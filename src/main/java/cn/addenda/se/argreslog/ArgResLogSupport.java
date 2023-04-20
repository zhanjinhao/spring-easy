package cn.addenda.se.argreslog;

import cn.addenda.businesseasy.util.BEJsonUtils;
import cn.addenda.businesseasy.util.BEStackTraceUtils;
import cn.addenda.businesseasy.util.ExceptionUtil;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;

/**
 * @author addenda
 * @datetime 2022/9/29 13:51
 */
@Slf4j
public class ArgResLogSupport {

    private static final String NULL_STR = "NIL";

    private static final Map<String, AtomicLong> SEQUENCE_GENERATOR_MAP = new ConcurrentHashMap<>();

    static ThreadLocal<Integer> nestNumber = ThreadLocal.withInitial(() -> 0);

    protected static <R> R invoke(ArgResLogAttr argResLogAttr, Object[] arguments, TSupplier<R> supplier, String callerInfo) throws Throwable {
        if (0 != nestNumber.get()) {
            log.warn("ArgResLog 出现了嵌套，打印的日志可能比较乱，请梳理日志粒度！");
        }
        try {
            nestNumber.set(nestNumber.get() + 1);
            if (callerInfo == null) {
                callerInfo = BEStackTraceUtils.getDetailedCallerInfo(true,
//                ReflectiveMethodInvocation.class.getName(),
//                "org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor",
                    ArgResLogSupport.class.getName(),
                    ArgResLogMethodInterceptor.class.getName(),
                    ArgResLogUtils.class.getName());
            }
            return doInvoke(argResLogAttr, arguments, supplier, callerInfo);
        } finally {
            nestNumber.set(nestNumber.get() - 1);
        }
    }

    private static <R> R doInvoke(ArgResLogAttr argResLogAttr, Object[] arguments, TSupplier<R> supplier, String callerInfo) throws Throwable {
        long sequence = SEQUENCE_GENERATOR_MAP.computeIfAbsent(callerInfo, s -> new AtomicLong(0L)).getAndDecrement();

        String argumentsStr;
        if (argResLogAttr.isLogParam()) {
            if (arguments == null || arguments.length == 0) {
                argumentsStr = "Method has no arguments.";
            } else {
                argumentsStr = toJsonStr(arguments);
            }
            log.info("{}-{}, arguments: {}", callerInfo, sequence, argumentsStr);
        }

        long start = System.currentTimeMillis();

        try {
            R result = supplier.get();
            String resultStr = toJsonStr(result);

            if (argResLogAttr.isLogResult()) {
                // 如果结果为null且配置了不打印空，不输出result日志
                if (NULL_STR.equals(resultStr) && argResLogAttr.isIgnoreNullResult()) {
                    // no-op
                } else {
                    log.info("{}-{}, result: {}", callerInfo, sequence, resultStr);
                }
            }
            return result;
        } catch (Throwable throwable) {
            if (argResLogAttr.isLogResult()) {
                if (ArgResLogAttr.ERROR.equals(argResLogAttr.getExceptionLevel())) {
                    log.error("{}-{}, error: {}", callerInfo, sequence, toJsonStr(ExceptionUtil.unwrapThrowable(throwable)));
                } else if (ArgResLogAttr.WARN.equals(argResLogAttr.getExceptionLevel())) {
                    log.warn("{}-{}, error: {}", callerInfo, sequence, toJsonStr(ExceptionUtil.unwrapThrowable(throwable)));
                } else if (ArgResLogAttr.INFO.equals(argResLogAttr.getExceptionLevel())) {
                    log.info("{}-{}, error: {}", callerInfo, sequence, toJsonStr(ExceptionUtil.unwrapThrowable(throwable)));
                } else {
                    throw new ArgResLogException("异常时的日志级别只能是：ERROR、WARN、INFO，当前是：" + argResLogAttr.getExceptionLevel() + "。");
                }
            }
            throw throwable;
        } finally {
            if (argResLogAttr.isLogCost()) {
                log.info("{}-{}, cost: {}", callerInfo, sequence, System.currentTimeMillis() - start);
            }
        }
    }

    private static String toJsonStr(Object o) {
        if (o == null) {
            return NULL_STR;
        }

        if (o instanceof Throwable) {
            Throwable throwable = (Throwable) o;
            return throwable.getMessage();
        }

        return BEJsonUtils.objectToString(o);
    }

    protected interface TSupplier<T> {

        T get() throws Throwable;
    }

    protected static void reportAsRuntimeException(Throwable throwable) {
        throwable = ExceptionUtil.unwrapThrowable(throwable);
        if (!(throwable instanceof ArgResLogException)) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else {
                throw new UndeclaredThrowableException(throwable);
            }
        }

        throw (ArgResLogException) throwable;
    }

}
