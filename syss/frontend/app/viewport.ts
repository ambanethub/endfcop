import type { Viewport } from "next";

export function generateViewport(): Viewport {
  return {
    themeColor: "#0f172a",
    width: "device-width",
    initialScale: 1,
    maximumScale: 1,
    userScalable: false,
    viewportFit: "cover",
  };
}