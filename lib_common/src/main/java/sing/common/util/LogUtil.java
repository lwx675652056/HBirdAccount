package sing.common.util;

import android.util.Log;

public class LogUtil {

    public static boolean isDebug = false;
    public static String TAG = "LogUtils";

    public static void init(boolean isDebug, String TAG) {
        LogUtil.isDebug = isDebug;
        LogUtil.TAG = TAG;
    }

    public static void i(String msg) {
        if (isDebug) {
            print(TAG, msg, 0);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            print(TAG, msg, 1);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            print(TAG, msg, 2);
        }
    }

    public static void v(String msg) {
        if (isDebug) {
            print(TAG, msg, 3);
        }
    }

    public static void w(String msg) {
        if (isDebug) {
            print(TAG, msg, 4);
        }
    }

    // custom TAG
    public static void i(String tag, String msg) {
        if (isDebug) {
            print(tag, msg, 0);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            print(tag, msg, 1);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            print(tag, msg, 2);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            print(tag, msg, 3);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            print(tag, msg, 4);
        }
    }

    private static void print(String tag, String msg, int type) {
        switch (type) {
            case 0:
                Log.i(tag, getLog());
                if (msg.length() > 4000) {
                    for (int i = 0; i < msg.length(); i += 4000) {
                        if (i + 4000 < msg.length()) {
                            Log.i(tag, msg.substring(i, i + 4000));
                        } else {
                            Log.i(tag, msg.substring(i, msg.length()));
                        }
                    }
                } else {
                    Log.i(tag, msg);
                }
                break;
            case 1:
                Log.d(tag, getLog());
                if (msg.length() > 4000) {
                    for (int i = 0; i < msg.length(); i += 4000) {
                        if (i + 4000 < msg.length()) {
                            Log.d(tag, msg.substring(i, i + 4000));
                        } else {
                            Log.d(tag, msg.substring(i, msg.length()));
                        }
                    }
                } else {
                    Log.d(tag, msg);
                }
                break;
            case 2:
                Log.e(tag, getLog());
                if (msg.length() > 4000) {
                    for (int i = 0; i < msg.length(); i += 4000) {
                        if (i + 4000 < msg.length()) {
                            Log.e(tag, msg.substring(i, i + 4000));
                        } else {
                            Log.e(tag, msg.substring(i, msg.length()));
                        }
                    }
                } else {
                    Log.e(tag, msg);
                }
                break;
            case 3:
                Log.v(tag, getLog());
                if (msg.length() > 4000) {
                    for (int i = 0; i < msg.length(); i += 4000) {
                        if (i + 4000 < msg.length()) {
                            Log.v(tag, msg.substring(i, i + 4000));
                        } else {
                            Log.v(tag, msg.substring(i, msg.length()));
                        }
                    }
                } else {
                    Log.v(tag, msg);
                }
                break;
            case 4:
                Log.w(tag, getLog());
                if (msg.length() > 4000) {
                    for (int i = 0; i < msg.length(); i += 4000) {
                        if (i + 4000 < msg.length()) {
                            Log.w(tag, msg.substring(i, i + 4000));
                        } else {
                            Log.w(tag, msg.substring(i, msg.length()));
                        }
                    }
                } else {
                    Log.w(tag, msg);
                }
                break;
            default:
        }
    }


    private static String getLog() {
        StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
        StackTraceElement element = traceElements[5];
        return "打印出处： -> (" + element.getFileName() + ":" + element.getLineNumber() + ")";
    }
}