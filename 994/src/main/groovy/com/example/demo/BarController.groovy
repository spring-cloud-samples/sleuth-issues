package com.example.demo

import groovy.util.logging.Slf4j

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class BarController {

	@GetMapping(path = '/bar')
	String getBar() {
		log.info("[TEST_ME] entering ${this.class}")
		'bar'
	}

}
