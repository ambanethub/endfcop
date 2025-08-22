package io.syss.auth.model;

import io.syss.shared.security.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(unique = true)
	private String username;

	@NotBlank
	private String passwordHash;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	private String unitId;

	private String totpSecret;

	private boolean twoFactorEnabled;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getPasswordHash() { return passwordHash; }
	public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
	public UserRole getRole() { return role; }
	public void setRole(UserRole role) { this.role = role; }
	public String getUnitId() { return unitId; }
	public void setUnitId(String unitId) { this.unitId = unitId; }
	public String getTotpSecret() { return totpSecret; }
	public void setTotpSecret(String totpSecret) { this.totpSecret = totpSecret; }
	public boolean isTwoFactorEnabled() { return twoFactorEnabled; }
	public void setTwoFactorEnabled(boolean twoFactorEnabled) { this.twoFactorEnabled = twoFactorEnabled; }
}