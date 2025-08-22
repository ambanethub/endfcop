# ADR 003: Offline Conflict Resolution

- Policy: Last-write-wins on the client with server reconciliation.
- Client: queues mutations with timestamps; on reconnect, sends with `lastEventId` and includes mutation time.
- Server: applies if entity version unchanged; otherwise returns conflict details for user resolution.