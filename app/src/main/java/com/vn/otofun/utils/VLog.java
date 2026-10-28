package com.vn.otofun.utils;

import android.util.Log;
import com.vn.otofun.BuildConfig;

/**
 * Created by AnhVT on 12/31/15.
 */
public class VLog {

    /*
     * l method used to log passed string and returns the
     * calling file as the tag, method and line number prior
     * to the string's message
     */
    public static void d(String s) {
        if (BuildConfig.IS_TEST) {
            String[] msg = trace(Thread.currentThread().getStackTrace(), 3);
            Log.d(Configs.TAG_LOG + " " + msg[0], msg[1] + s);
        } else {
            return;
        }
    }

    /*
     * l (tag, string)
     * used to pass logging messages as normal but can be disabled
     * when debug == false
     */
    public static void d(String t, String s) {
        if (BuildConfig.IS_TEST) {
            d(s);
        } else {
            return;
        }
    }

    /*
     * trace
     * Gathers the calling file, method, and line from the stack
     * returns a string array with element 0 as file name and
     * element 1 as method[line]
     */
    public static String[] trace(final StackTraceElement e[], final int level) {
        if (e != null && e.length >= level) {
            final StackTraceElement s = e[level];
            if (s != null) { return new String[] {
                    e[level].getFileName(), e[level].getMethodName() + "[" + e[level].getLineNumber() + "]"
            };}
        }
        return null;
    }

}
