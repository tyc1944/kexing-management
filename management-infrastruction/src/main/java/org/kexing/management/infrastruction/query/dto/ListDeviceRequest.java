package org.kexing.management.infrastruction.query.dto;

import com.yunmo.iot.domain.core.DeviceConnectionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.kexing.management.infrastruction.query.BaseParam;

@Setter
@Getter
@Schema(description = "工单列表查询参数")
public class ListDeviceRequest extends BaseParam {

    private DeviceConnectionStatus deviceStatus;
    private String deviceType;
    private Long projectId;
    @Schema(description = "当查看门禁地图时设为TRUE,deviceType=door_face")
    private Boolean doorFaceMap;
}
