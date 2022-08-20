package cn.addenda.se.propertyrefresh;

import java.lang.reflect.Field;

/**
 * @Author ISJINHAO
 * @Date 2022/4/4 10:56
 */
public class PropertyRefreshHolder {

    private String beanName;

    private Field field;

    private String expression;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    private Object oldValue;

    private Object newValue;

    private Object bean;

    private Class<?> beanType;

    private int delay;

    private boolean nullAble;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isNullAble() {
        return nullAble;
    }

    public void setNullAble(boolean nullAble) {
        this.nullAble = nullAble;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public void setBeanType(Class<?> beanType) {
        this.beanType = beanType;
    }

    public String getFieldName() {
        return field == null ? null : field.getName();
    }

    @Override
    public String toString() {
        return "PropertyRefreshHolder{" +
                "beanName='" + beanName + '\'' +
                ", field=" + getFieldName() +
                ", expression='" + expression + '\'' +
                ", oldValue=" + oldValue +
                ", newValue=" + newValue +
                ", delay=" + delay +
                ", nullAble=" + nullAble +
                '}';
    }
}
