package com.jivesoftware.jivesdk.impl.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 21/5/13
 * Time: 3:48 PM
 */
public interface DealRoomLogger {
    void debug(Object message);

    void debug(Object message, Throwable t);

    void error(Object message);

    void error(Object message, Throwable t);

    void warn(Object message, Throwable t);

    void warn(Object message);

    void incAtomic(String name);

    void incAtomic(String name, long amount);

    void startTimer(String name);

    long stopTimer(String name);

    void info(Object message, Throwable t);

    void info(Object message);

    void set(String name, long value);

    void setAtomic(String name, long value);

    boolean isDebugEnabled();
}
