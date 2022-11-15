package org.kexing.management.api_reset.dto;

import com.yunmo.iot.domain.core.Device;
import com.yunmo.iot.domain.core.EntityEventRecordRpc;
import lombok.Data;

@Data
public class RouteRecordResponse {
    private EntityEventRecordRpc entityEventRecordRpc;
    private Device device;
}
