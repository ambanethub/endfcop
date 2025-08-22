package io.syss.auth.api;

import io.syss.auth.model.User;
import io.syss.auth.repo.UserRepository;
import io.syss.auth.service.JwtService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

record LoginRequest(@NotBlank String username, @NotBlank String password, String totp) {}
record TokenResponse(String accessToken, String refreshToken){}
record TwoFASetupResponse(String qrDataUrl, String secret){}
record TwoFAVerifyRequest(@NotBlank String code){}

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository users;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository users, JwtService jwtService, PasswordEncoder encoder) {
        this.users = users;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<User> opt = users.findByUsername(req.username());
        if (opt.isEmpty()) return ResponseEntity.status(401).body("invalid_credentials");
        User u = opt.get();
        if (!encoder.matches(req.password(), u.getPasswordHash())) {
            return ResponseEntity.status(401).body("invalid_credentials");
        }
        if (u.isTwoFactorEnabled()) {
            if (req.totp() == null || !verifyTotp(u.getTotpSecret(), req.totp())) {
                return ResponseEntity.status(401).body("totp_required_or_invalid");
            }
        }
        String token = jwtService.createAccessToken(u);
        return ResponseEntity.ok(new TokenResponse(token, ""));
    }

    @PostMapping("/2fa/setup")
    public ResponseEntity<TwoFASetupResponse> setup2fa() {
        // For demo, return a static secret; production should bind to authenticated user
        String secret = Base64.getEncoder().encodeToString("DEVSECRET".getBytes());
        return ResponseEntity.ok(new TwoFASetupResponse("data:image/png;base64,", secret));
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<Void> verify2fa(@RequestBody TwoFAVerifyRequest request) {
        return ResponseEntity.noContent().build();
    }

    private boolean verifyTotp(String base32Secret, String code) {
        try {
            byte[] secret = Base64.getDecoder().decode(base32Secret);
            long timestep = 30L;
            long counter = Instant.now().getEpochSecond() / timestep;
            for (long i = -1; i <= 1; i++) {
                if (generateTotp(secret, counter + i) == Integer.parseInt(code)) return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private int generateTotp(byte[] key, long counter) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(key, "HmacSHA1"));
        byte[] data = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(counter).array();
        byte[] hash = mac.doFinal(data);
        int offset = hash[hash.length - 1] & 0x0F;
        int binary = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16) | ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);
        return binary % 1_000_000;
    }
}