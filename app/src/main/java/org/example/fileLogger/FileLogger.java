package org.example.fileLogger;

import java.io.IOException;

import java.util.logging.LogManager;
import java.util.logging.Logger;


public class FileLogger {
    private static final Logger logger = Logger.getLogger(FileLogger.class.getName());

    static {
        try {
            // configure the file logging based on the logging.properties file under resources folder
            LogManager.getLogManager().readConfiguration(FileLogger.class.getResourceAsStream("/logging.properties"));

        } catch (IOException e) {
            System.out.println("An error occurred while writing benchmarks to file: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
