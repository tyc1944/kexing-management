package org.kexing.management.api_reset.dto;

import com.yunmo.iot.domain.core.GenericTelemetryRecordRpc;
import lombok.Data;

@Data
public class GenericTelemetryRecordResponse extends GenericTelemetryRecordRpc {
    private String rtsp;

    public void assign(GenericTelemetryRecordRpc recordRpc){
        this.setMessage(recordRpc.getMessage());
        this.setChannel(recordRpc.getChannel());
        this.setEntity(recordRpc.getEntity());
        this.setEventTime(recordRpc.getEventTime());
        this.setDeviceId(recordRpc.getDeviceId());
    }
}
