package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.jz.JzHuNeiExceptionReportedRecord;

import java.time.Instant;
import java.util.Optional;

/** @author lh */
public interface JZHuNeiExceptionReportedRecordRepository extends EntityRepository<JzHuNeiExceptionReportedRecord, Long> {
    Optional<JzHuNeiExceptionReportedRecord> findByDeviceIdAndUploadDateTime(String deviceId, Instant credate);
}
