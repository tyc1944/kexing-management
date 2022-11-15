package org.kexing.management.api_reset.config.iot;

import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class IotApiConfig extends IotGrpcClient {

    @Value("${grpc.iot.ip}")
    String ip;

    @Autowired
    private ApplicationContext appContext;


    @Override
    protected <T extends AbstractStub<T>> T config(T stub) {
        return stub.withInterceptors();
    }

    @Override
    protected ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(ip, 9090)
                .usePlaintext()
                .idleTimeout(5, TimeUnit.MINUTES)
                .maxInboundMessageSize(104857600)//100mb
                .build();
    }
}
