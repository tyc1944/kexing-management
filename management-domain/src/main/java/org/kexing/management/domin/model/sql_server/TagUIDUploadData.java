package org.kexing.management.domin.model.sql_server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 *
 * @author lh
 */
@Data
public class TagUIDUploadData {
    @Schema(description = "身份标识")
    private String tagUID;

    @Schema(description = "上传的原始值")
    private String  uploadOriginalValue;

    @Schema(description = "更新时间")
    private Instant updateTime;

    @Schema(description = "仪表点上设置了计算公式通过 原始值计算得到")
    private BigDecimal uploadNumber;

  public BigDecimal getUploadNumber() {
    if (uploadOriginalValue == null) {
      return null;
    }
    return uploadNumber;
  }

    public Double getDoubleUploadNumber() {
    if (getUploadNumber() == null) {
      return null;
    }
    return getUploadNumber().doubleValue();
  }

}
