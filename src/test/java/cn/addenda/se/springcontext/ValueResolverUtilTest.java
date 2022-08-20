package cn.addenda.se.springcontext;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author ISJINHAO
 * @Date 2022/3/2 19:42
 */
public class ValueResolverUtilTest {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SpringContextDeclareConfiguration.class, ValueResolverUtil.class);

        context.refresh();

        ValueResolverUtil valueResolverUtil = context.getBean(ValueResolverUtil.class);
        System.out.println(valueResolverUtil.resolveDollarPlaceholderFromContext("${db.url}"));
        System.out.println(valueResolverUtil.resolveFromContext("${db.url}"));
        System.out.println(valueResolverUtil.resolveHashPlaceholderFromContext("#{db.url}"));
        System.out.println(valueResolverUtil.resolveHashPlaceholderFromContext("#{test}"));
        System.out.println(valueResolverUtil.resolveDollarPlaceholderFromContext("${test}"));

        context.close();

    }

}
