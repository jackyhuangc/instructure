package com.jacky.strive.common;


public class ExceptionUtil {
    public static String getString(Throwable throwable) {
        return getString(throwable, 20);
    }

    public static String getString(Throwable throwable, int dept) {
        if (throwable == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(2048);
        sb.append(String.format("message:%s ", throwable.getMessage()));
        sb.append(System.lineSeparator());
        sb.append("StackTrace:");
        sb.append(System.lineSeparator());
        sb.append(getStackTraceString(throwable, dept));
        sb.append(System.lineSeparator());
        if (throwable.getCause() != null) {
            sb.append("内部异常:");
            sb.append(getString(throwable.getCause(), dept));
        }
        return sb.toString();
    }


    public static String getStackTraceString(Throwable throwable, int dept) {
        return getStackTraceString(throwable.getStackTrace(), dept);
    }

    public static String getStackTraceString(StackTraceElement[] stacks, int dept) {
        StringBuffer sb = new StringBuffer(1024);

        for (int i = 0; i < Math.min(stacks.length, dept); i++) {
            StackTraceElement ste = stacks[i];
            sb.append(ste.toString());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static String getMessage(Throwable throwable) {
        String msg = throwable.getMessage();
        if (msg == null
                && throwable.getCause() != null) {
            msg = getMessage(throwable.getCause());
        }
        if (msg == null) {
            msg = throwable.toString();
        }
        return msg;
    }


}
