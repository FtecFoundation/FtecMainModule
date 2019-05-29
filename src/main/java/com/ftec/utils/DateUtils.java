package com.ftec.utils;

import java.util.Date;

public class DateUtils {

    public final static int DAY = 24*60*60*1000;
    public final static long WEEK = 7*24*60*60*1000;
    public final static long MONTH = 30*24*60*60*1000L;
    public final static long DAY90 = 90*24*60*60*1000L;
    public final static int MINUTES_30 = 30*60*1000;

    public static Date getDateOffset(Date date, int offset) {
        return new Date(date.getTime() + offset);
    }

    public static Date getDateOffset(Date date, long offset) {
        return new Date(date.getTime() + offset);
    }

    public static String getHumanTime(long timestamp, boolean showMilliseconds) {
        String answer = "";
        if (showMilliseconds) answer += timestamp % 1000 + " milliseconds";
        timestamp /= 1000;
        if (timestamp == 0) return answer;
        answer = timestamp % 60 + " seconds " + answer;
        timestamp /= 60;
        if (timestamp == 0) return answer;
        answer = timestamp % 60 + " minutes " + answer;
        timestamp /= 60;
        if (timestamp == 0) return answer;
        answer = timestamp % 24 + " hours " + answer;
        timestamp /= 24;
        if (timestamp == 0) return answer;
        return timestamp + " days " + answer;

    }

}
