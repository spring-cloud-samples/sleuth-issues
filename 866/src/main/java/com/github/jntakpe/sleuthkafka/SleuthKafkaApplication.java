package com.github.jntakpe.sleuthkafka;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

@SpringBootApplication
public class SleuthKafkaApplication {

	private static final Log log = LogFactory.getLog(SleuthKafkaApplication.class);

	@RestController
	@RequestMapping("/kafka")
	class KafkaResource {


		private AtomicInteger correlationId = new AtomicInteger();

		private final KafkaSender<String, String> kafkaSender;

		private final ObjectMapper objectMapper;

		private final KafkaTemplate<String, String> kafkaTemplate;

		private final Tracer tracer;

		private final CurrentTraceContext currentTraceContext;

		KafkaResource(KafkaProperties kafkaProperties, ObjectMapper objectMapper, Tracer tracer, CurrentTraceContext currentTraceContext) {
			this.tracer = tracer;
			this.currentTraceContext = currentTraceContext;
			Map<String, Object> properties = kafkaProperties.buildProducerProperties();
			properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
					StringSerializer.class);
			properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
					StringSerializer.class);
			this.kafkaSender = KafkaSender.create(SenderOptions.create(properties));
			this.kafkaTemplate = new KafkaTemplate<>(
					new DefaultKafkaProducerFactory<>(properties));
			this.objectMapper = objectMapper;
		}

		@GetMapping("/reactor")
		public Mono<User> reactor(ServerWebExchange exchange)
				throws JsonProcessingException {
			User user = new User().setUsername("reactor-user").setAge(20);
			SenderRecord<String, String, Integer> record = WebFluxSleuthOperators.withSpanInScope(tracer, currentTraceContext, exchange, () -> {
				SenderRecord<String, String, Integer> r = SenderRecord
						.create("some-topic", 0, null, user.getUsername(),
								this.objectMapper.writeValueAsString(user),
								this.correlationId.incrementAndGet());
				log.info("[TEST] Sending the response for reactor");
				return r;
			});
			return this.kafkaSender.send(Mono.just(record)).map(i -> user).single();
		}

		@GetMapping("/template")
		public User template(ServerWebExchange exchange)
				throws JsonProcessingException, ExecutionException, InterruptedException {
			return WebFluxSleuthOperators.withSpanInScope(tracer, currentTraceContext, exchange, () -> {
				User user = new User().setUsername("template-user").setAge(30);
				SendResult<String, String> stringStringSendResult = this.kafkaTemplate
						.send("some-topic", this.objectMapper.writeValueAsString(user)).get();
				log.info("[TEST] Sending the response for template");
				return user;
			});
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(SleuthKafkaApplication.class, args);
	}
}
