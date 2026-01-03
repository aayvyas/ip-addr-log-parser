package com.aayvyas;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> logs = Arrays.asList(
                "192.168.1.4,12:39,failure",
                "192.168.1.4,12:43,failure",
                "192.168.1.1,12:35,failure",
                "192.168.1.1,12:36,failure",
                "192.168.1.1,12:38,failure",
                "192.168.1.1,12:36,failure",
                "192.168.1.1,12:38,failure",
                "192.168.1.2,12:39,failure",
                "192.168.1.4,12:36,failure",
                "192.168.1.4,12:38,failure",
                "192.168.1.1,12:39,failure"

        );

        IpAddrLogParser ipl = new IpAddrLogParser(logs);

        try {
            ipl.flagIps();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ipl.getFlaggedIps().stream().forEach(System.out::println);






    }

}