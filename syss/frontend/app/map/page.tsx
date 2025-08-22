"use client"
import dynamic from 'next/dynamic'
import 'leaflet/dist/leaflet.css'

const MapClient = dynamic(() => import('../../components/MapClient'), { ssr: false })

export default function MapPage() {
	return (
		<div className="w-full h-[90vh]">
			<MapClient />
		</div>
	)
}