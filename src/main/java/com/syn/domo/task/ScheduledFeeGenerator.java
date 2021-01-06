package com.syn.domo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class ScheduledFeeGenerator {

    private static final Logger log =
            LoggerFactory.getLogger(ScheduledFeeGenerator.class);

    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("HH:mm:ss");
}
