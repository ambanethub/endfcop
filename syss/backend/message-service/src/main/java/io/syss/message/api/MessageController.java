package io.syss.message.api;

import io.syss.message.model.Message;
import io.syss.message.repo.MessageRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageRepository messages;

    public MessageController(MessageRepository messages) {
        this.messages = messages;
    }

    @GetMapping
    public ResponseEntity<List<Message>> list(@RequestParam UUID operationId) {
        return ResponseEntity.ok(messages.findByOperationIdOrderByCreatedAtAsc(operationId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','COMMANDER','ANALYST','FIELD_UNIT')")
    @PostMapping
    public ResponseEntity<Message> post(@RequestBody @Valid Message msg) {
        msg.setId(null);
        return ResponseEntity.ok(messages.save(msg));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> get(@PathVariable UUID id) {
        Optional<Message> m = messages.findById(id);
        return m.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}