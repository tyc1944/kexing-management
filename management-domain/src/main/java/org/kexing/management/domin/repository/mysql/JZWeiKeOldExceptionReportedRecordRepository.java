package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.jz.JzHuWaiExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.jz.JzWeiKeOldExceptionReportedRecord;

import java.time.Instant;
import java.util.Optional;

/** @author lh */
public interface JZWeiKeOldExceptionReportedRecordRepository extends EntityRepository<JzWeiKeOldExceptionReportedRecord, Long> {
    Optional<JzWeiKeOldExceptionReportedRecord> findByDeviceIdAndUploadDateTime(String deviceId, Instant credate);
}
