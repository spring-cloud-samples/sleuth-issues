package net.marcusolk.demo.jms.configuration;

import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.logstash.logback.decorate.JsonFactoryDecorator;

import static com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class LogstashLogbackJsonFactoryDecorator implements JsonFactoryDecorator {

	@Override
	public MappingJsonFactory decorate(final MappingJsonFactory factory) {

		final ObjectMapper objectMapper = factory.getCodec();

		objectMapper.registerModules(new Jdk8Module(), new JavaTimeModule());
		objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.configure(ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);

		return factory;
	}

}
