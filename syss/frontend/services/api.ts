export type Tokens = { accessToken: string; refreshToken: string };

const TOKEN_KEY = "syss.tokens";

export function loadTokens(): Tokens | null {
  try {
    const raw = localStorage.getItem(TOKEN_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function saveTokens(tokens: Tokens | null) {
  if (!tokens) {
    localStorage.removeItem(TOKEN_KEY);
    return;
  }
  localStorage.setItem(TOKEN_KEY, JSON.stringify(tokens));
}

async function refresh(tokens: Tokens | null): Promise<Tokens | null> {
  // TODO: call /auth/refresh if implemented; placeholder returns null
  return null;
}

export async function apiFetch<T>(path: string, init: RequestInit = {}): Promise<T> {
  const base = process.env.NEXT_PUBLIC_API_BASE || "";
  let tokens = loadTokens();
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
    ...(init.headers as any),
  };
  if (tokens?.accessToken) {
    headers["Authorization"] = `Bearer ${tokens.accessToken}`;
  }
  let res = await fetch(base + path, { ...init, headers });
  if (res.status === 401) {
    tokens = await refresh(tokens);
    if (tokens) {
      saveTokens(tokens);
      headers["Authorization"] = `Bearer ${tokens.accessToken}`;
      res = await fetch(base + path, { ...init, headers });
    }
  }
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`HTTP ${res.status}: ${text}`);
  }
  if (res.status === 204) return undefined as any;
  return (await res.json()) as T;
}