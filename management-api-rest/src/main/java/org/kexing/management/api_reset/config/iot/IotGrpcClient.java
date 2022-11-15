package org.kexing.management.api_reset.config.iot;

import com.yunmo.iot.api.core.*;
import io.genrpc.runtime.AbstractClient;
import org.springframework.context.annotation.Bean;

public abstract class IotGrpcClient extends AbstractClient {
    @Bean
    public DeviceService deviceService() {
        return (DeviceService)this.config(DeviceServiceGrpc.newBlockingStub(this.getChannel()));
    }

    @Bean
    public AssetEntityService assetEntityService() {
        return (AssetEntityService)this.config(AssetEntityServiceGrpc.newBlockingStub(this.getChannel()));
    }

    @Bean
    public TelemetryRecordService telemetryRecordService() {
        return (TelemetryRecordService)this.config(TelemetryRecordServiceGrpc.newBlockingStub(this.getChannel()));
    }

    @Bean
    public EntityEventService entityEventService() {
        return (EntityEventService)this.config(EntityEventServiceGrpc.newBlockingStub(this.getChannel()));
    }

    @Bean
    public DeviceOtaRecordService deviceOtaRecordService() {
        return (DeviceOtaRecordService)this.config(DeviceOtaRecordServiceGrpc.newBlockingStub(this.getChannel()));
    }
}
