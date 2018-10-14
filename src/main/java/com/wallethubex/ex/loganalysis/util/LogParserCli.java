package com.wallethubex.ex.loganalysis.util;


import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.wallethubex.ex.loganalysis.entity.Duration;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class LogParserCli {
    private final List<String> errors = new ArrayList<>(OPTIONS.getRequiredOptions().size());

    private LocalDateTime startDate;
    private TimeUnit duration;
    private int threshold;
    private File logFile;

    private LogParserCli() {
    }

    public static LogParserCli checkAgurments(String... args) {
        LogParserCli logParser = new LogParserCli();

        logParser.parseArgs(args);

        return logParser;
    }

    private void parseArgs(String... args) {
        CommandLine commandLine;
        try {
            commandLine = PARSER.parse(OPTIONS, args);
        } catch (ParseException e) {
            this.errors.add(e.getMessage());
            return;
        }

        try {
            startDate = LocalDateTime.parse(commandLine.getOptionValue(START_DATE_OPT), DATE_FORMATTER);
        } catch (Exception e) {
            this.errors.add(e.getMessage());
        }

        try {
            Duration parsedDuration = Duration.parseDuration(commandLine.getOptionValue(DURATION_OPT));
            if (parsedDuration == Duration.HOURLY) {
                this.duration = TimeUnit.HOURS;
            } else {
                this.duration = TimeUnit.DAYS;
            }
        } catch (ParseException e) {
            this.errors.add(e.getMessage());
        }

        try {
            threshold = Integer.parseInt(commandLine.getOptionValue(THRESHOLD_OPT));
            if (threshold <= 0) {
                errors.add(THRESHOLD_OPT + " should be positive");
            }
        } catch (NumberFormatException e) {
            this.errors.add(e.getMessage());
        }

        String logFilePath = commandLine.getOptionValue(ACCESSLOG_OPT);
        logFile = new File(logFilePath);
        if (!logFile.exists()) {
            errors.add("Defined log file '" + logFilePath + "' does not exist.");
        } else if (logFile.isDirectory()) {
            errors.add("Defined path '" + logFilePath + "' is directory.");
        }

    }

    public boolean isArgsValid() {
        return errors.isEmpty();
    }

    /**
     * Prints errors to {@link System#err}
     */
    public void showErrors() {
        errors.forEach(System.err::println);
    }

    public static void showArgsHelps() {
        HELP_FORMATTER.printHelp("parser", OPTIONS);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public TimeUnit getDuration() {
        return duration;
    }

    public int getThreshold() {
        return threshold;
    }

    public File getLogFile() {
        return logFile;
    }

    private static final CommandLineParser PARSER = new DefaultParser();

    private static final Options OPTIONS = new Options();
    private static final String START_DATE_OPT = "startDate";
    private static final String DURATION_OPT = "duration";
    private static final String THRESHOLD_OPT = "threshold";
    private static final String ACCESSLOG_OPT = "accesslog";

    private static final DateTimeFormatter DATE_FORMATTER;
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd.HH:mm:ss";

    private static final HelpFormatter HELP_FORMATTER = new HelpFormatter();

    static {
        DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

        OPTIONS.addOption(Option.builder().longOpt(START_DATE_OPT)
                .required()
                .hasArg()
                .argName("Date")
                .desc("Format: '" + DATE_FORMAT_PATTERN + "'")
                .build());

        OPTIONS.addOption(Option.builder().longOpt(DURATION_OPT)
                .required()
                .hasArg()
                .desc("Only 'hourly' or 'daily'")
                .build());

        OPTIONS.addOption(Option.builder().longOpt(THRESHOLD_OPT)
                .required()
                .hasArg()
                .desc("Positive integer value")
                .build());

        OPTIONS.addOption(Option.builder().longOpt(ACCESSLOG_OPT)
                .required()
                .hasArg()
                .desc("/path/to/file")
                .build());
    }


}