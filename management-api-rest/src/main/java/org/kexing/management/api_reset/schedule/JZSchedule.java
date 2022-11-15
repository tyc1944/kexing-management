package org.kexing.management.api_reset.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.javatuples.Pair;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;
import org.kexing.management.domin.model.mysql.jz.JzHuNeiExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.jz.JzHuWaiExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.jz.JzWeiKeNewExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.jz.JzWeiKeOldExceptionReportedRecord;
import org.kexing.management.domin.model.sql_server.*;
import org.kexing.management.domin.repository.mysql.JZHuNeiExceptionReportedRecordRepository;
import org.kexing.management.domin.repository.mysql.JZHuWaiExceptionReportedRecordRepository;
import org.kexing.management.domin.repository.mysql.JZWeiKeNewExceptionReportedRecordRepository;
import org.kexing.management.domin.repository.mysql.JZWeiKeOldExceptionReportedRecordRepository;
import org.kexing.management.domin.service.ErpDeviceService;
import org.kexing.management.infrastruction.query.dto.ErpDeviceResponse;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.ErpTagUIDUploadDataQueryMapper;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.MacQueryMapper;
import org.kexing.management.infrastruction.service.sql_server.JZDeviceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.kexing.management.domin.util.ErpDeviceUtil.*;

