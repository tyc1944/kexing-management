package org.kexing.management.api_reset.config;

import com.yunmo.attendance.api.AttendanceGrpcClient;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;

public class AttendanceApiConfig extends AttendanceGrpcClient {

    @Autowired
    private ApplicationContext appContext;
    @Value("${grpc.kexing.attendance.ip}")
    String ip;

    protected ClientInterceptor[] getInterceptors() {
        return appContext.getBeansOfType(ClientInterceptor.class).values().toArray(new ClientInterceptor[0]);
    }

    @Override
    protected <T extends AbstractStub<T>> T config(T stub) {
        return stub.withInterceptors(getInterceptors());
    }

    @Override
    protected ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(ip, 9090)
                .usePlaintext()
                .idleTimeout(5, TimeUnit.MINUTES)
                .build();
    }
}
