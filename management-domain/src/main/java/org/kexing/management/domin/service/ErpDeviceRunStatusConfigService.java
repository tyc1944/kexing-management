package org.kexing.management.domin.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.javatuples.Pair;
import org.kexing.management.domin.model.mysql.ErpDeviceRunStatusConfig;
import org.kexing.management.domin.repository.mysql.ErpDeviceRunStatusConfigRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.kexing.management.domin.util.ErpDeviceUtil.DEVICE_DOWNTIME_STATUS_ID;
import static org.kexing.management.domin.util.ErpDeviceUtil.DEVICE_WORK_STATUS_ID;

/** @author lh */
@Service
@RequiredArgsConstructor
public class ErpDeviceRunStatusConfigService {
  public static final String WORK_NAME = "正常加工";
  public static final String DOWN_NAME = "停机";
  private final ErpDeviceRunStatusConfigRepository erpDeviceRunStatusConfigRepository;

  public ErpDeviceRunStatusConfig getRunStatusConfig() {
    List<ErpDeviceRunStatusConfig> erpDeviceRunStatusConfigList =
        erpDeviceRunStatusConfigRepository.findAll();
    if (CollectionUtils.isEmpty(erpDeviceRunStatusConfigList)
        || erpDeviceRunStatusConfigList.size() > 1) {
      throw new UnsupportedOperationException("判断运行状态配置异常");
    }
    return erpDeviceRunStatusConfigList.get(0);
  }

  public double getHongXinagTemperatureThresholdValue() {
    return getRunStatusConfig().getHongXiangTemperatureThresholdValue();
  }

  public double getJZVacuumThresholdValue() {
    return getRunStatusConfig().getJzVacuumThresholdValue();
  }

  public String getJZDeviceStatus(
      Pair<Instant, Double> vacuumPouringJarA,
      Pair<Instant, Double> vacuumPouringJarB,
      double thresholdValue) {
    if (vacuumPouringJarA != null
        && isValidUploadDate(vacuumPouringJarA.getValue0())
        && vacuumPouringJarA.getValue1() != null
        && vacuumPouringJarA.getValue1() >= thresholdValue) {
      return WORK_NAME;
    }

    if (vacuumPouringJarB != null
        && isValidUploadDate(vacuumPouringJarB.getValue0())
        && vacuumPouringJarB.getValue1() != null
        && vacuumPouringJarB.getValue1() >= thresholdValue) {
      return WORK_NAME;
    }
    return DOWN_NAME;
  }
  /**
   * 上传时间在24小时内属于有效上传数据
   * @param uploadTime
   * @return
   */
  public static boolean isValidUploadDate(Instant uploadTime){
    if(uploadTime==null){
      return false;
    }
    return !uploadTime.plus(1, ChronoUnit.DAYS).isBefore(Instant.now());
  }


  public  String getHongXiangDeviceStatus(Optional<Double> temperature) {
    return getHongXiangDeviceStatus(temperature, getHongXinagTemperatureThresholdValue());
  }

  public String getHongXiangDeviceStatus(Optional<Double> currentTemperature, double thresholdValue) {
    String deviceStatus = DOWN_NAME;
    if (currentTemperature.isPresent()&& currentTemperature.get() >= thresholdValue) {
      deviceStatus = WORK_NAME;
    }
    return deviceStatus;
  }

  public String getDeviceStatusIdByDeviceStatus(String deviceStatus) {
    if (WORK_NAME.equals(deviceStatus)) {
      return DEVICE_WORK_STATUS_ID;
    } else if (DOWN_NAME.equals(deviceStatus)) {
      return DEVICE_DOWNTIME_STATUS_ID;
    }
    return null;
  }
}
