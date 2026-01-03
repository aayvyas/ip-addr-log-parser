package com.aayvyas;

import java.util.*;

public class IpAddrLogParser {

    private final List<String> logs;

    private List<Request> requests;

    private final Set<String> flaggedIps = new HashSet<>();

    private Map<String, Deque<Request.Time>>  ipToRequestWindows = new HashMap<>();

    private final Integer WINDOW = 5;


    IpAddrLogParser(List<String> logs) {
        this.logs = logs;
    }

    private void processLogs() {
        this.requests = logs.stream().map(log -> {
            try{
               String[] split = log.split(",");

               String ipAddr = split[0];
               String timeStr = split[1];
               String status = split[2];
               Request.Time time = new Request.Time(timeStr);
               Request request = new Request(ipAddr, time,
                       status.contains("fail")
                               ? Request.Status.FAILURE
                               : Request.Status.SUCCESS);
               return request;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).toList();
    }


    public void flagIps() throws Exception {
        processLogs();
        if(logs.isEmpty()) throw new Exception("No logs to process");
        requests
                .stream()
                .filter(request -> request.status() == Request.Status.FAILURE)
                .sorted()
                .forEach(this::analyseFailures);

    }

    private void analyseFailures(Request failure) {


        Deque<Request.Time> dq = ipToRequestWindows
                .computeIfAbsent(failure.ipAddr(), k -> new ArrayDeque<>());

        while (!dq.isEmpty() && failure.time().timeInMins - dq.peekFirst().timeInMins > WINDOW) {
            dq.pollFirst();
        }

        dq.offerLast(failure.time());

        if (dq.size() > 3) {
            flaggedIps.add(failure.ipAddr());
        }


    }

    Set<String> getFlaggedIps() {
        // defensive so that caller can't modify the data
        return new HashSet<>(this.flaggedIps);
    }

}
