package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.jz.JzHuNeiExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.jz.JzHuWaiExceptionReportedRecord;

import java.time.Instant;
import java.util.Optional;

/** @author lh */
public interface JZHuWaiExceptionReportedRecordRepository extends EntityRepository<JzHuWaiExceptionReportedRecord, Long> {
    Optional<JzHuWaiExceptionReportedRecord> findByDeviceIdAndUploadDateTime(String deviceId, Instant credate);
}
