package io.syss.auth.api;

import io.syss.auth.security.KeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwkController {
    private final KeyService keys;

    public JwkController(KeyService keys) {
        this.keys = keys;
    }

    @GetMapping("/.well-known/jwks.json")
    public ResponseEntity<Map<String, Object>> jwks() {
        return ResponseEntity.ok(keys.getJwkSet());
    }
}