package org.kexing.management.infrastruction.query.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/** @author lh */
@Setter
@Getter
@Schema(description = "设备统计")
public class ErpDeviceStatusStatisticsResponse {
  @Schema(description = "工作数量")
  private int numOfWork;

  @Schema(description = "停机数量")
  private int numOfDowntime;

  public void addWorkNum(int i) {
    numOfWork = numOfWork + i;
  }

  public void addDownTimeNum(int i) {
    numOfDowntime = numOfDowntime + i;
  }

  public void add(ErpDeviceStatusStatisticsResponse erpDeviceStatusStatisticsResponse){
    numOfDowntime += erpDeviceStatusStatisticsResponse.getNumOfDowntime();
    numOfWork += erpDeviceStatusStatisticsResponse.getNumOfWork();
  }

  @Getter
  @Setter
  @Accessors(chain = true)
  public static class ErpDiffDeviceTypeStatusStatisticsResponse extends ErpDeviceStatusStatisticsResponse{
    private ErpDeviceStatusStatisticsResponse rx;
    private ErpDeviceStatusStatisticsResponse hx;
    private ErpDeviceStatusStatisticsResponse jz;

    public ErpDiffDeviceTypeStatusStatisticsResponse setRx(ErpDeviceStatusStatisticsResponse rx) {
      this.rx = rx;
      add(rx);
      return this;
    }

    public ErpDiffDeviceTypeStatusStatisticsResponse setHx(ErpDeviceStatusStatisticsResponse hx) {
      this.hx = hx;
      add((hx));
      return this;
    }

    public ErpDiffDeviceTypeStatusStatisticsResponse setJz(ErpDeviceStatusStatisticsResponse jz) {
      this.jz = jz;
      add(jz);
      return this;
    }
  }
}
