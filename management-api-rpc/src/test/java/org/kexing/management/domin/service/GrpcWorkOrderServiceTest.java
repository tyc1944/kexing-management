package org.kexing.management.domin.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.domin.model.mysql.value.WorkOrderValue;

import java.time.Instant;
import java.util.Arrays;

@RunWith(JUnit4.class)
class GrpcWorkOrderServiceTest {
  GrpcWorkOrderService grpcWorkOrderService;
  ManagedChannel channel;

  @BeforeClass
  public void client() {}

  @Test
  void create() {
//    channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
//    grpcWorkOrderService = GrpcWorkOrderServiceGrpc.newBlockingStub(channel);
//
//    WorkOrderValue workOrderValue = new WorkOrderValue();
//    workOrderValue
//        .setType(WorkOrder.Type.EVENT)
//        .setExpectHandleDateTime(Instant.now())
//        .setImmediateProcessing(false)
//        .setProcessArea("fake_data")
//        .setDeviceId(2L)
//        .setProblemDescription("fake_data1")
//        .setCreateWorkOrderAttachment(Arrays.asList("fake_data"))
//        .setProcessorUserAccountId(1L)
//        .setCcUserAccountIds(Arrays.asList(1L))
//        .setRemark("test");
//
//    WorkOrder workOrder =
//        grpcWorkOrderService.createWorkOrder(WorkOrder.Source.builder().build(), "处理位置", "问题描述");
//    System.out.println(workOrder);
  }
}
