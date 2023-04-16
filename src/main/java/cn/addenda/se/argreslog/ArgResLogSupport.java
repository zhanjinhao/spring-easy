package cn.addenda.se.argreslog;

import cn.addenda.businesseasy.util.BEJsonUtils;
import cn.addenda.businesseasy.util.BEStackTraceUtils;
import cn.addenda.se.result.SystemException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
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

    protected static <R> R invoke(ArgResLogAttr argResLogAttr, Object[] arguments, TSupplier<R> supplier, String callerInfo) {
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

    private static <R> R doInvoke(ArgResLogAttr argResLogAttr, Object[] arguments, TSupplier<R> supplier, String callerInfo) {
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
                    log.error("{}-{}, error: {}", callerInfo, sequence, toJsonStr(throwable), throwable);
                } else if (ArgResLogAttr.WARN.equals(argResLogAttr.getExceptionLevel())) {
                    log.warn("{}-{}, error: {}", callerInfo, sequence, toJsonStr(throwable));
                } else if (ArgResLogAttr.INFO.equals(argResLogAttr.getExceptionLevel())) {
                    log.info("{}-{}, error: {}", callerInfo, sequence, toJsonStr(throwable));
                } else {
                    throw warp(new SystemException("unexpected exception!"));
                }
            }
            // get()时的异常时不确定的，可能是Business异常，可能是其他的RuntimeException，也可能是受查的异常，
            // 这里统一warp成ArgResLogException，在invoke()的调用者处需要再将真实的异常unWrap出来。
            throw warp(throwable);
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

    /**
     * 这种方法输出的字符串json短，但是不方便反序列化为对象。<p/>
     * 先保留着，后续看看有没有用武之地。
     */
    private static String toStr(Object o) {
        if (o == null) {
            return NULL_STR;
        } else if (o instanceof Collection) {
            Collection<?> collection = (Collection<?>) o;
            return collection.stream().map(ArgResLogSupport::toStr).collect(Collectors.joining(",", "[", "]"));
        } else if (o.getClass().isArray()) {
            // A 是 B 的子类，则 A[] 是 B[] 的子类；
            // 所以 o 可以转换为 Object[]
            Object[] array = (Object[]) o;
            return Arrays.stream(array).map(ArgResLogSupport::toStr).collect(Collectors.joining(",", "[", "]"));
        } else if (o instanceof Map.Entry) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            return toStr(entry.getKey()) + "=" + toStr(entry.getValue());
        } else if (o instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) o;
            return "{" + map.entrySet().stream().map(ArgResLogSupport::toStr).collect(Collectors.joining(",")) + "}";
        } else if (o instanceof Throwable) {
            Throwable throwable = (Throwable) o;
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
        return o.toString();
    }

    protected interface TSupplier<T> {

        T get() throws Throwable;
    }

    protected static ArgResLogException warp(Throwable throwable) {
        return new ArgResLogException(throwable);
    }

    protected static Throwable unWarp(ArgResLogException argResLogException) {
        Throwable throwable = argResLogException;
        while (throwable instanceof ArgResLogException) {
            throwable = throwable.getCause();
        }
        return throwable;
    }

}
