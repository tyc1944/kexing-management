package org.kexing.management.api_reset.resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yunmo.domain.common.Principal;
import com.yunmo.domain.common.Problems;
import com.yunmo.domain.common.Tenant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.domin.model.mysql.value.*;
import org.kexing.management.domin.repository.mysql.WorkOrderRepository;
import org.kexing.management.domin.service.SmsService;
import org.kexing.management.domin.service.UserAccountService;
import org.kexing.management.domin.service.WorkOrderService;
import org.kexing.management.infrastruction.query.BaseParam;
import org.kexing.management.infrastruction.query.dto.*;
import org.kexing.management.infrastruction.service.DeviceViewService;
import org.kexing.management.infrastruction.service.UserAccountViewService;
import org.kexing.management.infrastruction.service.WorkOrderViewService;
import org.kexing.management.infrastruction.util.sms.YunPianMessageUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

import static org.kexing.management.domin.model.mysql.WorkOrder.Status.AWAITING_PROCESSING;
import static org.kexing.management.domin.model.mysql.WorkOrder.Type.DEVICE_MAINTAIN;

/** @author lh */
@RequestMapping("/api/workOrders")
@RestController
@Tag(name = "工单资源")
@RequiredArgsConstructor
@Transactional
public class WorkOrderResource {
  private final WorkOrderRepository workOrderRepository;
  private final WorkOrderViewService workOrderViewService;
  private final WorkOrderService workOrderService;
  private final UserAccountViewService userAccountViewService;
  private final UserAccountService userAccountService;
  private final SmsService smsService;
  private final DeviceViewService deviceViewService;

  @PostMapping
  @Operation(description = "创建工单")
  public WorkOrder createWorkOrder(
      @Principal Tenant tenant, @RequestBody @Validated WorkOrderValue workOrderValue) {

    WorkOrder workOrder = workOrderService.createWorkOrder(tenant.getId(), workOrderValue);
    sendWOrkOrderSmsToProcessUser(workOrder);
    return workOrder;
  }

  private void sendWOrkOrderSmsToProcessUser(WorkOrder workOrder) {
    if (workOrder.getProcessorUserAccountId() != null) {
      String mobile =
          userAccountService.getUserAccountMobile(workOrder.getProcessorUserAccountId());
      if (StringUtils.isNotBlank(mobile)) {
        smsService.sendSms(mobile, YunPianMessageUtil.getAssignWorkOrderText(workOrder.getNo()));
      }
    }
  }

  @GetMapping
  @Operation(description = "工单列表")
  public IPage<ListWorkOrderResponse> listWorkOrder(
      @ModelAttribute @Validated(value = BaseParam.BaseParamVaGroup.class)
          ListWorkOrderRequest listWorkOrderRequest) {
    return workOrderViewService.listWorkOrder(listWorkOrderRequest);
  }

  @GetMapping("/-:source")
  @Operation(description = "根据工单来源,获取工单信息")
  public WorkOrderDetailResponse getWorkOrder(@ModelAttribute WorkOrder.Source source) {
    Optional<WorkOrder> workOrderOptional = workOrderRepository.findBySource(source);
    Problems.ensure(workOrderOptional.isPresent(), "工单信息不存在");
    return getWorkOrder(workOrderOptional.get());
  }

