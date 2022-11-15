package org.kexing.management.domin.service;

import com.yunmo.domain.common.Problems;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.kexing.management.domin.model.mysql.DeviceSource;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.domin.model.mysql.value.*;
import org.kexing.management.domin.repository.mysql.WorkOrderRepository;
import org.kexing.management.domin.util.NoGeneratorUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

import static org.kexing.management.domin.model.mysql.WorkOrder.Status.*;
import static org.kexing.management.domin.model.mysql.WorkOrder.Type.EVENT;
import static org.kexing.management.domin.util.DefaultUserAccount.DEFAULT_SYSTEM_ACCOUNT_ID;

/** @author lh */
@Service
@RequiredArgsConstructor
@Validated
public class WorkOrderService implements GrpcWorkOrderService {
  private final WorkOrderRepository workOrderRepository;
  private final UserAccountService userAccountService;
  private final MessageService messageService;

  public WorkOrder createWorkOrder(long currentLoginId, @Valid WorkOrderValue workOrderValue) {
    if (workOrderValue.getImmediateProcessing() == null) {
      workOrderValue.setImmediateProcessing(false);
    }
    checkCreateWorkOrderParams(workOrderValue);
    WorkOrder workOrder = workOrderValue.create();

    // 指派处理
    Long processorUserAccountId = workOrder.getProcessorUserAccountId();

    workOrderRepository.save(
        workOrder
            .setNo(NoGeneratorUtils.noGenerator(NoGeneratorUtils.NoGeneratorPrefix.WO))
            .setProcessorUserAccountId(null));
    messageService.createWorkOrderMessage(
        currentLoginId, workOrder.getId(), workOrder.getNo(), workOrder.getStatus());

    if (processorUserAccountId != null) {
      assignProcessor(
          currentLoginId,
          workOrder,
          new WorkOrderAssignProcessor().setProcessorUserAccountId(processorUserAccountId));
    }
    return workOrder;
  }

  private void checkCreateWorkOrderParams(WorkOrderValue workOrderValue) {
    Problems.ensure(
        !(Objects.isNull(workOrderValue.getExpectHandleDateTime())
            && Objects.isNull(workOrderValue.getImmediateProcessing())),
        "期望处理时间与立即处理必须选择一个");
    Problems.ensure(
        !(ObjectUtils.isNotEmpty(workOrderValue.getExpectHandleDateTime())
            && ObjectUtils.isNotEmpty(workOrderValue.getImmediateProcessing())
            && workOrderValue.getImmediateProcessing()),
        "期望处理时间与立即处理只能选择一个");

    Problems.ensure(
        !(ObjectUtils.isEmpty(workOrderValue.getExpectHandleDateTime())
            && (ObjectUtils.isNotEmpty(workOrderValue.getImmediateProcessing())
                && !workOrderValue.getImmediateProcessing())),
        "期望立即处理需选择:true");

    switch (workOrderValue.getType()) {
      case EVENT:
        Problems.ensure(StringUtils.isNotBlank(workOrderValue.getProcessArea()), "处理区域需要设置");
        break;
      case DEVICE_MAINTAIN:
        Problems.ensure(workOrderValue.getDeviceId() != null, "设备id不能为空");
        Problems.ensure(workOrderValue.getDeviceSource() != null, "设备来源不能为空");
        break;
      case OTHER:
        break;
    }
  }

  public WorkOrder assignProcessor(
      long currentLoginId, WorkOrder workOrder, WorkOrderAssignProcessor workOrderAssignProcessor) {
    Problems.ensure(
        ObjectUtils.isNotEmpty(workOrderAssignProcessor.getProcessorUserAccountId()), "请指派工单处理人");

    Problems.ensure(workOrder.canAssign(), "工单状态不支持重新指派");
    userAccountService.checkValidUserAccount(workOrderAssignProcessor.getProcessorUserAccountId());

    workOrder.assignProcessor(currentLoginId, workOrderAssignProcessor.getProcessorUserAccountId());
    return workOrderRepository.save(workOrder);
  }

