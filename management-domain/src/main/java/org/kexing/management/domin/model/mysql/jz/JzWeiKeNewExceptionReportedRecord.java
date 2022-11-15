package org.kexing.management.domin.model.mysql.jz;

import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;
import org.kexing.management.domin.model.sql_server.JZWeiKeNewDataUploadDate;
import org.kexing.management.domin.model.sql_server.JZWeiKeOldDataUploadDate;

import javax.persistence.*;
import java.time.Instant;

/** @author lh */
@Entity
@Setter
@Getter
@Builder
@AutoValueDTO
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "浇注机维克新异常上报记录")
@org.hibernate.annotations.Table(
    appliesTo = "jz_wei_ke_new_exception_reported_record",
    comment = "浇注机维克新异常上报记录")
@Table(indexes = {@Index(columnList = "deviceId,uploadDateTime", unique = true)})
public class JzWeiKeNewExceptionReportedRecord extends Audited implements JzExceptionReportedRecord{

  @Id
  @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
  @GeneratedValue(generator = "sequence_id")
  private Long id;

  @Schema(description = "上报时间")
  private Instant uploadDateTime;

  @Schema(description = "设备id")
  private String deviceId;

  @Schema(description = "erp device config,json 字符串")
  @org.hibernate.annotations.Type(type = "json")
  @Column(columnDefinition = "json") private ErpDeviceConfig.JZWeiKeNewDeviceConfig config;

  @Schema(description = "erp device update data with conformity status,json 字符串")
  @org.hibernate.annotations.Type(type = "json")
  @Column(columnDefinition = "json")
  private JZWeiKeNewDataUploadDate.WeiKeNewCheckedExceptionResult uploadDataWithConformityStatus;
}
