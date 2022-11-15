package org.kexing.management.infrastruction.query.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * @author lh
 */
@Setter
@Getter
public class RobotResponse extends DeviceNameAndAttributesResponse{
  public RobotResponse(DeviceNameAndAttributesResponse deviceNameAndAttributesResponse) {
    setDeviceId(deviceNameAndAttributesResponse.getDeviceId());
    setAttributes(deviceNameAndAttributesResponse.getAttributes());
  }
}
