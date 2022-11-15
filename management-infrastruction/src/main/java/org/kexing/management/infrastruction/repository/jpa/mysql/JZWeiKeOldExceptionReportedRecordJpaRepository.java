package org.kexing.management.infrastruction.repository.jpa.mysql;

import org.kexing.management.domin.model.mysql.jz.JzHuWaiExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.jz.JzWeiKeOldExceptionReportedRecord;
import org.kexing.management.domin.repository.mysql.JZHuWaiExceptionReportedRecordRepository;
import org.kexing.management.domin.repository.mysql.JZWeiKeOldExceptionReportedRecordRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author lh */
public interface JZWeiKeOldExceptionReportedRecordJpaRepository
    extends JpaRepository<JzWeiKeOldExceptionReportedRecord, Long>, JZWeiKeOldExceptionReportedRecordRepository {}
