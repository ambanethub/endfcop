"use client";
import dynamic from "next/dynamic";
import { useEffect, useState } from "react";

const Map = dynamic(() => import("@/components/Map"), { ssr: false });

export default function MapPage() {
  const [ready, setReady] = useState(false);
  useEffect(() => setReady(true), []);
  return (
    <div style={{ height: "100vh", width: "100%" }}>
      {ready ? <Map /> : null}
    </div>
  );
}