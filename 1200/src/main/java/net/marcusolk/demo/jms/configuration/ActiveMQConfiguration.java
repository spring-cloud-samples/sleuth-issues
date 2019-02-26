package net.marcusolk.demo.jms.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class ActiveMQConfiguration {

	@Value("${queue.connection}")
	private String connectionUrl;

	@Value("${queue.username}")
	private String user;

	@Value("${queue.password}")
	private String password;

	@Bean
	protected ActiveMQConnectionFactory connectionFactory() {
		final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(this.connectionUrl);
		factory.setUserName(this.user);
		factory.setPassword(this.password);
		return factory;
	}
}
