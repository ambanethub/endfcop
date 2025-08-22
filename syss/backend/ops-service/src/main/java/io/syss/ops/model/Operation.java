package io.syss.ops.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String name;

    private String status;

    private Instant startTime;
    private Instant endTime;

    @Column(length = 4000)
    private String areaGeoJson;

    private UUID ownerId;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }
    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant endTime) { this.endTime = endTime; }
    public String getAreaGeoJson() { return areaGeoJson; }
    public void setAreaGeoJson(String areaGeoJson) { this.areaGeoJson = areaGeoJson; }
    public UUID getOwnerId() { return ownerId; }
    public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }
}