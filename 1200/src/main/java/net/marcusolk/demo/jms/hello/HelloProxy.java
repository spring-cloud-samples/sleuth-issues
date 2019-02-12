package net.marcusolk.demo.jms.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class HelloProxy {

	public static final String X_CALLER_REF = "x-caller-ref";

	private final String helloServiceUrl;
    private final RestTemplate restTemplate;

    public HelloProxy(
			final RestTemplateBuilder restTemplateBuilder,
			final @Value("${service.hello.url}") String helloServiceUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.helloServiceUrl = helloServiceUrl;
    }

    public String sendHello(final String name) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
		headers.set(X_CALLER_REF, "jms-queue-listener");
        final HttpEntity<String> entity = new HttpEntity<>(name, headers);

        try {
        	log.info("Sending hello request.");

            return restTemplate.exchange(helloServiceUrl, HttpMethod.POST, entity, String.class).getBody();
        }
		catch (final Exception e) {
            throw new RuntimeException("Hello reuest failed.", e);
        }
    }
}
