package org.kexing.management.infrastruction.query.dto;

import com.yunmo.iot.domain.core.Device;
import com.yunmo.iot.domain.ota.DeviceOTARecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kexing.management.infrastruction.query.dto.sql_server.GetErpDeviceResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IotDeviceWrap extends Device {
    List<Device> children;
    List<GetErpDeviceResponse> erpDevices;
    DeviceOTARecord lastDeviceOTARecord;
}
