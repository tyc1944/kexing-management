package org.kexing.management.infrastruction.query.dto.sql_server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/** @author lh */
@Setter
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(
      value = ListErpHongXiangDeviceResponse.class,
      name = "ListErpHongXiangDeviceResponse"),
})
@Schema(
    discriminatorProperty = "type",
    discriminatorMapping = {
      @DiscriminatorMapping(
          value = "ListErpHongXiangDeviceResponse",
          schema = ListErpHongXiangDeviceResponse.class),
    })
public class ListErpDeviceResponse {
  private String deviceName;

  @Schema(description = "设备id")
  private String deviceId;

  @Schema(description = "设备型号")
  private String deviceSpec;

  @Schema(description = "设备位置")
  private String location;

  @Schema(description = "设备状态")
  private String deviceStatus;

  @JsonIgnore
  @Schema(description = "内部取值使用")
  private String deviceStatusId;
}
