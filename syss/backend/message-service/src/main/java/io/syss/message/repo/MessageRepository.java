package io.syss.message.repo;

import io.syss.message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByOperationIdOrderByCreatedAtAsc(UUID operationId);
}