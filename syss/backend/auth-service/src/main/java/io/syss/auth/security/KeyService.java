package io.syss.auth.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class KeyService {
    private final AtomicReference<RSAKey> current = new AtomicReference<>();

    public KeyService() {
        this.current.set(generate());
    }

    private RSAKey generate() {
        try {
            return new RSAKeyGenerator(2048)
                .keyUse(com.nimbusds.jose.jwk.KeyUse.SIGNATURE)
                .keyIDFromThumbprint(true)
                .generate();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate RSA key", e);
        }
    }

    public RSAKey getSigningKey() {
        return current.get();
    }

    public Map<String, Object> getJwkSet() {
        return new JWKSet(getSigningKey().toPublicJWK()).toJSONObject();
    }
}