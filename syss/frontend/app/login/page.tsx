"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/store/auth";

export default function LoginPage() {
  const { login } = useAuth();
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [totp, setTotp] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await login(username, password, totp || undefined);
      router.push("/map");
    } catch (err: any) {
      setError(err?.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main style={{ display: "grid", placeItems: "center", height: "100vh" }}>
      <form onSubmit={onSubmit} style={{ display: "grid", gap: 8, width: 320 }}>
        <h2>Login</h2>
        <input placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
        <input placeholder="Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        <input placeholder="2FA code (if enabled)" value={totp} onChange={(e) => setTotp(e.target.value)} />
        {error ? <div style={{ color: "red" }}>{error}</div> : null}
        <button type="submit" disabled={loading}>{loading ? "Signing in..." : "Sign in"}</button>
      </form>
    </main>
  );
}