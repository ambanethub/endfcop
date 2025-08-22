import './globals.css'
import type { Metadata, Viewport } from 'next'
import { ReactQueryClientProvider } from '../lib/react-query'

export const metadata = {
  title: "SYSS COP",
  description: "Common Operational Picture",
};

export function generateViewport(): Viewport {
	return {
		width: 'device-width',
		initialScale: 1,
		themeColor: '#0B5FFF',
	}
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body style={{ margin: 0 }}>{children}</body>
    </html>
  );
}