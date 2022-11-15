package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.jz.JzHuNeiExceptionReportedRecord;
import org.kexing.management.domin.repository.mysql.JZHuNeiExceptionReportedRecordRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface JZHuNeiExceptionReportedRecordJpaRepository
    extends JpaRepository<JzHuNeiExceptionReportedRecord, Long>, JZHuNeiExceptionReportedRecordRepository {}
