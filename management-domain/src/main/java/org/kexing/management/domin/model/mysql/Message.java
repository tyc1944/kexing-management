package org.kexing.management.domin.model.mysql;

import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import io.genrpc.annotation.ProtoMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

/** @author lh */
@Entity
@Setter
@Getter
@Builder
@AutoValueDTO
@ProtoMessage
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@org.hibernate.annotations.Table(appliesTo = "message", comment = "消息")
@Table(
    indexes = {
      @Index(columnList = "createdDate"),
            @Index(columnList = "belongAccountId"),
    })
public class Message extends Audited {
  @Id
  @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
  @GeneratedValue(generator = "sequence_id")
  private Long id;

  @Schema(description = "内容")
  @Column(columnDefinition = "text not null")
  private String content;

  @Schema(description = "消息类型")
  @Enumerated(value = STRING)
  @Column(nullable = false)
  private Type type;

  @Schema(description = "产生消息数据的表id")
  @Column(columnDefinition = "bigint comment '产生消息数据的表id' ")
  private Long originalId;

  @Schema(description = "归属用户id")
  private Long belongAccountId;

  @Schema(
      description = "消息类型: WORK_ORDER_CREATE-> 创建工单, WORK_ORDER_PROCESS-> 处理工单, WORK_ORDER_CLOSE-> 关闭工单, WORK_ORDER_FINISH,-> 完成工单")
  public enum Type {
    @Schema(description = "创建工单")
    WORK_ORDER_CREATE,
    @Schema(description = "处理工单")
    WORK_ORDER_PROCESS,
    @Schema(description = "关闭工单")
    WORK_ORDER_CLOSE,
    @Schema(description = "完成工单")
    WORK_ORDER_FINISH,;
  }
}
