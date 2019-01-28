package com.templateengine.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This util class parses Exception message
 *
 */
public class ExceptionUtils {

    public static String getExceptionAsString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

}
