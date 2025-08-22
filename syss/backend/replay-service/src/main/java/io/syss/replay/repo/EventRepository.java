package io.syss.replay.repo;

import io.syss.replay.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByOperationIdAndTimeBetweenOrderByTimeAsc(UUID opId, Instant from, Instant to);
}