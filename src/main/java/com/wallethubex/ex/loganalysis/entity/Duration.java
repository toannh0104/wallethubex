package com.wallethubex.ex.loganalysis.entity;


import org.apache.commons.cli.ParseException;

public enum Duration {
    DAILY("daily"),
    HOURLY("hourly");

    private String value;

    Duration(String value) {
        this.value = value;
    }

    public static Duration parseDuration(String value) throws ParseException {
        if (HOURLY.value.equalsIgnoreCase(value)) {
            return HOURLY;
        } else if (DAILY.value.equals(value)) {
            return DAILY;
        } else {
            throw new ParseException("Duration invalid: " + value);
        }
    }
}