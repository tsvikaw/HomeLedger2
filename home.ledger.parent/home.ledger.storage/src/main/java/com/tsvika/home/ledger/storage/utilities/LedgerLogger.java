package com.tsvika.home.ledger.storage.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class LedgerLogger {

    private static final String CLOSER = " : {}";
    private static String format;
    private static String formatError;
    private static String formatWarn;

    /**
     * Private Constructor
     */

    private LedgerLogger(){}


    static  {
        LedgerLogger.format = "[CPC log] CPC_Automation : {}";
        LedgerLogger.formatError = "[CPC log *** OOPS! *** !] : {}";
        LedgerLogger.formatWarn = "[CPC log *** BEWARE... *** !] : {}";
    }

    /**
     * info
     *
     * @param clazz
     * @param msg
     */
    public static void info(final Class<?> clazz, final String msg) {

        Logger logger = getInfoLogger(clazz);
        if (Objects.nonNull(logger) && logger.isInfoEnabled()) {
            logger.info(LedgerLogger.format, msg);
        }
    }


    /**
     * info
     *
     * @param name
     * @param obj
     */
    public static void info(final String name, final Object obj) {

        Logger logger = getInfoLogger(name);
        if (Objects.nonNull(logger) && logger.isInfoEnabled()) {
            logger.info(LedgerLogger.format, obj);
        }
    }

    /**
     * info
     *
     * @param obj
     */
    public static void info(final Class<?> clazz, final Object obj) {
        Logger logger = getInfoLogger(clazz);
        if (Objects.nonNull(logger) && logger.isInfoEnabled()) {
            logger.info(LedgerLogger.format, obj);
        }
    }

    /**
     * debug
     *
     * @param clazz
     * @param obj
     */
    public static void debug(final Class<?> clazz, final Object obj) {
        Logger logger = getDebugLogger(clazz);
        if (Objects.nonNull(logger) && logger.isDebugEnabled()) {
            logger.debug(LedgerLogger.format, obj);
        }
    }

    /**
     * debug
     *
     * @param name
     * @param obj
     */
    public static void debug(final String name, final Object obj) {
        Logger logger = getDebugLogger(name);
        if (Objects.nonNull(logger) && logger.isDebugEnabled()) {
            logger.debug(LedgerLogger.format, obj);
        }
    }

    /**
     * debug
     *
     * @param clazz
     * @param ex
     */
    public static void debug(final Class<?> clazz, final Throwable ex) {
        Logger logger = getDebugLogger(clazz);
        if (Objects.nonNull(logger) && logger.isDebugEnabled()) {
            logger.debug(LedgerLogger.format, ex);
        }
    }

    /**
     * warn
     *
     * @param clazz
     * @param obj
     */
    public static void warn(final Class<?> clazz, final Object obj) {
        Logger logger = getWarnLogger(clazz);
        if (Objects.nonNull(logger) && logger.isWarnEnabled()) {
            logger.warn(LedgerLogger.formatWarn, obj);
        }

    }

    /**
     * warn
     *
     * @param name
     * @param obj
     */
    public static void warn(final String name, final Object obj) {
        Logger logger = getWarnLogger(name);
        if (Objects.nonNull(logger) && logger.isWarnEnabled()) {
            logger.warn(LedgerLogger.formatWarn, obj);
        }
    }

    /**
     * error
     *
     * @param clazz
     * @param obj
     */
    public static void error(final Class<?> clazz, final Object obj) {
        Logger logger = getErrorLogger(clazz);
        if (Objects.nonNull(logger) && logger.isErrorEnabled()) {
            logger.error(LedgerLogger.formatError, obj);
        }

    }

    /**
     * error
     *
     * @param name
     * @param obj
     */
    public static void error(final String name, final Object obj) {
        Logger logger = getErrorLogger(name);
        if (Objects.nonNull(logger) && logger.isErrorEnabled()) {
            logger.error(LedgerLogger.formatError, obj);
        }
    }

    /**
     * error
     *
     * @param clazz
     * @param t
     */
    public static void error(final Class<?> clazz, final Throwable t) {
        Logger logger = getErrorLogger(clazz);
        if (Objects.nonNull(logger) && logger.isErrorEnabled()) {
            logger.error(LedgerLogger.formatError, t);
        }

    }

    /**
     * error
     *
     * @param clazz
     * @param mess
     * @param t
     */
    public static void error(final Class<?> clazz, String mess,
                             final Throwable t) {
        Logger logger = getErrorLogger(clazz);
        if (Objects.nonNull(logger) && logger.isErrorEnabled()) {
            logger.error(LedgerLogger.formatError, mess, t);
        }

    }

    /**
     * error
     *
     * @param name
     * @param t
     */
    public static void error(final String name, final Throwable t) {
        Logger logger = getErrorLogger(name);
        if (Objects.nonNull(logger) && logger.isErrorEnabled()) {
            logger.error(LedgerLogger.formatError, t);
        }
    }

    /**
     * error
     *
     * @param name
     * @param mess
     * @param t
     */
    public static void error(final String name, final String mess,
                             final Throwable t) {
        Logger logger = getErrorLogger(name);
        if (Objects.nonNull(logger) && logger.isErrorEnabled()) {
            logger.error(LedgerLogger.formatError, mess, t);
        }
    }

    /**
     * trace
     *
     * @param clazz
     * @param obj
     */
    public static void trace(final Class<?> clazz, final Object obj) {
        Logger logger = getTraceLogger(clazz);
        if (Objects.nonNull(logger) && logger.isTraceEnabled()) {
            logger.trace(LedgerLogger.format, obj);
        }
    }

    /**
     * trace
     *
     * @param name
     * @param obj
     */
    public static void trace(final String name, final Object obj) {
        Logger logger = getTraceLogger(name);
        if (Objects.nonNull(logger) && logger.isTraceEnabled()) {
            logger.trace(LedgerLogger.format, obj);
        }
    }


    private static Logger getErrorLogger(String key) {
        Logger logger = LoggerFactory.getLogger(key);

        return Objects.nonNull(logger) ? getErrorLoggerIfEnabled(logger) : null;
    }

    private static Logger getErrorLoggerIfEnabled(Logger logger) {
        return logger.isErrorEnabled() ? logger : null;
    }

    private static Logger getWarnLogger(String key) {
        Logger logger = LoggerFactory.getLogger(key);

        return Objects.nonNull(logger) ? getWarnLoggerIfEnabled(logger) : null;
    }

    private static Logger getWarnLoggerIfEnabled(Logger logger) {
        return logger.isWarnEnabled() ? logger : null;
    }

    private static Logger getDebugLogger(String key) {
        Logger logger = LoggerFactory.getLogger(key);

        return Objects.nonNull(logger) ? getDebugLoggerIfEnabled(logger) : null;
    }

    private static Logger getDebugLoggerIfEnabled(Logger logger) {
        return logger.isDebugEnabled() ? logger : null;
    }

    private static Logger getTraceLogger(String key) {
        Logger logger = LoggerFactory.getLogger(key);

        return Objects.nonNull(logger) ? getTraceLoggerIfEnabled(logger) : null;
    }

    private static Logger getTraceLoggerIfEnabled(Logger logger) {
        return logger.isTraceEnabled() ? logger : null;
    }

    private static Logger getInfoLogger(String key) {
        Logger logger = LoggerFactory.getLogger(key);

        return Objects.nonNull(logger) ? getInfoLoggerIfEnabled(logger) : null;
    }

    private static Logger getInfoLoggerIfEnabled(Logger logger) {
        return logger.isInfoEnabled() ? logger : null;
    }

    private static Logger getErrorLogger(Class cls) {
        Logger logger = LoggerFactory.getLogger(cls);

        return Objects.nonNull(logger) ? (getErrorLoggerIfEnabled(logger)) : null;
    }

    private static Logger getWarnLogger(Class cls) {
        Logger logger = LoggerFactory.getLogger(cls);

        return Objects.nonNull(logger) ? (getWarnLoggerIfEnabled(logger)) : null;
    }

    private static Logger getDebugLogger(Class cls) {
        Logger logger = LoggerFactory.getLogger(cls);

        return Objects.nonNull(logger) ? (getDebugLoggerIfEnabled(logger)) : null;
    }

    private static Logger getTraceLogger(Class cls) {
        Logger logger = LoggerFactory.getLogger(cls);

        return Objects.nonNull(logger) ? (getTraceLoggerIfEnabled(logger)) : null;
    }

    private static Logger getInfoLogger(Class cls) {
        Logger logger = LoggerFactory.getLogger(cls);

        return Objects.nonNull(logger) ? (getInfoLoggerIfEnabled(logger)) : null;
    }

}

