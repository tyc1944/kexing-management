package org.kexing.management.domin.model.sql_server;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ErpDeviceUploadVal {
  private String deviceId;
  private BigDecimal uploadDate;
  private Instant updateTime;
}
