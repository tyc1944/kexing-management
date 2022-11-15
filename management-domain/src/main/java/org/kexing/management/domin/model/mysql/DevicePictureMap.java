package org.kexing.management.domin.model.mysql;

import com.yunmo.domain.common.Audited;
import com.yunmo.generator.annotation.AutoValueDTO;
import com.yunmo.generator.annotation.ValueField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/** @author lh */
@Entity
@Setter
@Getter
@Builder
@AutoValueDTO
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table
@Schema(description = "设备图片地图位置")
@org.hibernate.annotations.Table(appliesTo = "device_picture_map", comment = "设备图片地图位置")
public class DevicePictureMap extends Audited {
  @Id
  @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
  @GeneratedValue(generator = "sequence_id")
  private Long id;

  @Schema(description = "设备图片地址")
  @Column(length = 500,nullable = false)
  @ValueField
  @NotBlank
  private String pictureMap;

  @Schema(description = "设备图片地图类型")
  @ValueField
  @NotNull
  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private Type type;

  @org.hibernate.annotations.Type(type = "json")
  @Column(columnDefinition = "json")
  @Valid
  @ValueField
  private List<DeviceLocation> deviceLocationList;

  @Setter
  @Getter
  public static class DeviceLocation {
    @Schema(description = "设备id")
    @NotBlank private String deviceId;
    private String deviceName;
    // 设备x坐标
    @NotNull private BigDecimal deviceX;
    // 设备y坐标
    @NotNull private BigDecimal deviceY;
    // 设备图片中显示的宽度
    @NotNull private BigDecimal deviceW;
    // 设备在图片中显示的高度
    @NotNull private Double deviceH;
  }

  @Schema(description = "设备图片类型,WORKSHOP_DEVICE_MAP->车间设备图片地图,CAMERA_DEVICE_MAP->摄像头设备图片地图")
  public enum Type {
    @Schema(description = "车间设备图片地图")
    WORKSHOP_DEVICE_MAP,
    @Schema(description = "摄像头设备图片地图")
    IOT_DEVICE_MAP
  }
}
