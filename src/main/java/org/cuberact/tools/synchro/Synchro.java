package org.cuberact.tools.synchro;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;

public class Synchro {

    public static void readLock(ReadWriteLock lock, Runnable runnable) {
        lock.readLock().lock();
        try {
            runnable.run();
        } catch (Throwable t) {
            throw new SynchroException(t);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static <T> T readLock(ReadWriteLock lock, Callable<T> callable) {
        lock.readLock().lock();
        try {
            return callable.call();
        } catch (Throwable t) {
            throw new SynchroException(t);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void writeLock(ReadWriteLock lock, Runnable runnable) {
        lock.writeLock().lock();
        try {
            runnable.run();
        } catch (Throwable t) {
            throw new SynchroException(t);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static <T> T writeLock(ReadWriteLock lock, Callable<T> callable) {
        lock.writeLock().lock();
        try {
            return callable.call();
        } catch (Throwable t) {
            throw new SynchroException(t);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static class SynchroException extends RuntimeException {
        private SynchroException(Throwable cause) {
            super(cause);
        }
    }

}
