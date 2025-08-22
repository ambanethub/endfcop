"use client"
import { MapContainer, TileLayer } from 'react-leaflet'

export default function MapClient() {
	if (typeof window === 'undefined') return null
	return (
		<MapContainer center={[8.9806, 38.7578]} zoom={12} style={{ height: '100%', width: '100%' }}>
			<TileLayer
				url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
				attribution="&copy; OpenStreetMap contributors"
			/>
		</MapContainer>
	)
}