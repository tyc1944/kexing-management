package org.kexing.management.infrastruction.query.dto;

import com.yunmo.iot.domain.core.DeviceConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/** @author lh */
@Setter
@Getter
public class DeviceNameAndAttributesResponse {
  private String deviceId;
  private Map<String,Object> attributes;
}
