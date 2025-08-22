package io.syss.replay.api;

import io.syss.replay.model.Event;
import io.syss.replay.repo.EventRepository;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/replay")
public class ReplayController {
    private final EventRepository events;

    public ReplayController(EventRepository events) {
        this.events = events;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','COMMANDER','ANALYST')")
    @PostMapping("/bookmark")
    public ResponseEntity<?> bookmark(@RequestBody @Valid Map<String, Object> body) {
        // TODO: persist bookmark
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/ops/{id}/timeline")
    public ResponseEntity<List<Event>> timeline(
            @PathVariable UUID id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        List<Event> list = events.findByOperationIdAndTimeBetweenOrderByTimeAsc(id, from, to);
        return ResponseEntity.ok(list);
    }
}