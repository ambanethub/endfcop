package io.syss.report.api;

import io.syss.report.model.Report;
import io.syss.report.repo.ReportRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportRepository reports;

    public ReportController(ReportRepository reports) {
        this.reports = reports;
    }

    @GetMapping
    public ResponseEntity<List<Report>> list(@RequestParam UUID operationId) {
        return ResponseEntity.ok(reports.findByOperationIdOrderByCreatedAtDesc(operationId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','COMMANDER','ANALYST')")
    @PostMapping("/sitrep")
    public ResponseEntity<Report> postSitrep(@RequestBody @Valid Report report) {
        report.setId(null);
        report.setType("SITREP");
        return ResponseEntity.ok(reports.save(report));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','COMMANDER','ANALYST')")
    @PostMapping("/conrep")
    public ResponseEntity<Report> postConrep(@RequestBody @Valid Report report) {
        report.setId(null);
        report.setType("CONREP");
        return ResponseEntity.ok(reports.save(report));
    }
}