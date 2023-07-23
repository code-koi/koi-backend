package com.codekoi.time.formatter;

public enum TimePattern {
    BASIC_FORMAT(TimePattern.BASIC_FORMAT_STRING);

    public static final String BASIC_FORMAT_STRING = "yyyyMMddHHmmss";

    public final String format;

    TimePattern(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}