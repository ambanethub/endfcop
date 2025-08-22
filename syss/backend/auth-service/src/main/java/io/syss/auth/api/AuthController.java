package io.syss.auth.api;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import io.syss.auth.model.User;
import io.syss.auth.repo.UserRepository;
import io.syss.auth.service.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final UserRepository users;
	private final JwtService jwtService;

	public AuthController(UserRepository users, JwtService jwtService) {
		this.users = users;
		this.jwtService = jwtService;
	}

	public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
	public record TokenResponse(String accessToken, String refreshToken) {}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequest req) {
		Optional<User> opt = users.findByUsername(req.username());
		if (opt.isEmpty()) return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
		User u = opt.get();
		if (!BCrypt.checkpw(req.password(), u.getPasswordHash())) {
			return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
		}
		if (u.isTwoFactorEnabled()) {
			return ResponseEntity.ok(Map.of("2fa_required", true));
		}
		return ResponseEntity.ok(new TokenResponse(jwtService.createAccessToken(u), jwtService.createRefreshToken(u)));
	}

	public record TwoFaSetupResponse(String secretBase32, String otpauthUrl) {}

	@PostMapping("/2fa/setup")
	public ResponseEntity<?> setup2fa(@RequestParam("username") String username) throws Exception {
		User u = users.findByUsername(username).orElse(null);
		if (u == null) return ResponseEntity.status(404).body(Map.of("error", "user_not_found"));
		TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30));
		KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
		keyGenerator.init(160);
		SecretKey key = keyGenerator.generateKey();
		String base32 = Base64.getEncoder().encodeToString(key.getEncoded());
		u.setTotpSecret(base32);
		u.setTwoFactorEnabled(true);
		users.save(u);
		String issuer = "SYSS";
		String otpauth = "otpauth://totp/" + issuer + ":" + username + "?secret=" + base32 + "&issuer=" + issuer + "&algorithm=SHA1&digits=6&period=30";
		return ResponseEntity.ok(new TwoFaSetupResponse(base32, otpauth));
	}

	public record TwoFaVerifyRequest(@NotBlank String username, int code) {}

	@PostMapping("/2fa/verify")
	public ResponseEntity<?> verify2fa(@RequestBody @Valid TwoFaVerifyRequest req) throws Exception {
		User u = users.findByUsername(req.username()).orElse(null);
		if (u == null || u.getTotpSecret() == null) return ResponseEntity.status(400).body(Map.of("error", "invalid_state"));
		TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
		byte[] secretBytes = Base64.getDecoder().decode(u.getTotpSecret());
		SecretKey key = new javax.crypto.spec.SecretKeySpec(secretBytes, totp.getAlgorithm());
		int current = totp.generateOneTimePassword(key, java.time.Instant.now());
		if (current != req.code()) return ResponseEntity.status(401).body(Map.of("error", "invalid_code"));
		return ResponseEntity.ok(new TokenResponse(jwtService.createAccessToken(u), jwtService.createRefreshToken(u)));
	}
}