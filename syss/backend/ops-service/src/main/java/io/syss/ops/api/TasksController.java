package io.syss.ops.api;

import io.syss.ops.model.Task;
import io.syss.ops.repo.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping
public class TasksController {
    private final TaskRepository tasks;

    public TasksController(TaskRepository tasks) {
        this.tasks = tasks;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','COMMANDER','ANALYST')")
    @PostMapping("/ops/{opId}/tasks")
    public ResponseEntity<Task> create(@PathVariable UUID opId, @RequestBody @Valid Task task) {
        task.setId(null);
        task.setOperationId(opId);
        return ResponseEntity.ok(tasks.save(task));
    }

    @GetMapping("/ops/{opId}/tasks")
    public ResponseEntity<List<Task>> list(@PathVariable UUID opId) {
        return ResponseEntity.ok(tasks.findByOperationId(opId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','COMMANDER','FIELD_UNIT')")
    @PatchMapping("/tasks/{id}/status")
    public ResponseEntity<?> patchStatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        Optional<Task> t = tasks.findById(id);
        if (t.isEmpty()) return ResponseEntity.notFound().build();
        Task task = t.get();
        task.setStatus(body.getOrDefault("status", task.getStatus()));
        tasks.save(task);
        return ResponseEntity.noContent().build();
    }
}