/**
 * @author lh
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JZSchedule {
  private final JZDeviceService jzDeviceService;

  public static final int QUERY_INTERVAL_MINUTES = 1;
  private final JZHuNeiExceptionReportedRecordRepository jzHuNeiExceptionReportedRecordRepository;
  private final JZHuWaiExceptionReportedRecordRepository jzHuWaiExceptionReportedRecordRepository;
  private final JZWeiKeOldExceptionReportedRecordRepository
      jzWeiKeOldExceptionReportedRecordRepository;
  private final JZWeiKeNewExceptionReportedRecordRepository
      jzWeiKeNewExceptionReportedRecordRepository;
  private final ErpDeviceService erpDeviceService;
  private final MacQueryMapper macQueryMapper;

  @Transactional(transactionManager = "sqlServerTransactionManager")
  @Scheduled(fixedDelay = QUERY_INTERVAL_MINUTES, timeUnit = TimeUnit.MINUTES)
  public void getJZExceptionTask() {
    log.info("getJZExceptionTask start");
    try {
      List<ErpDeviceResponse> jzDeviceList = macQueryMapper.selectByMacNames(jzMachNames);

      if (Objects.isNull(jzDeviceList)) {
        throw new EntityNotFoundException();
      }
      for (ErpDeviceResponse erpDeviceResponse : jzDeviceList) {
        String jzDeviceId = erpDeviceResponse.getDeviceId();
        switch (erpDeviceResponse.getDeviceName()) {
          case HUNEI_NAME:
            jzHuNeiExceptionRecord(jzDeviceId);
            break;
          case HUWAI_NAME:
            jzHuWaiExceptionRecord(jzDeviceId);
            break;
          case WEINEW_NAME:
            jzWeiKeNewExceptionRecord(jzDeviceId);
            break;
          case WEIOLD_NAME:
            jzWeiKeOldExceptionRecord(jzDeviceId);
            break;
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      log.info("getJZExceptionTask end");
    }
  }

  private void jzWeiKeNewExceptionRecord(String jzDeviceId) {
    ErpDeviceConfig.JZWeiKeNewDeviceConfig jzWeiKeNewDeviceConfig =
        (ErpDeviceConfig.JZWeiKeNewDeviceConfig) erpDeviceService.getJZDeviceConfig(jzDeviceId);
    if (Objects.isNull(jzWeiKeNewDeviceConfig) || jzWeiKeNewDeviceConfig.hasEmptyConfig()) {
      return;
    }

    List<JZWeiKeNewDataUploadDate> jzWeiKeNewDataUploadDates =
        jzDeviceService.getLastJZWeiKeNewDataUploadDate(jzDeviceId);

    for (JZWeiKeNewDataUploadDate jzWeiKeNewDataUploadDate : jzWeiKeNewDataUploadDates) {
      Pair<Boolean, JZWeiKeNewDataUploadDate.WeiKeNewCheckedExceptionResult> resultPair =
          jzWeiKeNewDataUploadDate.inconformity(jzWeiKeNewDeviceConfig);
      if (ObjectUtils.isNotEmpty(resultPair.getValue0()) && !resultPair.getValue0()) {
        Optional<JzWeiKeNewExceptionReportedRecord> jzExceptionReportedRecord =
            jzWeiKeNewExceptionReportedRecordRepository.findByDeviceIdAndUploadDateTime(
                jzDeviceId, jzWeiKeNewDataUploadDate.getCredate());
        if (jzExceptionReportedRecord.isPresent()) {
          continue;
        }

        jzWeiKeNewExceptionReportedRecordRepository.save(
            JzWeiKeNewExceptionReportedRecord.builder()
                .deviceId(jzDeviceId)
                .uploadDateTime(jzWeiKeNewDataUploadDate.getCredate())
                .config(jzWeiKeNewDeviceConfig)
                .uploadDataWithConformityStatus(resultPair.getValue1())
                .build());
      }
    }
  }

  private void jzWeiKeOldExceptionRecord(String jzDeviceId) {
    ErpDeviceConfig.JZWeiKeOldDeviceConfig jzWeiKeOldDeviceConfig =
        (ErpDeviceConfig.JZWeiKeOldDeviceConfig) erpDeviceService.getJZDeviceConfig(jzDeviceId);
    if (Objects.isNull(jzWeiKeOldDeviceConfig) || jzWeiKeOldDeviceConfig.hasEmptyConfig()) {
      return;
    }

    List<JZWeiKeOldDataUploadDate> jzWeiKeOldDataUploadDates =jzDeviceService.getLastJZWeiKeOldDataUploadDate(jzDeviceId);

    for (JZWeiKeOldDataUploadDate jzWeiKeOldDataUploadDate : jzWeiKeOldDataUploadDates) {
      Pair<Boolean, JZWeiKeOldDataUploadDate.WeiKeOldCheckedExceptionResult> resultPair =
          jzWeiKeOldDataUploadDate.inconformity(jzWeiKeOldDeviceConfig);
      if (ObjectUtils.isNotEmpty(resultPair.getValue0()) && !resultPair.getValue0()) {
        Optional<JzWeiKeOldExceptionReportedRecord> jzExceptionReportedRecord =
            jzWeiKeOldExceptionReportedRecordRepository.findByDeviceIdAndUploadDateTime(
                jzDeviceId, jzWeiKeOldDataUploadDate.getCredate());
        if (jzExceptionReportedRecord.isPresent()) {
          continue;
        }

        jzWeiKeOldExceptionReportedRecordRepository.save(
            JzWeiKeOldExceptionReportedRecord.builder()
                .deviceId(jzDeviceId)
                .uploadDateTime(jzWeiKeOldDataUploadDate.getCredate())
                .config(jzWeiKeOldDeviceConfig)
                .uploadDataWithConformityStatus(resultPair.getValue1())
                .build());
      }
    }
  }

  private void jzHuNeiExceptionRecord(String jzDeviceId) {
    ErpDeviceConfig.JZHuNeiDeviceConfig jzHuNeiDeviceConfig =
        (ErpDeviceConfig.JZHuNeiDeviceConfig) erpDeviceService.getJZDeviceConfig(jzDeviceId);
    if (Objects.isNull(jzHuNeiDeviceConfig) || jzHuNeiDeviceConfig.hasEmptyConfig()) {
      return;
    }
    List<JZHuNeiDataUploadDate> jzHuNeiDataUploadDates =
            jzDeviceService.getLastJZHuNeiDataUploadDate(jzDeviceId);

    for (JZHuNeiDataUploadDate jzHuNeiDataUploadDate : jzHuNeiDataUploadDates) {
      Pair<Boolean, JZHuNeiDataUploadDate.HuNeiCheckedExceptionResult> resultPair =
          jzHuNeiDataUploadDate.inconformity(jzHuNeiDeviceConfig);
      if (ObjectUtils.isNotEmpty(resultPair.getValue0()) && !resultPair.getValue0()) {
        Optional<JzHuNeiExceptionReportedRecord> jzExceptionReportedRecord =
            jzHuNeiExceptionReportedRecordRepository.findByDeviceIdAndUploadDateTime(
                jzDeviceId, jzHuNeiDataUploadDate.getCredate());
        if (jzExceptionReportedRecord.isPresent()) {
          continue;
        }

        jzHuNeiExceptionReportedRecordRepository.save(
            JzHuNeiExceptionReportedRecord.builder()
                .deviceId(jzDeviceId)
                .uploadDateTime(jzHuNeiDataUploadDate.getCredate())
                .config(jzHuNeiDeviceConfig)
                .uploadDataWithConformityStatus(resultPair.getValue1())
                .build());
      }
    }
  }

  private void jzHuWaiExceptionRecord(String jzDeviceId) {
    ErpDeviceConfig.JZHuWaiDeviceConfig jzHuWaiDeviceConfig =
        (ErpDeviceConfig.JZHuWaiDeviceConfig) erpDeviceService.getJZDeviceConfig(jzDeviceId);
    if (Objects.isNull(jzHuWaiDeviceConfig) || jzHuWaiDeviceConfig.hasEmptyConfig()) {
      return;
    }

    List<JZHuWaiDataUploadDate> jzHuWaiDataUploadDates =
        jzDeviceService.getLastJZHuWaiDataUploadDate(jzDeviceId);

    for (JZHuWaiDataUploadDate jzHuWaiDataUploadDate : jzHuWaiDataUploadDates) {
      Pair<Boolean, JZHuWaiDataUploadDate.HuWaiCheckedExceptionResult> resultPair =
          jzHuWaiDataUploadDate.inconformity(jzHuWaiDeviceConfig);
      if (ObjectUtils.isNotEmpty(resultPair.getValue0()) && !resultPair.getValue0()) {
        Optional<JzHuWaiExceptionReportedRecord> jzExceptionReportedRecord =
            jzHuWaiExceptionReportedRecordRepository.findByDeviceIdAndUploadDateTime(
                jzDeviceId, jzHuWaiDataUploadDate.getCredate());
        if (jzExceptionReportedRecord.isPresent()) {
          continue;
        }

        jzHuWaiExceptionReportedRecordRepository.save(
            JzHuWaiExceptionReportedRecord.builder()
                .deviceId(jzDeviceId)
                .uploadDateTime(jzHuWaiDataUploadDate.getCredate())
                .config(jzHuWaiDeviceConfig)
                .uploadDataWithConformityStatus(resultPair.getValue1())
                .build());
      }
    }
  }
}
