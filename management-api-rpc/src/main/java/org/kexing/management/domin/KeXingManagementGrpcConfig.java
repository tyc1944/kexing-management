package org.kexing.management.domin;

import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractStub;
import org.kexing.management.domin.service.ServiceGrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/** @author lh */
public class KeXingManagementGrpcConfig extends ServiceGrpcClient {

  @Autowired private ApplicationContext appContext;

  protected ClientInterceptor[] getInterceptors() {
    return appContext
        .getBeansOfType(ClientInterceptor.class)
        .values()
        .toArray(new ClientInterceptor[0]);
  }

  @Override
  protected <T extends AbstractStub<T>> T config(T stub) {
    return stub.withInterceptors(getInterceptors());
  }

  @Bean
  @Override
  protected ManagedChannel getChannel() {
    return ManagedChannelBuilder.forAddress("kexing-management-server", 9090)
        .usePlaintext()
        .build();
  }
}
