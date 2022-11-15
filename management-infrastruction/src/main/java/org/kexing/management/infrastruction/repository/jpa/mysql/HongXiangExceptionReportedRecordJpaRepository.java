package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.HongXiangExceptionReportedRecord;
import org.kexing.management.domin.repository.mysql.HongXiangExceptionReportedRecordRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface HongXiangExceptionReportedRecordJpaRepository
    extends JpaRepository<HongXiangExceptionReportedRecord, Long>, HongXiangExceptionReportedRecordRepository {}
