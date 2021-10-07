package net.marcusolk.demo.jms

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.common.Slf4jNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import spock.lang.Shared
import spock.lang.Specification

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles('TEST')
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class ApiTestBase extends Specification {

	@Shared WireMockServer wireMockServer;

	def setupSpec() {
		this.wireMockServer = new WireMockServer(WireMockConfiguration.options()
				.dynamicPort()
				.notifier(new Slf4jNotifier(true)))
		this.wireMockServer.start()
		System.properties.'wiremock.server.port' = wireMockServer.port()
	}

	def cleanupSpec() {
		this.wireMockServer.shutdown()
	}

}
