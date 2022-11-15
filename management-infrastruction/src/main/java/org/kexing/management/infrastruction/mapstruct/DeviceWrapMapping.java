package org.kexing.management.infrastruction.mapstruct;

import com.yunmo.iot.domain.core.Device;
import org.kexing.management.infrastruction.query.dto.IotDeviceWrap;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        typeConversionPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DeviceWrapMapping {
    IotDeviceWrap map(Device device);
}
