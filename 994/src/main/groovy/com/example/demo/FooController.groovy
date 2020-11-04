package com.example.demo

import groovy.util.logging.Slf4j

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class FooController {

	@GetMapping(path = '/foo')
	String getFoo() {
		log.info("[TEST_ME] entering ${this.class}")
		'foo'
	}

}
