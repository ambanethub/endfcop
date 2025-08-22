package io.syss.ops.repo;

import io.syss.ops.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByOperationId(UUID operationId);
}