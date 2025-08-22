"use client";
import { useEffect } from "react";
import dynamic from "next/dynamic";

const LeafletMap = dynamic(async () => {
  const L = await import("leaflet");
  await import("leaflet/dist/leaflet.css");
  return function Inner() {
    useEffect(() => {
      if (typeof window === "undefined") return;
      const map = L.map("map", { center: [9.03, 38.74], zoom: 12 });
      L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: "&copy; OpenStreetMap contributors",
      }).addTo(map);
    }, []);
    return <div id="map" style={{ height: "100vh", width: "100%" }} />;
  };
}, { ssr: false });

export default function Map() {
  return <LeafletMap />;
}