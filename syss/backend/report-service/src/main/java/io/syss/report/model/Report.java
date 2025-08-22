package io.syss.report.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID operationId;

    @NotBlank
    private String type;

    @Column(length = 8000)
    private String fieldsJson;

    private Instant createdAt = Instant.now();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getOperationId() { return operationId; }
    public void setOperationId(UUID operationId) { this.operationId = operationId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getFieldsJson() { return fieldsJson; }
    public void setFieldsJson(String fieldsJson) { this.fieldsJson = fieldsJson; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}