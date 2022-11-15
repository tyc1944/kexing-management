package org.kexing.management.domin.model.mysql;

import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import com.yunmo.generator.annotation.ValueField;
import io.genrpc.annotation.ProtoEnum;
import io.genrpc.annotation.ProtoMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static javax.persistence.EnumType.STRING;
import static org.kexing.management.domin.model.mysql.WorkOrder.Status.*;

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
@org.hibernate.annotations.Table(appliesTo = "work_order", comment = "工单")
@Table(
    indexes = {
      @Index(name = "UK_NO_INDEX", columnList = "no", unique = true),
      @Index(name = "CREATE_DATE_INDEX", columnList = "createdDate DESC"),
      @Index(columnList = "assignDateTime"),
      @Index(columnList = "startProcessingDateTime"),
      @Index(columnList = "status"),
      @Index(name = "CREATE_DATE_INDEX", columnList = "createdDate"),
      @Index(name = "TYPE_INDEX", columnList = "type"),
      @Index(name = "DEVICE_SOURCE_INDEX", columnList = "deviceId,deviceSource"),
    })
public class WorkOrder extends Audited {
  @Id
  @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
  @GeneratedValue(generator = "sequence_id")
  private Long id;

  @Column(nullable = false)
  private String no;

  @ValueField
  @Enumerated(STRING)
  @NotNull(message = "工单类型不能为空")
  @Column(nullable = false)
  private Type type;

  @ValueField(value = "Update")
  @Schema(description = "期望处理时间")
  private Instant expectHandleDateTime;

  @Schema(description = "指派时间")
  private Instant assignDateTime;

  @Schema(description = "关闭时间")
  private Instant closeTime;

  @Schema(description = "开始处理时间")
  private Instant startProcessingDateTime;

  @Schema(description = "完成处理时间")
  private Instant processedDateTime;

  @Schema(description = "确认完成处理时间")
  private Instant confirmFinishDateTime;

  @Schema(description = "确认人id")
  private Long confirmFinishUserAccountId;

  @ValueField(value = "Update")
  @Schema(description = "立即处理")
  @Column(nullable = false)
  private boolean immediateProcessing;

  @ValueField(value = "Update")
  @Schema(description = "事件处理:处理区域")
  private String processArea;

  @ValueField(value = "Update")
  @Schema(description = "设备维护:设备id")
  private String deviceId;

  @ValueField(value = "Update")
  @Schema(description = "设备来源")
  @Enumerated(value = STRING)
  private DeviceSource deviceSource;

  @ValueField(value = "Update")
  @Size(max = 500)
  @Column(length = 500)
  @NotBlank(message = "问题描述不能为空")
  private String problemDescription;

  @ValueField(value = "Update")
  @org.hibernate.annotations.Type(type = "json")
  @Column(columnDefinition = "json")
  private List<String> createWorkOrderAttachment;

  @ValueField(value = {"AssignProcessor", "Update"})
  @Schema(description = "处理人账户id")
  private Long processorUserAccountId;

  @Schema(description = "指派账户id")
  private Long assignerUserAccountId;

  @ValueField(value = "Update")
  @Schema(description = "备注")
  @Size(max = 500)
  @Column(length = 500)
  private String remark;

  @Size(max = 60)
  @Column(length = 250)
  @ValueField(value = "Close")
  private String closeReason;

  @Schema(description = "处理过程")
  @Size(max = 500)
  @Column(length = 500)
  @ValueField(value = "Finish")
  private String handleProcess;

  @Schema(description = "处理反馈")
  @Size(max = 500)
  @Column(length = 500)
  @ValueField(value = "Finish")
  private String handleFeedBack;

  @ValueField(value = "Finish")
  @org.hibernate.annotations.Type(type = "json")
  @Column(columnDefinition = "json")
  private List<String> finishAttachment;

  @Schema(description = "工单状态")
  @Enumerated(value = STRING)
  @Column(nullable = false)
  @Builder.Default
  private Status status = AWAITING_ASSIGN;

  @Schema(description = "工单来源")
  @ValueField
  @Embedded
  private Source source;

  @Schema(description = "属性（报警事件信息）")
  @ValueField
  @org.hibernate.annotations.Type(type = "json")
  @Column(columnDefinition = "json")
  private Map<String, Object> attributes;
  /**
   * 指派处理人
   *
   * @param processorUserAccountId
   */
  public void assignProcessor(long assignUserAccountId, long processorUserAccountId) {
    this.assignerUserAccountId = assignUserAccountId;
    this.processorUserAccountId = processorUserAccountId;
    this.assignDateTime = Instant.now();
    this.status = AWAITING_PROCESSING;
  }

  public boolean canOff() {
    return status == AWAITING_ASSIGN || status == AWAITING_PROCESSING || status == PROCESSING;
  }

  public boolean canHandle() {
    return status == AWAITING_PROCESSING && ObjectUtils.isNotEmpty(this.processorUserAccountId);
  }

  public boolean canAssign() {
    return status == AWAITING_ASSIGN || status == AWAITING_PROCESSING;
  }

  public boolean canConfirmFinish() {
    return status == UNCONFIRMED;
  }

  public boolean canFinish() {
    return status == Status.PROCESSING;
  }

  public boolean canChange() {
    return status == Status.AWAITING_ASSIGN || status == AWAITING_PROCESSING;
  }

  @Schema(description = "工单类型:EVENT->事件, DEVICE_MAINTAIN->设备维护, OTHER->其他")
  public enum Type {
    EVENT,
    DEVICE_MAINTAIN,
    OTHER,
  }

  @Schema(
      description =
          "工单状态:AWAITING_ASSIGN->等待指派,AWAITING_PROCESSING->等待处理, PROCESSING->处理中, UNCONFIRMED->待确认,FINISHED->已完成, CLOSED->已关闭")
  public enum Status {
    @Schema(description = "等待指派")
    AWAITING_ASSIGN,
    @Schema(description = "等待处理")
    AWAITING_PROCESSING,
    @Schema(description = "处理中")
    PROCESSING,
    @Schema(description = "待确认")
    UNCONFIRMED,
    @Schema(description = "已完成")
    FINISHED,
    @Schema(description = "已关闭")
    CLOSED,
  }

  public static String getStatusDes(Status status) {
    switch (status) {
      case AWAITING_ASSIGN:
        return "等待指派";
      case AWAITING_PROCESSING:
        return "等待处理";
      case PROCESSING:
        return "处理中";
      case UNCONFIRMED:
        return "待确认";
      case FINISHED:
        return "已确认";
      case CLOSED:
        return "已关闭";
      default:
        return status.name();
    }
  }

  @Schema(description = "来源类型:ALARM->")
  @ProtoEnum
  public enum SourceType {
    camera,
    temperature_and_humidity_sensor,
    temperature_sensor,
    gateway,
    video_recorder,
    door,
    door_face,
    robot,
    ;

    public static String describe(SourceType sourceType) {
      switch (sourceType) {
        case camera:
          return "摄像头";
        case temperature_and_humidity_sensor:
          return "温湿度传感器";
        case temperature_sensor:
          return "测温传感器";
        case gateway:
          return "网关";
        case video_recorder:
          return "录像机";
        case door:
          return "道闸";
        case door_face:
          return "人脸机";
        case robot:
          return "机器人";
        default:
          return null;
      }
    }
  }

  @Data
  @Embeddable
  @ProtoMessage
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Source {
    @Schema(description = "来源id")
    @NotNull
    private Long id;

    @Schema(description = "来源类型")
    @Enumerated(value = STRING)
    @NotNull
    private SourceType type;
  }
}
