package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.HongXiangExceptionReportedRecord;

import java.time.Instant;
import java.util.Optional;

/** @author lh */
public interface HongXiangExceptionReportedRecordRepository extends EntityRepository<HongXiangExceptionReportedRecord, Long> {
    Optional<HongXiangExceptionReportedRecord> findByDeviceIdAndUploadDateTime(String deviceId, Instant credate);
}
