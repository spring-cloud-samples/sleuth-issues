package net.marcusolk.demo.jms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

/**
 * Startet den Service als SpringBoot-Applikation.
 */
@SpringBootApplication
public class Application implements InfoContributor {

	@Value("${application.name:n.a.}")
	private String applicationName;

	@Value("${application.version:n.a.}")
	private String applicationVersion;

	@Override
	public void contribute(final Info.Builder builder) {
		builder
			.withDetail("application-name", applicationName)
			.withDetail("application-version", applicationVersion);
	}

	public static void main(final String[] args) {
		run(Application.class, args);
	}

}
