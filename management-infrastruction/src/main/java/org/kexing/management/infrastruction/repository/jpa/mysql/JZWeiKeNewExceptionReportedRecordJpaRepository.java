package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.jz.JzWeiKeNewExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.jz.JzWeiKeOldExceptionReportedRecord;
import org.kexing.management.domin.repository.mysql.JZWeiKeNewExceptionReportedRecordRepository;
import org.kexing.management.domin.repository.mysql.JZWeiKeOldExceptionReportedRecordRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface JZWeiKeNewExceptionReportedRecordJpaRepository
    extends JpaRepository<JzWeiKeNewExceptionReportedRecord, Long>, JZWeiKeNewExceptionReportedRecordRepository {}
