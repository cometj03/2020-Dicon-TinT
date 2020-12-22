package com.sunrin.tint.Util;

import android.annotation.SuppressLint;
import android.content.res.Resources;

import com.bumptech.glide.load.engine.Resource;
import com.sunrin.tint.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    // How to use : String timeAgo = TimeAgo.getTimeAgo(TIMESTAMP);

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final long WEEK_MILLIS = 7 * DAY_MILLIS;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String getTimeAgo(long millis, Resources res) {
        // TODO: Refactor with Resource - StringArray

        // 밀리세컨드로 변환
//        if (time < 1000000000000L) {
//            time *= 1000;
//        }

        long now = System.currentTimeMillis();
        if (millis > now || millis <= 0) {
            return null;
        }

        final long diff = now - millis;
        String[] timeLapse = res.getStringArray(R.array.TimeLapse);

        if (diff < MINUTE_MILLIS) {
            return timeLapse[0];                        // 방금 전
        } else if (diff < 2 * MINUTE_MILLIS) {
            return timeLapse[1];                        // 1분 전
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + timeLapse[2]; // 분 전
        } else if (diff < 90 * MINUTE_MILLIS) {
            return timeLapse[3];                        // 1시간 전
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + timeLapse[4];   // 시간 전
        } else if (diff < 2 * DAY_MILLIS) {
            return timeLapse[5];                        // 어제
        } else if (diff < 7 * DAY_MILLIS) {
            return diff / DAY_MILLIS + timeLapse[6];    // 일 전
        } else if (diff < 2 * WEEK_MILLIS) {
            return timeLapse[7];                        // 1주 전
        } else if (diff < 4 * WEEK_MILLIS) {
            return diff / WEEK_MILLIS + timeLapse[8];   // 주 전
        } else {
            return getSimpleDateFormat(millis);
        }
    }

    public static String getTimeAgo(String dateFormat, Resources res) {
        return getTimeAgo(getTime(dateFormat), res);
    }

    public static long getTime(String dateFormat) {
        // 2013-09-19 03:27:23+01:00
        Date date = new Date();
        try {
            date = DATE_FORMAT.parse(dateFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getDateFormat(long timeMillis) {
        return DATE_FORMAT.format(new Date(timeMillis));
    }

    public static String getDateFormat() {
        return getDateFormat(System.currentTimeMillis());
    }

    public static String getSimpleDateFormat(long millis) {
        return SIMPLE_DATE_FORMAT.format(new Date(millis));
    }
}
