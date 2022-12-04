package cn.addenda.se.lock;

/**
 * @author addenda
 * @datetime 2022/12/1 19:05
 */
public class LockAttribute {

    public static final String DEFAULT_PREFIX = "lock";
    public static final String DEFAULT_KEY_EXTRACTOR = "#this";
    public static final String DEFAULT_LOCK_FAILED_MSG = "数据 [{}] 繁忙，请重试！";

    private int keyArgumentIndex = 0;

    private String prefix;

    private String keyExtractor;

    private String lockFailedMsg;

    public int getKeyArgumentIndex() {
        return keyArgumentIndex;
    }

    public void setKeyArgumentIndex(int keyArgumentIndex) {
        this.keyArgumentIndex = keyArgumentIndex;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getKeyExtractor() {
        return keyExtractor;
    }

    public void setKeyExtractor(String keyExtractor) {
        this.keyExtractor = keyExtractor;
    }

    public String getLockFailedMsg() {
        return lockFailedMsg;
    }

    public void setLockFailedMsg(String lockFailedMsg) {
        this.lockFailedMsg = lockFailedMsg;
    }

    public static class LockAttributeBuilder {

        private int keyArgumentIndex = 0;

        private String prefix;

        private String keyExtractor;

        private String lockFailedMsg;

        public static LockAttributeBuilder newBuilder() {
            return new LockAttributeBuilder();
        }

        public LockAttributeBuilder withKeyArgumentIndex(int keyArgumentIndex) {
            this.keyArgumentIndex = keyArgumentIndex;
            return this;
        }

        public LockAttributeBuilder withPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public LockAttributeBuilder withKeyExtractor(String keyExtractor) {
            this.keyExtractor = keyExtractor;
            return this;
        }

        public LockAttributeBuilder withLockFailedMsg(String lockFailedMsg) {
            this.lockFailedMsg = lockFailedMsg;
            return this;
        }

        public LockAttribute build() {
            LockAttribute lockAttribute = new LockAttribute();
            if (lockFailedMsg == null) {
                lockAttribute.setLockFailedMsg(DEFAULT_LOCK_FAILED_MSG);
            } else {
                lockAttribute.setLockFailedMsg(lockFailedMsg);
            }
            if (keyExtractor == null) {
                lockAttribute.setKeyExtractor(DEFAULT_KEY_EXTRACTOR);
            } else {
                lockAttribute.setKeyExtractor(keyExtractor);
            }
            if (prefix == null) {
                lockAttribute.setPrefix(DEFAULT_PREFIX);
            } else {
                lockAttribute.setPrefix(prefix);
            }
            lockAttribute.setKeyArgumentIndex(keyArgumentIndex);
            return lockAttribute;
        }

    }
}
