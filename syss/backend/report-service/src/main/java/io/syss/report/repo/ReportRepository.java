package io.syss.report.repo;

import io.syss.report.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByOperationIdOrderByCreatedAtDesc(UUID operationId);
}