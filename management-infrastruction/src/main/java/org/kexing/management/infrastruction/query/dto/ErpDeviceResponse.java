package org.kexing.management.infrastruction.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kexing.management.domin.model.mysql.DeviceSource;

/** @author lh */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpDeviceResponse {

  private String deviceName;
  private String deviceId;
  private String location;
  private DeviceSource source;
}
