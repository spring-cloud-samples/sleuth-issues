package net.marcusolk.demo.jms

import com.github.tomakehurst.wiremock.common.Slf4jNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import org.junit.ClassRule
import org.junit.Rule
import spock.lang.Specification

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles('TEST')
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class ApiTestBase extends Specification {

	@ClassRule
	static wireMockRule = new WireMockClassRule(WireMockConfiguration.options()
																	 .dynamicPort()
																	 .notifier(new Slf4jNotifier(true)))

	@Rule
	WireMockClassRule wireMock = wireMockRule

	def setupSpec() {
		wireMockRule.start()
		System.properties.'wiremock.server.port' = wireMockRule.port()
	}

}
