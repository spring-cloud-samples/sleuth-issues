package net.marcusolk.demo.jms.configuration;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@Configuration
@Profile("TEST")
public class TestConfiguration {

	@Bean
	protected ConnectionFactory connectionFactory() {
		return new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
	}

}