package com.example.w3c;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.util.StringUtils;

import static org.assertj.core.api.BDDAssertions.then;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class Issue1900ApplicationTests {

	private static final Pattern TRACE_ID_PATTERN = Pattern.compile("^.* INFO \\[issue-1900,(.*?),(.*?)] .*$");

	@Test
	void contextLoads(CapturedOutput capturedOutput) {
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			String allOutput = capturedOutput.getAll();
			String[] strings = allOutput.split(System.lineSeparator());
			boolean remoteEventMatched = false;
			boolean publishedEventMatched = false;
			for (String string : strings) {
				if (!remoteEventMatched) {
					remoteEventMatched = spanAndTraceMatched(string, "Remote event");
				}
				if (!publishedEventMatched) {
					publishedEventMatched = spanAndTraceMatched(string, "Published event");
				}
			}
			then(remoteEventMatched).as("Remote event must have span and trace ids").isTrue();
			then(publishedEventMatched).as("Published event must have span and trace ids").isTrue();
		});
	}

	private boolean spanAndTraceMatched(String string, String log) {
		if (string.contains(log)) {
			Matcher matcher = TRACE_ID_PATTERN.matcher(string);
			if (matcher.matches()) {
				return StringUtils.hasText(matcher.group(1)) && StringUtils.hasText(matcher.group(2));
			}
		}
		return false;
	}

}
