type Listener = (data: any) => void;

class WSClient {
  private url: string;
  private ws: WebSocket | null = null;
  private reconnectAttempts = 0;
  private listeners = new Map<string, Set<Listener>>();
  private lastEventId = 0;

  constructor(url: string) {
    this.url = url;
  }

  connect() {
    if (typeof window === "undefined") return;
    const wsUrl = `${this.url}?since=${this.lastEventId}`;
    this.ws = new WebSocket(wsUrl);
    this.ws.onopen = () => {
      this.reconnectAttempts = 0;
    };
    this.ws.onmessage = (evt) => {
      try {
        const msg = JSON.parse(evt.data);
        if (typeof msg.eventId === "number") {
          this.lastEventId = Math.max(this.lastEventId, msg.eventId);
        }
        const set = this.listeners.get(msg.type);
        set?.forEach((cb) => cb(msg.payload));
      } catch (e) {
        console.error("WS message parse error", e);
      }
    };
    this.ws.onclose = () => {
      this.scheduleReconnect();
    };
    this.ws.onerror = () => {
      this.ws?.close();
    };
  }

  private scheduleReconnect() {
    const delay = Math.min(30000, 1000 * 2 ** this.reconnectAttempts + Math.random() * 1000);
    this.reconnectAttempts += 1;
    setTimeout(() => this.connect(), delay);
  }

  on(type: string, cb: Listener) {
    if (!this.listeners.has(type)) this.listeners.set(type, new Set());
    this.listeners.get(type)!.add(cb);
    return () => this.listeners.get(type)!.delete(cb);
  }
}

export function createWSClient(base = "/realtime/ws") {
  const proto = typeof window !== "undefined" && window.location.protocol === "https:" ? "wss" : "ws";
  const host = typeof window !== "undefined" ? window.location.host : "";
  const url = `${proto}://${host}${base}`;
  const client = new WSClient(url);
  client.connect();
  return client;
}