  @GetMapping("/{id}")
  @Operation(description = "工单详情")
  public WorkOrderDetailResponse getWorkOrder(@PathVariable("id") WorkOrder workOrder) {
    WorkOrderDetailResponse workOrderDetailResponse = new WorkOrderDetailResponse();
    workOrderDetailResponse.setWorkOrder(workOrder);

    Long assignerUserAccountId = workOrder.getAssignerUserAccountId();
    Long confirmFinishAccountId = workOrder.getConfirmFinishUserAccountId();
    Long[] userAccountIds = {workOrder.getProcessorUserAccountId(), assignerUserAccountId,confirmFinishAccountId};
    Map<Long, String> userAccountIdAndStaffNameMap =
        userAccountViewService.userAccountIdAndStaffNamePair(userAccountIds);

    workOrderDetailResponse.setProcessorStaffName(
        userAccountIdAndStaffNameMap.get(workOrder.getProcessorUserAccountId()));
    if (workOrder.getType() == DEVICE_MAINTAIN) {
      workOrderDetailResponse.setDeviceName(
          deviceViewService.getDeviceName(workOrder.getDeviceId(), workOrder.getDeviceSource()));
      workOrderDetailResponse.setDeviceLocation(
          deviceViewService.getDeviceLocation(
              workOrder.getDeviceId(), workOrder.getDeviceSource()));
    }
    workOrderDetailResponse.setAssignerStaffName(
        userAccountIdAndStaffNameMap.get(assignerUserAccountId));
    workOrderDetailResponse.setConfirmFinishStaffName(
            userAccountIdAndStaffNameMap.get(confirmFinishAccountId));
    if (assignerUserAccountId != null) {
      workOrderDetailResponse.setAssignerPhone(
          userAccountService.getUserAccountMobile(assignerUserAccountId));
    }
    return workOrderDetailResponse;
  }

  @PutMapping("/{id}")
  @Operation(description = "修改工单")
  public WorkOrder updateWorkOrder(
      @Principal Tenant tenant,
      @PathVariable("id") WorkOrder workOrder,
      @Valid @RequestBody WorkOrderUpdate workOrderUpdate) {
    Problems.ensure(workOrder.canChange(), "当前工单不支持修改");
    if(workOrder.getStatus()==AWAITING_PROCESSING ){
      Problems.ensure(
          workOrderUpdate.getProcessorUserAccountId() != null,
          "工单状态:" + WorkOrder.getStatusDes(workOrder.getStatus()) + " 不能删除处理人");
    }

    Long processorUserAccountId = workOrderUpdate.getProcessorUserAccountId();

    boolean assignProcessUser =
        processorUserAccountId != null
            && ObjectUtils.notEqual(processorUserAccountId, workOrder.getProcessorUserAccountId());
    if (assignProcessUser) {
      Problems.ensure(workOrder.canAssign(),"当前工单状态:"+ WorkOrder.getStatusDes(workOrder.getStatus())+" 不支持指派新的处理人");
      workOrderService.assignProcessor(
              tenant.getId(),
              workOrder,
              new WorkOrderAssignProcessor().setProcessorUserAccountId(processorUserAccountId));
    }

    workOrder = workOrderRepository.save(workOrderUpdate.assignTo(workOrder));

    if (assignProcessUser) {
      sendWOrkOrderSmsToProcessUser(workOrder);
    }
    return workOrder;
  }

  @PutMapping("/{id}:close")
  @Operation(description = "关闭工单")
  public void closeWorkOrder(
      @Principal Tenant tenant,
      @PathVariable("id") WorkOrder workOrder,
      @Valid @RequestBody WorkOrderClose workOrderClose) {
    workOrderService.closeWorkOrder(tenant.getId(), workOrder, workOrderClose);
  }


  @PutMapping("/{id}:handle")
  @Operation(description = "开始处理工单")
  public void handle(@Principal Tenant tenant, @PathVariable("id") WorkOrder workOrder) {
    workOrderService.handleWorkOrder(tenant.getId(), workOrder);
  }

  @PutMapping("/{id}:finish")
  @Operation(description = "完成工单处理")
  public void finish(
      @Principal Tenant tenant,
      @PathVariable("id") WorkOrder workOrder,
      @RequestBody WorkOrderFinish workOrderFinish) {
    workOrderService.finishWorkOrder(tenant.getId(), workOrder, workOrderFinish);
  }

  @PutMapping("/{id}:confirmFinish")
  @Operation(description = "确认完成工单")
  public void confirmFinish(
      @Principal Tenant tenant,
      @PathVariable("id") WorkOrder workOrder) {
    workOrderService.confirmFinish(tenant.getId(), workOrder);
  }
}
