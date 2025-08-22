import './globals.css'
import type { Metadata, Viewport } from 'next'
import { ReactQueryClientProvider } from '../lib/react-query'

export const metadata: Metadata = {
	title: 'SYSS COP',
	description: 'Common Operational Picture Platform',
}

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
			<body>
				<ReactQueryClientProvider>
					{children}
				</ReactQueryClientProvider>
			</body>
		</html>
	)
}