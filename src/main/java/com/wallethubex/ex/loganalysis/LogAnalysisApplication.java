package com.wallethubex.ex.loganalysis;

import com.wallethubex.ex.loganalysis.entity.BlockedIp;
import com.wallethubex.ex.loganalysis.service.LogAnalysisService;
import com.wallethubex.ex.loganalysis.util.LogParserCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootApplication
public class LogAnalysisApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogAnalysisApplication.class);

	public static void main(String[] args) {
		LogParserCli logParser = LogParserCli.checkAgurments(args);
		if (logParser.isArgsValid()) {
			SpringApplication.run(LogAnalysisApplication.class, args);
		} else {
			logParser.showErrors();
			LogParserCli.showArgsHelps();
		}
	}

	@Bean
	CommandLineRunner runner(FileParser fileParser, LogAnalysisService analysisService) {
		return args -> {

			LogParserCli logParserCli = LogParserCli.checkAgurments(args);
			final long startTime = System.currentTimeMillis();

			try {
				fileParser.importAccessLog(logParserCli.getLogFile());
			} catch (IOException e) {
				LOGGER.error("Unable to parse. "+ e.getMessage());
			}

			long executionTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime);
			LOGGER.info("Access Log import in: {} seconds!", executionTime);

			analysisService.analysisAssessLog(logParserCli.getStartDate(), logParserCli.getDuration(), logParserCli.getThreshold());

			List<BlockedIp> blockedAddresses = analysisService.findBlockedIps();

			LOGGER.info("Blocked IPs:\n {}",
					blockedAddresses
							.stream()
							.map(e -> '\n' + e.ip)
							.collect(Collectors.toList()));
		};
	}
}