  public void closeWorkOrder(long loginId, WorkOrder workOrder, WorkOrderClose workOrderClose) {
    Problems.ensure(workOrder.canOff(), "当前工单状态不支持关闭操作");

    workOrderRepository.save(
        workOrder
            .setStatus(WorkOrder.Status.CLOSED)
            .setCloseReason(workOrderClose.getCloseReason())
            .setCloseTime(Instant.now()));
    messageService.createWorkOrderMessage(
            loginId, workOrder.getId(), workOrder.getNo(), workOrder.getStatus());
  }

  public void handleWorkOrder(long currentLoginId, WorkOrder workOrder) {
    Problems.ensure(workOrder.canHandle(), "工单未指定处理人或工单不在待处理状态");
    verifyWorkOrderProcessor(currentLoginId, workOrder);

    workOrderRepository.save(
        workOrder.setStatus(PROCESSING).setStartProcessingDateTime(Instant.now()));

    messageService.createWorkOrderMessage(
            currentLoginId, workOrder.getId(), workOrder.getNo(), workOrder.getStatus());

  }

  public void finishWorkOrder(
      long currentLoginId, WorkOrder workOrder, WorkOrderFinish workOrderFinish) {
    Problems.ensure(workOrder.canFinish(), "工单不在处理中状态");
    verifyWorkOrderProcessor(currentLoginId, workOrder);
    workOrderFinish.patchTo(workOrder);
    workOrderRepository.save(workOrder.setProcessedDateTime(Instant.now()).setStatus(UNCONFIRMED));
  }

  private void verifyWorkOrderProcessor(long currentLoginId, WorkOrder workOrder) {
    Problems.ensure(currentLoginId == workOrder.getProcessorUserAccountId(), "非工单处理人，无法处理工单");
  }

  @Override
  public WorkOrder createWorkOrder1(long deviceId,
                                    String processArea,
                                    String problemDescription,
                                    WorkOrder.Type type,
                                    Map<String,Object> attributes,
                                    DeviceSource deviceSource){
    WorkOrderValue workOrderValue = new WorkOrderValue();
    workOrderValue.setDeviceId(String.valueOf(deviceId));
    workOrderValue.setType(type);
    workOrderValue.setImmediateProcessing(true);
    workOrderValue.setProcessArea(processArea);
    workOrderValue.setProblemDescription(problemDescription);
    workOrderValue.setAttributes(attributes);
    workOrderValue.setDeviceSource(deviceSource);
    return createWorkOrder(DEFAULT_SYSTEM_ACCOUNT_ID, workOrderValue); // todo_lh 不传登录信息默认 create_by
  }

  @Override
  public WorkOrder createWorkOrder(
      @Valid WorkOrder.Source source, String processArea, String problemDescription) {
    WorkOrderValue workOrderValue = new WorkOrderValue();
    workOrderValue.setType(EVENT);
    workOrderValue.setImmediateProcessing(true);
    workOrderValue.setProcessArea(processArea);
    workOrderValue.setProblemDescription(problemDescription);
    workOrderValue.setSource(source);
    return createWorkOrder(DEFAULT_SYSTEM_ACCOUNT_ID, workOrderValue); // todo_lh 不传登录信息默认 create_by
  }

  public void confirmFinish(long loginId, WorkOrder workOrder) {
    Problems.ensure(workOrder.canConfirmFinish(),"工单未在待确认状态");

    workOrder =
        workOrderRepository.save(
            workOrder
                .setConfirmFinishDateTime(Instant.now())
                .setConfirmFinishUserAccountId(loginId)
                .setStatus(FINISHED));
    messageService.createWorkOrderMessage(
            loginId, workOrder.getId(), workOrder.getNo(), workOrder.getStatus());
  }
}
