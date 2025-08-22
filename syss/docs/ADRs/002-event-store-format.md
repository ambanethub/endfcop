# ADR 002: Event Store Format

- Storage: PostgreSQL table `events` with columns: id (UUID), operation_id (UUID), type (TEXT), time (TIMESTAMPTZ), payload_json (JSONB).
- Rationale: Simple, queryable via SQL, easy to export, good for Testcontainers.
- Alternatives: Kafka as canonical store; rejected for simplicity and local replay without brokers.