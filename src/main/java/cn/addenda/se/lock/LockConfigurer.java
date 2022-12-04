package cn.addenda.se.lock;

import cn.addenda.businesseasy.lock.LockService;

/**
 * @author addenda
 * @datetime 2022/11/30 19:21
 */
public class LockConfigurer {

    private final LockService lockService;

    public LockConfigurer(LockService lockService) {
        this.lockService = lockService;
    }

    public LockService getLockService() {
        return lockService;
    }
}
