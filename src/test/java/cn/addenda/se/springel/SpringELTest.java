package cn.addenda.se.springel;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author addenda
 * @datetime 2022/11/30 19:54
 */
public class SpringELTest {

    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();

        StandardEvaluationContext teslaContext = new StandardEvaluationContext();

        String invention = parser.parseExpression("inventions[3]").getValue(
                teslaContext, String.class);

        StandardEvaluationContext societyContext = new StandardEvaluationContext();

        String name = parser.parseExpression("Members[0].Name").getValue(
                societyContext, String.class);

//        String invention = parser.parseExpression("Members[0].Inventions[6]").getValue(
//                societyContext, String.class);
    }

}
