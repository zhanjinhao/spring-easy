package cn.addenda.se.paramreslog;

import cn.addenda.businesseasy.util.BEJsonUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @datetime 2022/9/29 13:51
 */
public class ParamResLogMethodInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ParamResLogMethodInterceptor.class);

    private static final Map<String, AtomicLong> SEQUENCE_GENERATOR_MAP = new ConcurrentHashMap<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = AopUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass());
        ParamResLoggable paramResLoggable = AnnotationUtils.findAnnotation(method, ParamResLoggable.class);
        if (paramResLoggable == null) {
            return invocation.proceed();
        }

        // e.g. ParamResLogMethodInterceptor.invoke、ParamResLogMethodInterceptor.hashCode
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        long sequence = SEQUENCE_GENERATOR_MAP.computeIfAbsent(methodName, s -> new AtomicLong(0L)).getAndDecrement();

        Object[] arguments = invocation.getArguments();

        String argumentsStr;
        if (paramResLoggable.logParam()) {
            if (arguments.length == 0) {
                argumentsStr = "Method has no parameters.";
            } else {
                argumentsStr = toJsonStr(arguments);
            }
            logger.info("{}-{}, parameter: {}", methodName, sequence, argumentsStr);
        }

        long start = System.currentTimeMillis();

        try {
            Object result = invocation.proceed();
            String resultStr = toJsonStr(result);

            if (paramResLoggable.logReturn()) {
                logger.info("{}-{}, result: {}", methodName, sequence, resultStr);
            }
            return result;
        } catch (Throwable throwable) {
            if (paramResLoggable.logReturn()) {
                logger.info("{}-{}, error: {}", methodName, sequence, toJsonStr(throwable));
            }
            throw throwable;
        } finally {
            if (paramResLoggable.logCost()) {
                logger.info("{}-{}, cost: {}", methodName, sequence, System.currentTimeMillis() - start);
            }
        }
    }

    private String toJsonStr(Object o) {
        if (o == null) {
            return "NIL";
        }

        if (o instanceof Throwable) {
            Throwable throwable = (Throwable) o;
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }

        return BEJsonUtils.objectToString(o);
    }

    private String toStr(Object o) {
        if (o == null) {
            return "NIL";
        } else if (o instanceof Collection) {
            Collection<?> collection = (Collection<?>) o;
            return collection.stream().map(this::toStr).collect(Collectors.joining(",", "[", "]"));
        } else if (o.getClass().isArray()) {
            // A 是 B 的子类，则 A[] 是 B[] 的子类；
            // 所以 o 可以转换为 Object[]
            Object[] array = (Object[]) o;
            return Arrays.stream(array).map(this::toStr).collect(Collectors.joining(",", "[", "]"));
        } else if (o instanceof Map.Entry) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            return toStr(entry.getKey()) + "=" + toStr(entry.getValue());
        } else if (o instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) o;
            return "{" + map.entrySet().stream().map(this::toStr).collect(Collectors.joining(",")) + "}";
        } else if (o instanceof Throwable) {
            Throwable throwable = (Throwable) o;
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
        return o.toString();
    }

}
