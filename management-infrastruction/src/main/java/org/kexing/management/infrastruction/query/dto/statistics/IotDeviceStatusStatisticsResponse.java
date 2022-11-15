package org.kexing.management.infrastruction.query.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/** @author lh */
@Setter
@Getter
@Schema(description = "iot设备统计")
public class IotDeviceStatusStatisticsResponse {
  @Schema(description = "在线数量")
  private int numOfOnline;
  @Schema(description = "离线数量")
  private int numOfOffline;

  public void add(IotDeviceStatusStatisticsResponse iotDeviceStatusStatisticsResponse) {
    numOfOffline = iotDeviceStatusStatisticsResponse.getNumOfOffline() + numOfOffline;
    numOfOnline = iotDeviceStatusStatisticsResponse.getNumOfOnline() + numOfOnline;
  }

  @Getter
  @Setter
  @Accessors(chain = true)
  public static class IotDiffDeviceTypeStatusStatisticsResponse extends IotDeviceStatusStatisticsResponse{
    private IotDeviceStatusStatisticsResponse  camera;
    private IotDeviceStatusStatisticsResponse  temperatureAndHumiditySensor;
    private IotDeviceStatusStatisticsResponse  temperatureSensor;
    private IotDeviceStatusStatisticsResponse  doorFace;
    private IotDeviceStatusStatisticsResponse  gateway;
    private IotDeviceStatusStatisticsResponse  robot;

    public IotDiffDeviceTypeStatusStatisticsResponse setCamera(IotDeviceStatusStatisticsResponse camera) {
      this.camera = camera;
      add(camera);
      return this;
    }

    public IotDiffDeviceTypeStatusStatisticsResponse setTemperatureAndHumiditySensor(IotDeviceStatusStatisticsResponse temperatureAndHumiditySensor) {
      this.temperatureAndHumiditySensor = temperatureAndHumiditySensor;
      add(temperatureAndHumiditySensor);
      return this;
    }

    public IotDiffDeviceTypeStatusStatisticsResponse setTemperatureSensor(IotDeviceStatusStatisticsResponse temperatureSensor) {
      this.temperatureSensor = temperatureSensor;
      add(temperatureSensor);
      return this;
    }

    public IotDiffDeviceTypeStatusStatisticsResponse setDoorFace(IotDeviceStatusStatisticsResponse doorFace) {
      this.doorFace = doorFace;
      add(doorFace);
      return this;
    }

    public IotDiffDeviceTypeStatusStatisticsResponse setGateway(IotDeviceStatusStatisticsResponse gateway) {
      this.gateway = gateway;
      add(gateway);
      return this;
    }

    public IotDiffDeviceTypeStatusStatisticsResponse setRobot(IotDeviceStatusStatisticsResponse robot) {
      this.robot = robot;
      add(robot);
      return this;
    }
  }

}
