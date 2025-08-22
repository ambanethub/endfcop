package io.syss.auth.api;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

record LoginRequest(@NotBlank String username, @NotBlank String password, String totp)
{}
record TokenResponse(String accessToken, String refreshToken){}
record TwoFASetupResponse(String qrDataUrl, String secret){}
record TwoFAVerifyRequest(@NotBlank String code){}

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        // TODO: validate user, verify password and optional TOTP
        return ResponseEntity.ok(new TokenResponse("dev-access-token", "dev-refresh-token"));
    }

    @PostMapping("/2fa/setup")
    public ResponseEntity<TwoFASetupResponse> setup2fa() {
        // TODO: generate TOTP secret and QR
        return ResponseEntity.ok(new TwoFASetupResponse("data:image/png;base64,", "DEVSECRET"));
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<Void> verify2fa(@RequestBody TwoFAVerifyRequest request) {
        // TODO: verify code against stored secret
        return ResponseEntity.noContent().build();
    }
}