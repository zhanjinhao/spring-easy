package cn.addenda.se.utils;

import cn.addenda.se.SpringEasyException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author ISJINHAO
 * @Date 2022/2/7 12:37
 */
public class SeBeanUtil {

    private SeBeanUtil() {
    }

    public static Set<String> getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (checkNull(srcValue)) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames;
    }

    private static boolean checkNull(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return StringUtils.isEmpty(object);
        }
        if (object instanceof Integer) {
            return ((Integer) object) == 0;
        }
        if (object instanceof Long) {
            return ((Long) object) == 0;
        }
        return false;
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target, String... ignoreProperties) {
        Set<String> ignorePropertyNames = getNullPropertyNames(src);
        if (ignoreProperties != null) {
            ignorePropertyNames.addAll(Arrays.stream(ignoreProperties).collect(Collectors.toSet()));
        }
        String[] result = new String[ignorePropertyNames.size()];
        BeanUtils.copyProperties(src, target, ignorePropertyNames.toArray(result));
    }

    /**
     * @param list vital: 集合内部元素的属性必须具有无参构造方法。
     * @return 不会影响集合中的元素
     */
    public static <T> T mergeObject(List<T> list, String... ignoreProperties) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int size = list.size();
        T stand = list.get(0);
        Class<?> aClass = stand.getClass();
        try {
            @SuppressWarnings("all")
            T result = (T) aClass.newInstance();
            BeanUtils.copyProperties(stand, result);
            for (int i = 1; i < size; i++) {
                copyPropertiesIgnoreNull(list.get(i), result, ignoreProperties);
            }
            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SpringEasyException("实例化对象失败，className: " + aClass, e);
        }
    }

    public static <T> T copyProperties(Object source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }

}
