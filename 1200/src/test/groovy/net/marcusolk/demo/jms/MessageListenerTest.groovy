package net.marcusolk.demo.jms

import javax.jms.TextMessage

import net.marcusolk.demo.jms.hello.HelloProxy
import spock.lang.Specification
import spock.lang.Subject

class MessageListenerTest extends Specification {

	def helloProxy = Mock(HelloProxy)

	@Subject
	def messageListener = new MessageListener(helloProxy)

	def "test receiveMessage"() {
		given:
			def message = 'hello'

			def textMessage = Mock(TextMessage) {
				getText() >> message
			}

		when:
			messageListener.receiveMessage(textMessage)

		then:
			1 * helloProxy.sendHello(message)
	}

}
