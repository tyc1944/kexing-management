package org.kexing.management.infrastruction.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.CodedInputStream;
import com.yunmo.attendance.api.entity.Staff;
import com.yunmo.attendance.api.service.StaffRpcService;
import com.yunmo.haikang.device.api.MediaRpcService;
import com.yunmo.iot.api.core.AssetEntityService;
import com.yunmo.iot.api.core.DeviceService;
import com.yunmo.iot.domain.core.Device;
import com.yunmo.iot.domain.rule.RuleAlert;
import com.yunmo.iot.domain.rule.RuleAlertMarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.client.api.*;
import org.kexing.management.domin.repository.mysql.UserAccountRepository;
import org.kexing.management.domin.service.CameraAlertService;
import org.kexing.management.domin.service.WorkOrderService;
import org.kexing.management.domin.service.cameraalert.AlertMessage;
import org.kexing.management.infrastruction.repository.mybatis.mysql.DeviceQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
@Slf4j
public class RuleAlertPulsarConsumer {
    @Autowired
    private PulsarClient client;

    ConsumerBuilder<byte[]> consumerBuilder;

    private RuleAlertMarshaller marshaller = new RuleAlertMarshaller();

    @Autowired
    WorkOrderService workOrderService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MediaRpcService mediaRpcService;
    @Autowired
    AssetEntityService assetEntityService;
    @Autowired
    StaffRpcService staffRpcService;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    DeviceQueryMapper deviceQueryMapper;
    @Autowired
    CameraAlertService cameraAlertService;



    @EventListener
    public void run(ContextRefreshedEvent event) {
        this.consumerBuilder = this.client.newConsumer()
                .topic("iot/rule/alert")
                .subscriptionName("kexing_alert")
                .deadLetterPolicy(DeadLetterPolicy.builder()
                .maxRedeliverCount(1)
                .build())
                .subscriptionType(SubscriptionType.Key_Shared);
        IntStream.range(0,4).forEach(i->{
            try {
                this.consumerBuilder.consumerName(String.format("kexing_alert-consumer-%d", i))
                        .messageListener(this::process)
                        .subscribe();
            } catch (PulsarClientException e) {
                log.error("状态订阅失败：{}", e.getMessage());
            }
        });
    }

    @Transactional
    public void process(Consumer<byte[]> consumer, Message<byte[]> msg) {
        try {
            RuleAlert alert = marshaller.parse(CodedInputStream.newInstance(msg.getData()), null);
            AlertMessage alertMessage = objectMapper.readValue(alert.getMessage(), AlertMessage.class);
            Device deviceCamera = deviceService.getDeviceById(alert.getDeviceId());
            //如果是广角摄像头则不需要创建工单
            if("wide_angle".equals(deviceCamera.getAttributes().get("cameraType"))){
                consumer.acknowledge(msg);
                return;
            }
            alertMessage.setAlert_time(alert.getAlertTime().getEpochSecond());
            cameraAlertService.createWorkOrderByAlert(alert.getDeviceId(),alertMessage);

            consumer.acknowledge(msg);
        } catch (Exception e) {
            consumer.negativeAcknowledge(msg);
            log.error(e.getMessage());
        }
    }


}
