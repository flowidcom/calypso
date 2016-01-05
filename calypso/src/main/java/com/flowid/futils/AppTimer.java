package com.flowid.futils;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A timer, used to track performance information. Stores the information in the
 * log in a specific format.
 * 
 * |Id|Thread Name|start|
 */
public class AppTimer {
    // the information is stored here.
    static private final Logger logger = LoggerFactory.getLogger(AppTimer.class);
    static public final String START = "start";
    static public final String STOP = "stop";
    static public final String TIMER_MARKER = "||"; // this is at the end of the line for timer log entries

    // timer id
    private final String id;

    // time when the last event occurred
    private long lastEventTime = 0;

    // when the timer started
    private long startTime = 0;

    SimpleDateFormat PERF_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Initialize the timer. The default id is _."
     *
     * @param c      the class
     * @param method the method
     */
    private AppTimer(Class<?> c, String method) {
        this(c.getSimpleName() + "." + method);
    }

    /**
     * Initialize the timer.
     *
     * @param name
     */
    private AppTimer(String id) {
        this.id = id;
    }

    static public AppTimer timer(Class<?> c, String method) {
        return new AppTimer(c, method);
    }

    /**
     * Log an event associated with the timer.
     *
     * @param msg
     */
    public AppTimer event(String msg, Object... args) {
        long now = System.currentTimeMillis();
        if (startTime == 0) { // this is the start event
            startTime = now;
            lastEventTime = now;
        }

        long sinceLastEvent = now - this.lastEventTime;
        lastEventTime = now;
        long sinceStart = now - startTime;
        log(msg, sinceStart, sinceLastEvent, args);
        return this;
    }

    /**
     * Return the time (in milliseconds) between the start and the last event.
     * @return elapsed time in milliseconds.
     */
    public int getElapsedTime() {
        return (int) (lastEventTime - startTime);
    }

    /**
     * Add an entry to the log. Utility method used by start, stop and event.
     * The marker of the end of line is two vertical bars (||)
     *
     * @param msg
     * @param sinceStart
     * @param sinceLastEvent
     * @param currentTimeSecs
     */
    private void log(String msg, long sinceStart, long sinceLastEvent, Object... args) {
        StringBuilder sb = new StringBuilder(msg);
        if (args.length > 0) {
            sb.append(Arrays.toString(args));
        }
        logger.info("|" + id + "|" + sb.toString() + "|" + sinceStart + "|" + sinceLastEvent
                + "|" + Thread.currentThread().getName() + "|" + startTime + TIMER_MARKER);
    }

    /**
     * Start the timer.
     */
    public AppTimer start(Object... args) {
        return event(START, args);
    }

    /**
     * stop the timer.
     */
    public AppTimer stop(Object... args) {
        return event(STOP, args);
    }
}
