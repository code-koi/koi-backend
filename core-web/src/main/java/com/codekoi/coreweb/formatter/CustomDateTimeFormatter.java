package com.codekoi.coreweb.formatter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CustomDateTimeFormatter {

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 7;
        public static final int WEEK = 4;
        public static final int MONTH = 12;
    }

    public static String beforeTimeFormat(LocalDateTime localDateTime) {
        long diffTime = (Timestamp.valueOf(LocalDateTime.now()).getTime() - Timestamp.valueOf(localDateTime).getTime()) /1000;

        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전";                 // sec
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            msg = diffTime + "분 전";         // min
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            msg = (diffTime) + "시간 전";     // hour
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";     // day
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.WEEK) {
            msg = (diffTime) + "주 전";     // week
        } else if ((diffTime /= TIME_MAXIMUM.WEEK) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";     // month
        } else {
            diffTime /= TIME_MAXIMUM.MONTH;
            msg = (diffTime) + "년 전";     // year
        }
        return msg;
    }
}
