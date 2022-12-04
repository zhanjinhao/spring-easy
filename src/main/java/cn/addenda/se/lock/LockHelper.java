package cn.addenda.se.lock;

/**
 * @author addenda
 * @datetime 2022/12/1 18:55
 */
public class LockHelper extends LockAspectSupport {

    public <R> R doLock(LockAttribute attribute, LockExecutor<R> executor, Object... arguments) {
        if (arguments == null || arguments.length == 0) {
            throw new LockException("参数不能为空！");
        }
        return (R) invokeWithinLock(attribute, arguments, executor::process);
    }

    public interface LockExecutor<R> {
        R process() throws Throwable;
    }

    public interface VoidLockExecutor {
        void process() throws Throwable;
    }

}
