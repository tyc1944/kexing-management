package org.kexing.management.api_reset.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;
import org.kexing.management.domin.model.mysql.HongXiangExceptionReportedRecord;
import org.kexing.management.domin.model.sql_server.ErpDeviceUploadVal;
import org.kexing.management.domin.repository.mysql.HongXiangExceptionReportedRecordRepository;
import org.kexing.management.domin.service.ErpDeviceService;
import org.kexing.management.infrastruction.service.sql_server.HongXiangViewService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author lh
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HongXiangSchedule {
  private final HongXiangExceptionReportedRecordRepository
      hongXiangExceptionReportedRecordRepository;
  private final ErpDeviceService erpDeviceService;
  private final HongXiangViewService hongXiangViewService;

  public static final int QUERY_INTERVAL_MINUTES = 1;
  @Scheduled(fixedDelay = QUERY_INTERVAL_MINUTES, timeUnit = TimeUnit.MINUTES)
  @Transactional(transactionManager = "sqlServerTransactionManager")
  public void getHongXiangTemperatureExceptionTask() {
    try {
      log.info("hongXiangTemperatureExceptionTask start");
      List<ErpDeviceConfig> erpDeviceConfigList = erpDeviceService.listHongXiangDeviceConfig();
      if (CollectionUtils.isEmpty(erpDeviceConfigList)) {
        return;
      }

      for (ErpDeviceConfig erpDeviceConfig : erpDeviceConfigList) {
        ErpDeviceConfig.HongXiangDeviceConfig hongXiangDeviceConfig =
            erpDeviceConfig.hongXiangDeviceConfig();
        Optional<ErpDeviceUploadVal> erpDeviceUploadVal =
            hongXiangViewService.getHongXiangCurrentTemperatureAndUploadDate(
                erpDeviceConfig.getDeviceId());

        erpDeviceUploadVal.ifPresent(
            item -> {
              if (!hongXiangDeviceConfig.getTemperature().contain(item.getUploadDate().doubleValue())) {
                try {
                  if (hongXiangExceptionReportedRecordRepository
                      .findByDeviceIdAndUploadDateTime(
                          erpDeviceConfig.getDeviceId(), item.getUpdateTime())
                      .isEmpty()) {
                    hongXiangExceptionReportedRecordRepository.save(
                        HongXiangExceptionReportedRecord.builder()
                            .deviceId(erpDeviceConfig.getDeviceId())
                            .hongXiangDeviceConfig(hongXiangDeviceConfig)
                            .uploadDateTime(item.getUpdateTime())
                            .temperature(item.getUploadDate().doubleValue())
                            .build());
                  }
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                }
              }
            });
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      log.info("hongXiangTemperatureExceptionTask end");
    }
  }
}
