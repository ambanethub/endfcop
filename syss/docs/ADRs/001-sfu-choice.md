# ADR 001: SFU Choice

We choose Jitsi stack for A/V due to maturity, batteries-included recording (Jibri), and simple on-prem deployment. Signaling is implemented in `av-signal-service`, which issues room tokens and manages attendance. Media flows via Jitsi; future work may consider mediasoup for tighter control.