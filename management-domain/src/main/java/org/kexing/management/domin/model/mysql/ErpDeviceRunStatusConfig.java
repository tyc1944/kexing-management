package org.kexing.management.domin.model.mysql;

import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author lh
 */
@Entity
@Setter
@Getter
@Builder
@AutoValueDTO
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "erp设备运行状态配置")
@org.hibernate.annotations.Table(appliesTo = "erp_device_run_status_config", comment = "erp设备运行状态配置")
@Table
public class ErpDeviceRunStatusConfig extends Audited {
    @Id
    @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
    @GeneratedValue(generator = "sequence_id")
    private Long id;

    @Schema(description = "烘箱上传温度必须大于该阈值,该设备为运行状态")
    private double hongXiangTemperatureThresholdValue;

    @Schema(description = "浇注机,浇注箱真空值大于该阈值,该设备为运行状态")
    private double jzVacuumThresholdValue;
}
