import Link from 'next/link'

export default function Home() {
	return (
		<main className="p-4 space-y-4">
			<h1 className="text-2xl font-semibold">SYSS COP</h1>
			<nav className="space-x-4">
				<Link href="/map">Map</Link>
				<Link href="/login">Login</Link>
			</nav>
		</main>
	)
}