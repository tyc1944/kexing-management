package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.jz.JzExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.jz.JzWeiKeNewExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.jz.JzWeiKeOldExceptionReportedRecord;

import java.time.Instant;
import java.util.Optional;

/** @author lh */
public interface JZWeiKeNewExceptionReportedRecordRepository extends EntityRepository<JzWeiKeNewExceptionReportedRecord, Long> {
    Optional<JzWeiKeNewExceptionReportedRecord> findByDeviceIdAndUploadDateTime(String deviceId, Instant credate);

}
