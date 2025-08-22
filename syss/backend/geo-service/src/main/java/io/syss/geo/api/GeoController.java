package io.syss.geo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/geo")
public class GeoController {

    @GetMapping("/units/positions")
    public ResponseEntity<Map<String, Object>> positions(@RequestParam String bbox) {
        // TODO: query PostGIS and return GeoJSON FeatureCollection
        return ResponseEntity.ok(Map.of("type", "FeatureCollection", "features", new Object[]{}));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','COMMANDER','ANALYST')")
    @PostMapping("/overlays")
    public ResponseEntity<Void> saveOverlay(@RequestBody Map<String, Object> geojson) {
        // TODO: persist overlay
        return ResponseEntity.noContent().build();
    }
}