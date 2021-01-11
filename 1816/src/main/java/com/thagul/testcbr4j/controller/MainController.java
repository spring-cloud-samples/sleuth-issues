package com.thagul.testcbr4j.controller;

import com.thagul.testcbr4j.client.TestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/main")
@RestController
public class MainController {
    private final TestClient testClient;

    /**
     * Test.
     *
     * @return test
     */
    @RequestMapping("/test")
    public ResponseEntity<?> test() {
        log.info("Test");
        Map<String, Object> map = testClient.find(411640);
        log.info("response: " + map.get("id"));
        return ResponseEntity.ok(map);
    }
}
