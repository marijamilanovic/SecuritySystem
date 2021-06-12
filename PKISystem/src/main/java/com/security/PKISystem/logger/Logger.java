package com.security.PKISystem.logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger {
    public static void info(String message, String username){
        log.info("[USER] = " + username + " [MESSAGE] = " + message);
    }

    public static void error(String message, String username){
        log.error("[USER] = " + username + " [MESSAGE] = " + message);
    }

    public static void infoDb(String message){
        log.info("[DATABASE] = " + message);
    }

    public static void errorDb(String message, String exception){
        log.error("[DATABASE] = " + message + " [EXCEPTION] = " + exception);
    }

    public static void infoMessage(String message){
        log.info("[MESSAGE] = " + message);
    }
}
