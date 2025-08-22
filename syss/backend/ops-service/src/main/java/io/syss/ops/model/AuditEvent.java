package io.syss.ops.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_events")
public class AuditEvent {
    @Id
    @GeneratedValue
    private UUID id;
    private Instant time;
    private String actor;
    private String action;
    private String target;
    @Column(length = 4000)
    private String details;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Instant getTime() { return time; }
    public void setTime(Instant time) { this.time = time; }
    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}