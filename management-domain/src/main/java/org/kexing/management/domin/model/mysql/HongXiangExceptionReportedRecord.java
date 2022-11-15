package org.kexing.management.domin.model.mysql;

import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.EnumType.STRING;

/** @author lh */
@Entity
@Setter
@Getter
@Builder
@AutoValueDTO
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "烘箱异常上报记录")
@org.hibernate.annotations.Table(
    appliesTo = "hong_xiang_exception_reported_record",
    comment = "烘箱异常上报记录")
@Table(indexes = {@Index(columnList = "deviceId,uploadDateTime", unique = true)})
public class HongXiangExceptionReportedRecord extends Audited {

  @Id
  @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
  @GeneratedValue(generator = "sequence_id")
  private Long id;

  @Schema(description = "原因")
  @Enumerated(STRING)
  @Builder.Default
  private Reason reason = Reason.TEMPERATURE_ABNORMAL;

  @Schema(description = "上报时间")
  private Instant uploadDateTime;

  @Schema(description = "设备id")
  private String deviceId;

  @org.hibernate.annotations.Type(type = "json")
  @Column(columnDefinition = "json")
  private ErpDeviceConfig.HongXiangDeviceConfig hongXiangDeviceConfig;

  @Schema(description = "温度")
  private Double temperature;

  @Schema(description = "原因:TEMPERATURE_ABNORMAL->温度异常")
  public enum Reason {
    TEMPERATURE_ABNORMAL
  }
}
