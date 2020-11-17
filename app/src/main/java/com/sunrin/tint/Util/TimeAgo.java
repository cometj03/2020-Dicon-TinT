package com.sunrin.tint.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeAgo {

    // How to use : String timeAgo = TimeAgo.getTimeAgo(TIMESTAMP);

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final long WEEK_MILLIS = 7 * DAY_MILLIS;

    public static String getTimeAgo(long millis) {
        // 밀리세컨드로 변환
//        if (time < 1000000000000L) {
//            time *= 1000;
//        }

        long now = System.currentTimeMillis();
        if (millis > now || millis <= 0) {
            return null;
        }

        final long diff = now - millis;

        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 2 * DAY_MILLIS) {
            return "yesterday";
        } else if (diff < 7 * DAY_MILLIS) {
            return diff / DAY_MILLIS + " days ago";
        } else if (diff < 2 * WEEK_MILLIS) {
            return "a week ago";
        } else if (diff < 4 * WEEK_MILLIS) {
            return diff / WEEK_MILLIS + " weeks ago";
        } else {
            return getSimpleDateFormat(millis);
        }
    }

    public static String getTimeAgo(String dateFormat) {
        return getTimeAgo(getTime(dateFormat));
    }

    public static long getTime(String dateFormat) {
        // 2013-09-19 03:27:23+01:00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = format.parse(dateFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date.getTime();
    }

    public static String getDateFormat(long timeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return formatter.format(new Date(timeMillis));
    }

    public static String getSimpleDateFormat(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(new Date(millis));
    }
}
