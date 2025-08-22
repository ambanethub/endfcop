package io.syss.ops.api;

import io.syss.ops.model.Operation;
import io.syss.ops.repo.OperationRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/ops")
public class OpsController {
    private final OperationRepository operations;

    public OpsController(OperationRepository operations) {
        this.operations = operations;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','COMMANDER')")
    @PostMapping
    public ResponseEntity<Operation> create(@RequestBody @Valid Operation op) {
        op.setId(null);
        Operation saved = operations.save(op);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operation> get(@PathVariable UUID id) {
        Optional<Operation> op = operations.findById(id);
        return op.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<Map<String, Object>> summary(@PathVariable UUID id) {
        // TODO: aggregate counts
        return ResponseEntity.ok(Map.of("id", id, "tasks", 0, "units", 0));
    }
}