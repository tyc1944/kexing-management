package org.kexing.management.domin.model.mysql;

import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/** @author lh */
@Entity
@Setter
@Getter
@Builder
@AutoValueDTO
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(indexes = {@Index(columnList = "iotDeviceId"), @Index(columnList = "erpDeviceId")})
@org.hibernate.annotations.Table(
    appliesTo = "iot_erp_device_bind_map",
    comment = "erp设备配置iot与erp设备绑定")
public class IotErpDeviceBindMap extends Audited {

  @Id
  @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
  @GeneratedValue(generator = "sequence_id")
  private Long id;

  private Long iotDeviceId;

  private String erpDeviceId;
}
