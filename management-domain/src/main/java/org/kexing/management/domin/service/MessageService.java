package org.kexing.management.domin.service;

import com.yunmo.domain.common.Problems;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.kexing.management.domin.model.mysql.Message;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.domin.repository.mysql.MessageRepository;
import org.kexing.management.domin.util.DefaultUserAccount;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/** @author lh */
@Service
@RequiredArgsConstructor
public class MessageService {
  private static final String WORK_ORDER_PROCESS_TEMPLATE = "{0}处理工单编号：{1}，此项工单请及时关注";
  private static final String WORK_ORDER_PROCESSED_TEMPLATE = "{0}完成工单编号：{1}，此项工单请及时关注";
  private static final String WORK_ORDER_CREATE_TEMPLATE = "{0}新建工单编号：{1}，此项工单请及时关注";
  private static final String WORK_ORDER_CLOSE_TEMPLATE = "{0}关闭工单编号：{1}，此项工单请及时关注";
  public final MessageRepository messageRepository;
  public final UserAccountService userAccountService;

  public void createWorkOrderMessage(
      long operator, long workOrderId, String workOrderNo, WorkOrder.Status status) {
    Problems.ensure(ObjectUtils.isNotEmpty(workOrderNo), "工单编号不能为空");

    String creatorName = null;
    if (DefaultUserAccount.DEFAULT_SYSTEM_ACCOUNT_ID == operator) {
      creatorName = "系统触发";
    } else {
      creatorName = userAccountService.getStaffName(operator);
    }

    String content = null;
    Message.Type type;
    switch (status) {
      case AWAITING_ASSIGN:
        type = Message.Type.WORK_ORDER_CREATE;
        content = MessageFormat.format(WORK_ORDER_CREATE_TEMPLATE, creatorName, workOrderNo);
        break;
      case PROCESSING:
        type = Message.Type.WORK_ORDER_PROCESS;
        content = MessageFormat.format(WORK_ORDER_PROCESS_TEMPLATE, creatorName, workOrderNo);
        break;
      case FINISHED:
        type = Message.Type.WORK_ORDER_FINISH;
        content = MessageFormat.format(WORK_ORDER_PROCESSED_TEMPLATE, creatorName, workOrderNo);
        break;
      case CLOSED:
        type = Message.Type.WORK_ORDER_CLOSE;
        content = MessageFormat.format(WORK_ORDER_CLOSE_TEMPLATE, creatorName, workOrderNo);
        break;
      default:
        return;
    }
    messageRepository.save(
        Message.builder()
            .content(content)
            .type(type)
            .originalId(workOrderId)
            .belongAccountId(operator)
            .build());
  }

  public List<Message> listRecentMessage() {
    return messageRepository.findFirst20ByOrderByCreatedDateDesc();
  }
}
