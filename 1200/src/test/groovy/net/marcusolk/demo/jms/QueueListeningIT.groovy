package net.marcusolk.demo.jms

import spock.util.concurrent.PollingConditions
import wiremock.org.apache.http.HttpStatus

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.jms.core.JmsTemplate
import org.springframework.test.annotation.DirtiesContext

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class QueueListeningIT extends ApiTestBase {

	@Autowired
	JmsTemplate jmsTemplate

	@Value('${queue.name}')
	String queueName

	def 'process message - happy path'() {
		given:
			def someMessage = 'Arthur'

		and:
			wireMock.stubFor(
					post(urlEqualTo('/'))
							.willReturn(aResponse().withStatus(HttpStatus.SC_OK)))

		when:
			jmsTemplate.convertAndSend(queueName, someMessage)

		then: 'wait until jms message has been processed'
			new PollingConditions(timeout: 30, initialDelay: 1, factor: 1.10).eventually {
				def hasMessage = jmsTemplate.browse(queueName) { session, browser ->
					browser.getEnumeration().hasMoreElements()
				}

				assert !hasMessage
			}

		and: 'the request has been sent'
			wireMock.verify(1, postRequestedFor(urlEqualTo('/')).withRequestBody(equalTo(someMessage)))
	}

}
