package net.marcusolk.demo.jms;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.marcusolk.demo.jms.hello.HelloProxy;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

	private final HelloProxy helloProxy;

	@NewSpan
	@JmsListener(destination = "${queue.name}", containerFactory = "jmsFactory")
	public void receiveMessage(final TextMessage message) {
		try {
			log.info("Received request [receiveMessage]");

			final String result = this.helloProxy.sendHello(message.getText());

			log.info("Processed request [receiveMessage]: " + result);
		}
		catch (JMSException e) {
			log.error("Queue message could not be opened: {}", message, e);
		}

	}
}
