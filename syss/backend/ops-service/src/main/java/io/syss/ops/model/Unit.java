package io.syss.ops.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name = "units")
public class Unit {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String name;

    private String type;

    private UUID parentUnitId;

    private String callsign;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public UUID getParentUnitId() { return parentUnitId; }
    public void setParentUnitId(UUID parentUnitId) { this.parentUnitId = parentUnitId; }
    public String getCallsign() { return callsign; }
    public void setCallsign(String callsign) { this.callsign = callsign; }
}