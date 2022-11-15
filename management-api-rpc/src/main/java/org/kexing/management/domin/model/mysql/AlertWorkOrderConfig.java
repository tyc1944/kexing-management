package org.kexing.management.domin.model.mysql;


import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AutoValueDTO
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@org.hibernate.annotations.Table(appliesTo = "alert_work_order_config", comment = "报警工单配置")
@Table
public class AlertWorkOrderConfig  extends Audited {

    @Id
    @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
    @GeneratedValue(generator = "sequence_id")
    private Long id;

    private LocalTime morningStartWorkTime;
    private LocalTime morningEndWorkTime;
    private LocalTime afternoonStartWorkTime;
    private LocalTime afternoonEndWorkTime;
    private int timeDistance;
}
