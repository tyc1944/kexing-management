package org.kexing.management.domin.model.mysql;


import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AutoValueDTO
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@org.hibernate.annotations.Table(appliesTo = "vehicle", comment = "车辆")
@Table
public class Vehicle extends Audited {

    @Id
    @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
    @GeneratedValue(generator = "sequence_id")
    private Long id;

    @Column(unique = true)
    private String plateNumber;

    private Long staffId;
}
