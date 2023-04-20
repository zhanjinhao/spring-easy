package cn.addenda.se.argreslog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author addenda
 * @datetime 2023/3/9 10:17
 */
@Setter
@Getter
@ToString
public class ArgResLogAttr {

    public static final String ERROR = "ERROR";
    public static final String INFO = "INFO";
    public static final String WARN = "WARN";

    private boolean logParam;
    private boolean logResult;
    private boolean logCost;
    private boolean ignoreNullResult;
    private String exceptionLevel;

    public static class ArgResLogAttrBuilder {

        private boolean logParam = true;
        private boolean logResult = true;
        private boolean logCost = true;
        private boolean ignoreNullResult = false;
        private String exceptionLevel;

        public static ArgResLogAttrBuilder newBuilder() {
            return new ArgResLogAttrBuilder();
        }

        public ArgResLogAttrBuilder withLogParam(boolean logParam) {
            this.logParam = logParam;
            return this;
        }

        public ArgResLogAttrBuilder withLogResult(boolean logResult) {
            this.logResult = logResult;
            return this;
        }

        public ArgResLogAttrBuilder withIgnoreNullResult(boolean ignoreNullResult) {
            this.ignoreNullResult = ignoreNullResult;
            return this;
        }

        public ArgResLogAttrBuilder withLogCost(boolean logCost) {
            this.logCost = logCost;
            return this;
        }

        public ArgResLogAttrBuilder withExceptionLevel(String exceptionLevel) {
            if (ERROR.equals(exceptionLevel)
                || INFO.equals(exceptionLevel) || WARN.equals(exceptionLevel)) {
                this.exceptionLevel = exceptionLevel;
                return this;
            }
            throw new ArgResLogException("异常时的日志级别只能是：ERROR、WARN、INFO，当前是：" + exceptionLevel + "。");
        }

        public ArgResLogAttr build() {
            ArgResLogAttr argResLogAttr = new ArgResLogAttr();
            argResLogAttr.setLogParam(logParam);
            argResLogAttr.setLogResult(logResult);
            argResLogAttr.setLogCost(logCost);
            argResLogAttr.setIgnoreNullResult(ignoreNullResult);
            if (exceptionLevel == null) {
                argResLogAttr.setExceptionLevel(ERROR);
            } else {
                argResLogAttr.setExceptionLevel(exceptionLevel);
            }
            return argResLogAttr;
        }

    }

    public static ArgResLogAttr defaultAttr() {
        return ArgResLogAttrBuilder.newBuilder().withExceptionLevel(ArgResLogAttr.ERROR).build();
    }

}
