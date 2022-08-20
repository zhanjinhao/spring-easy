package cn.addenda.se.transaction;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.util.ArrayList;

/**
 * 兼容 @Transactional 全部属性
 */
public class TransactionAttributeBuilder {

    private String transactionManager = "";
    private Propagation propagation = Propagation.REQUIRED;
    private Isolation isolation = Isolation.DEFAULT;
    private int timeout = TransactionDefinition.TIMEOUT_DEFAULT;
    private boolean readOnly = false;
    private Class<? extends Throwable>[] rollbackFor;
    private String[] rollbackForClassName;
    private Class<? extends Throwable>[] noRollbackFor;
    private String[] noRollbackForClassName;

    public static TransactionAttributeBuilder newBuilder() {
        return new TransactionAttributeBuilder();
    }

    private TransactionAttributeBuilder() {

    }

    public TransactionAttributeBuilder withTransactionManager(String transactionManager) {
        this.transactionManager = transactionManager;
        return this;
    }

    public TransactionAttributeBuilder withPropagation(Propagation propagation) {
        this.propagation = propagation;
        return this;
    }

    public TransactionAttributeBuilder withIsolation(Isolation isolation) {
        this.isolation = isolation;
        return this;
    }

    public TransactionAttributeBuilder withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public TransactionAttributeBuilder readOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    public TransactionAttributeBuilder rollbackFor(Class<? extends Throwable>... classes) {
        this.rollbackFor = classes;
        return this;
    }

    public TransactionAttributeBuilder rollbackForWithName(String... classeNames) {
        this.rollbackForClassName = classeNames;
        return this;
    }

    public TransactionAttributeBuilder noRollbackFor(Class<? extends Throwable>... classes) {
        this.noRollbackFor = classes;
        return this;
    }

    public TransactionAttributeBuilder noRollbackForWithName(String... classeNames) {
        this.noRollbackForClassName = classeNames;
        return this;
    }

    public TransactionAttribute build() {
        RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
        rbta.setPropagationBehavior(propagation.value());
        rbta.setIsolationLevel(isolation.value());
        rbta.setTimeout(timeout);
        rbta.setReadOnly(readOnly);
        rbta.setQualifier(transactionManager);
        ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList<RollbackRuleAttribute>();
        if (rollbackFor != null) {
            for (Class<?> rbRule : rollbackFor) {
                RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
                rollBackRules.add(rule);
            }
        }

        if (rollbackForClassName != null) {
            for (String rbRule : rollbackForClassName) {
                RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
                rollBackRules.add(rule);
            }
        }

        if (noRollbackFor != null) {
            for (Class<?> rbRule : noRollbackFor) {
                NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
                rollBackRules.add(rule);
            }
        }

        if (noRollbackForClassName != null) {
            for (String rbRule : noRollbackForClassName) {
                NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
                rollBackRules.add(rule);
            }
        }

        rbta.getRollbackRules().addAll(rollBackRules);
        return rbta;
    }
}