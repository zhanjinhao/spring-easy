package cn.addenda.se.springcontext;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringValueResolver;

import java.util.Properties;

/**
 * @Author ISJINHAO
 * @Date 2022/3/2 19:28
 */
public class ValueResolverUtil implements EmbeddedValueResolverAware {

    private static final String DOLLAR_PREFIX = "${";
    private static final String DOLLAR_SUFFIX = "}";

    private static final String HASH_PREFIX = "#{";
    private static final String HASH_SUFFIX = "}";

    private static final PropertyPlaceholderHelper dollarHelper = new PropertyPlaceholderHelper(DOLLAR_PREFIX, DOLLAR_SUFFIX);
    private static final PropertyPlaceholderHelper hashHelper = new PropertyPlaceholderHelper(HASH_PREFIX, HASH_SUFFIX);

    private StringValueResolver stringValueResolver;

    public static String resolveDollarPlaceholder(String str, Properties properties) {
        return dollarHelper.replacePlaceholders(str, properties);
    }

    public static String resolveHashPlaceholder(String str, Properties properties) {
        return hashHelper.replacePlaceholders(str, properties);
    }

    public String resolveDollarPlaceholderFromContext(String str) {
        return dollarHelper.replacePlaceholders(str, new PropertyPlaceholderHelper.PlaceholderResolver() {
            @Override
            public String resolvePlaceholder(String placeholderName) {
                return stringValueResolver.resolveStringValue(DOLLAR_PREFIX + placeholderName + DOLLAR_SUFFIX);
            }
        });
    }

    public String resolveHashPlaceholderFromContext(String str) {
        return hashHelper.replacePlaceholders(str, new PropertyPlaceholderHelper.PlaceholderResolver() {
            @Override
            public String resolvePlaceholder(String placeholderName) {
                return stringValueResolver.resolveStringValue(DOLLAR_PREFIX + placeholderName + DOLLAR_SUFFIX);
            }
        });
    }

    public String resolveFromContext(String key) {
        if (key == null || key.length() == 0) {
            return null;
        }
        return stringValueResolver.resolveStringValue(key);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.stringValueResolver = resolver;
    }

}
