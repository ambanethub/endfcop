"use client";
import { create } from "zustand";
import { saveTokens, loadTokens, Tokens, apiFetch } from "@/services/api";

type AuthState = {
  tokens: Tokens | null;
  setTokens: (t: Tokens | null) => void;
  login: (username: string, password: string, totp?: string) => Promise<void>;
  logout: () => void;
};

export const useAuth = create<AuthState>((set) => ({
  tokens: typeof window !== "undefined" ? loadTokens() : null,
  setTokens: (t) => {
    saveTokens(t);
    set({ tokens: t });
  },
  login: async (username, password, totp) => {
    const resp = await apiFetch<{ accessToken: string; refreshToken: string }>(
      "/auth/login",
      {
        method: "POST",
        body: JSON.stringify({ username, password, totp }),
      }
    );
    saveTokens(resp);
    set({ tokens: resp });
  },
  logout: () => {
    saveTokens(null);
    set({ tokens: null });
  },
}));