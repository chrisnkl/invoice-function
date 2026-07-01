package com.chrisnkl.function.util;

import com.microsoft.azure.functions.ExecutionContext;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

public record Tracker(Logger logger, String operation, Instant startTime) {

    public Tracker(ExecutionContext context, String operation) {
        this(context.getLogger(), operation, Instant.now());
        logger.log(Level.INFO, "Operation {0} has started at {1}", new Object[]{operation, startTime.toString()});
    }

    public Tracker(Logger logger, String operation) {
        this(logger, operation, Instant.now());
        logger.log(Level.INFO, "Operation {0} has started at {1}", new Object[]{operation, startTime.toString()});
    }

    public static Tracker create(String operation, ExecutionContext context) {
        return new Tracker(context, operation);
    }

    public void complete() {
        Instant endTime = Instant.now();
        logger.log(Level.INFO, "Operation {0} has completed at {1} in {2}.", new Object[]{operation, endTime.toString(), Duration.between(startTime, endTime).toString()});
    }

}

