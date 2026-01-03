package com.aayvyas;

import java.util.regex.PatternSyntaxException;

public record Request(String ipAddr, Time time, Status status) implements Comparable<Request> {
    @Override
    public int compareTo(Request o) {
        return this.time.timeInMins - o.time.timeInMins;
    }


    static class Time {
        final Integer timeInMins;

        Time(String time) {

            try {
                this.timeInMins = convertHHMMToTimestamp(time);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        private Integer convertHHMMToTimestamp(final String time) throws Exception {

            try {
                String[] split = time.split(":");
                String hh = split[0];
                String mm = split[1];

                // convert hh to minutes
                Integer htoM = Integer.parseInt(hh) * 60;
                Integer min = Integer.parseInt(mm);

                return htoM + min;

            } catch (PatternSyntaxException e) {
                throw new Exception("Not a valid time string");
            }

        }


    }

    enum Status {
        SUCCESS,
        FAILURE
    }

    @Override
    public String toString() {
        return "Request{" +
                "ipAddr='" + ipAddr + '\'' +
                ", time=" + time +
                ", status='" + status + '\'' +
                '}';
    }
}


