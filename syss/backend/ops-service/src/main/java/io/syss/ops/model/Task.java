package io.syss.ops.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID operationId;

    @NotBlank
    private String type;

    @Column(length = 4000)
    private String geometryGeoJson;

    private UUID assigneeUnitId;
    private String status;
    private String priority;
    private Instant dueAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getOperationId() { return operationId; }
    public void setOperationId(UUID operationId) { this.operationId = operationId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getGeometryGeoJson() { return geometryGeoJson; }
    public void setGeometryGeoJson(String geometryGeoJson) { this.geometryGeoJson = geometryGeoJson; }
    public UUID getAssigneeUnitId() { return assigneeUnitId; }
    public void setAssigneeUnitId(UUID assigneeUnitId) { this.assigneeUnitId = assigneeUnitId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public Instant getDueAt() { return dueAt; }
    public void setDueAt(Instant dueAt) { this.dueAt = dueAt; }
}