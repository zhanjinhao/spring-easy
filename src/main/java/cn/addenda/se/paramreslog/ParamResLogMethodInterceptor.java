package cn.addenda.se.paramreslog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author addenda
 * @datetime 2022/9/29 13:51
 */
public class ParamResLogMethodInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ParamResLogMethodInterceptor.class);

    private static final AtomicLong SEQUENCE_GENERATOR = new AtomicLong(0L);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        String argumentsStr;
        if (arguments.length == 0) {
            argumentsStr = "Method has no parameters.";
        } else {
            argumentsStr = toStr(arguments);
        }
        long sequence = SEQUENCE_GENERATOR.getAndDecrement();
        logger.info("{}, parameter: {}", sequence, argumentsStr);

        try {
            Object result = invocation.proceed();
            String resultStr = toStr(result);
            logger.info("{}, result: {}", sequence, resultStr);
            return result;
        } catch (Throwable throwable) {
            logger.info("{}, error: {}", sequence, toStr(throwable));
            throw throwable;
        }

    }

    private String toStr(Object o) {
        if (o == null) {
            return "null";
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
