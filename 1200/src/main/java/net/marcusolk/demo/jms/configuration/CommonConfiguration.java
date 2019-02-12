package net.marcusolk.demo.jms.configuration;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@EnableJms
@Configuration
public class CommonConfiguration {

	@Bean
	public JmsListenerContainerFactory<?> jmsFactory(
			final ConnectionFactory connectionFactory,
			final DefaultJmsListenerContainerFactoryConfigurer configurer) {

		final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

		// This provides all boot's default to this factory, including the message converter
		configurer.configure(factory, connectionFactory);

		// You could still override some of Boot's default if necessary.
		return factory;
	}
}
