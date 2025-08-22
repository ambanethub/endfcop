package io.syss.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.syss.auth.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
	private final Key key;
	private final String issuer;
	private final long accessTtlSeconds;
	private final long refreshTtlSeconds;

	public JwtService(
		@Value("${security.jwt.secret}") String secret,
		@Value("${security.jwt.issuer}") String issuer,
		@Value("${security.jwt.access-ttl-seconds}") long accessTtlSeconds,
		@Value("${security.jwt.refresh-ttl-seconds}") long refreshTtlSeconds
	) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(secret.getBytes())));
		this.issuer = issuer;
		this.accessTtlSeconds = accessTtlSeconds;
		this.refreshTtlSeconds = refreshTtlSeconds;
	}

	public String createAccessToken(User user) {
		Instant now = Instant.now();
		return Jwts.builder()
			.setSubject(user.getUsername())
			.setIssuer(issuer)
			.setIssuedAt(Date.from(now))
			.setExpiration(Date.from(now.plusSeconds(accessTtlSeconds)))
			.addClaims(Map.of(
				"role", user.getRole().name(),
				"uid", user.getId()
			))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String createRefreshToken(User user) {
		Instant now = Instant.now();
		return Jwts.builder()
			.setSubject(user.getUsername())
			.setIssuer(issuer)
			.setIssuedAt(Date.from(now))
			.setExpiration(Date.from(now.plusSeconds(refreshTtlSeconds)))
			.claim("type", "refresh")
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}
}