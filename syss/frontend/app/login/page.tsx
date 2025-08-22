"use client"
import { useState } from 'react'

export default function LoginPage() {
	const [username, setUsername] = useState('admin')
	const [password, setPassword] = useState('admin')
	const [code, setCode] = useState('')
	const [needs2fa, setNeeds2fa] = useState(false)
	const [msg, setMsg] = useState('')

	async function doLogin() {
		setMsg('')
		const res = await fetch('http://localhost:8081/auth/login', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ username, password }),
		})
		const data = await res.json()
		if (data?.['2fa_required']) { setNeeds2fa(true); return }
		if (data?.accessToken) {
			localStorage.setItem('accessToken', data.accessToken)
			setMsg('Logged in')
		} else {
			setMsg('Login failed')
		}
	}

	async function verify2fa() {
		const res = await fetch('http://localhost:8081/auth/2fa/verify', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ username, code: Number(code) }),
		})
		const data = await res.json()
		if (data?.accessToken) {
			localStorage.setItem('accessToken', data.accessToken)
			setMsg('Logged in with 2FA')
		} else {
			setMsg('2FA failed')
		}
	}

	return (
		<div className="p-4 space-y-2">
			<h2 className="text-xl font-semibold">Login</h2>
			<input value={username} onChange={e=>setUsername(e.target.value)} placeholder="username" className="border p-2" />
			<input type="password" value={password} onChange={e=>setPassword(e.target.value)} placeholder="password" className="border p-2" />
			<button onClick={doLogin} className="border px-3 py-2">Login</button>
			{needs2fa && (
				<div className="space-x-2">
					<input value={code} onChange={e=>setCode(e.target.value)} placeholder="2FA code" className="border p-2" />
					<button onClick={verify2fa} className="border px-3 py-2">Verify</button>
				</div>
			)}
			<div>{msg}</div>
		</div>
	)
}