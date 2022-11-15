package org.kexing.management.domin.util;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** @author lh */
@Component
public class ErpDeviceUtil {
  /** 烘箱id前缀 */
  public static final String HONGXIANG_ID_PRE = "HX";

  public static final String WEIOLD_NAME = "维克浇注设备1#";
  public static final String WEINEW_NAME = "浇注新维克设备";
  public static final String HUWAI_NAME = "盈硕科浇注设备（户外）";
  public static final String HUNEI_NAME = "盈硕科浇注设备1#";

  public static final String DEVICE_WORK_STATUS_ID = "100"; // 工作状态
  public static final String DEVICE_DOWNTIME_STATUS_ID = "200"; // 停机状态
  public static final List<String> jzMachNames =
      Collections.unmodifiableList(Arrays.asList(WEIOLD_NAME, WEINEW_NAME, HUWAI_NAME, HUNEI_NAME));

  //<editor-fold desc="hide device setting">
  public static final String RX_DEVICE_NAME_CONTAIN_CONTAIN="绕";
  public static final List<String> HX_DEVICE_ID_NOT_CONTAIN =
      List.of(new String[] {"HX003701", "HX003702", "HX003703", "HX003704"});
  //</editor-fold>

  public static Optional<ErpDeviceType> getDeviceType(String deviceId, String deviceName) {
    if (deviceId.toUpperCase().startsWith(HONGXIANG_ID_PRE)) {
      return Optional.of(ErpDeviceType.HX);
    }
    if (deviceName.contains(RX_DEVICE_NAME_CONTAIN_CONTAIN)) {
      return Optional.of(ErpDeviceType.RX);
    }

    if (jzMachNames.contains(deviceName)) {
      return Optional.of(ErpDeviceType.JZ);
    }
    return Optional.empty();
  }


  @Schema(description = "erp 设备类型:JZ->浇筑设备,HX->烘箱设备,RX->绕线设备")
  public enum ErpDeviceType {
    JZ, // 浇筑设备
    HX, // 烘箱设备
    RX, // 绕线设备
  }
}
