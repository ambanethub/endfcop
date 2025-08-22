package io.syss.av.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/av")
public class AvController {

    @PreAuthorize("hasAnyAuthority('ADMIN','COMMANDER','ANALYST')")
    @PostMapping("/rooms")
    public ResponseEntity<Map<String, Object>> createRoom(@RequestBody Map<String, Object> body) {
        // TODO: create room bridged to Jitsi and store mapping
        return ResponseEntity.ok(Map.of("id", UUID.randomUUID(), "title", body.getOrDefault("title", "Room")));
    }

    @PostMapping("/rooms/{id}/join")
    public ResponseEntity<Map<String, Object>> join(@PathVariable UUID id) {
        // TODO: return ICE servers + JWT for Jitsi
        return ResponseEntity.ok(Map.of("iceServers", new Object[]{}, "token", "dev-token"));
    }

    @PostMapping("/rooms/{id}/offer")
    public ResponseEntity<Void> offer(@PathVariable UUID id, @RequestBody Map<String, Object> sdp) {
        // Placeholder for custom SFU; for Jitsi, not required
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/rooms/{id}/recordings/start")
    public ResponseEntity<Void> startRecording(@PathVariable UUID id) {
        // TODO: trigger Jibri or similar
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/rooms/{id}/recordings/stop")
    public ResponseEntity<Void> stopRecording(@PathVariable UUID id) {
        return ResponseEntity.accepted().build();
    }
}