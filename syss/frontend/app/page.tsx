import Link from 'next/link'

export default function HomePage() {
  return (
    <main style={{ padding: 16 }}>
      <h1>SYSS COP</h1>
      <ul>
        <li><a href="/map">Map</a></li>
        <li><a href="/ops">Operations</a></li>
      </ul>
    </main>
  );
}