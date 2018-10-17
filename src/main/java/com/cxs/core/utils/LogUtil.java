package com.cxs.core.utils;

import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author ChenXS
 * 日志打印工具类
 */
@SuppressWarnings({"unchecked", "unused"})
public class LogUtil {
    private static Logger log = Logger.getLogger(LogUtil.class);

    /**
     * 不允许实例化
     */
    private LogUtil() {
    }

    public static void warn(Object obj) {
        try {
            String location;
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            location = stacks[2].getClassName() + "." + stacks[2].getMethodName() + "(" + stacks[2].getLineNumber()
                    + ")";

            if (obj instanceof Exception) {
                Exception e = (Exception) obj;
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                log.warn(location + str);
                return;
            }
            log.warn(location + obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void debug(Object obj) {
        try {
            String location;
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            location = stacks[2].getClassName() + "." + stacks[2].getMethodName() + "(" + stacks[2].getLineNumber()
                    + ")";

            if (obj instanceof Exception) {
                Exception e = (Exception) obj;
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                log.debug(location + str);
                return;
            }
            log.debug(location + obj.toString());
        } catch (Exception localException1) {
            localException1.printStackTrace();
        }
    }

    public static void info(Object obj) {
        try {
            String location;
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            location = stacks[2].getClassName() + "." + stacks[2].getMethodName() + "(" + stacks[2].getLineNumber()
                    + ")";

            if (obj instanceof Exception) {
                Exception e = (Exception) obj;
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                log.info(location + str);
                return;
            }
            log.info(location + obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void error(Object obj) {
        try {
            String location;
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            location = stacks[2].getClassName() + "." + stacks[2].getMethodName() + "(" + stacks[2].getLineNumber()
                    + ")";

            if (obj instanceof Exception) {
                Exception e = (Exception) obj;
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                log.error(location + str);
                return;
            }
            log.error(location + obj.toString());
        } catch (Exception localException1) {
            localException1.printStackTrace();
        }
    }
}
