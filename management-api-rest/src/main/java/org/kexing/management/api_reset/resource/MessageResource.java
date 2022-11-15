package org.kexing.management.api_reset.resource;

import com.yunmo.domain.common.Principal;
import com.yunmo.domain.common.Tenant;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kexing.management.domin.model.mysql.Message;
import org.kexing.management.domin.service.MessageService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lh
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Transactional
@Tag(name = "消息资源")
public class MessageResource {
  public final MessageService messageService;

  @GetMapping("/recent")
  public List<Message> listRecentMessage(@Principal Tenant tenant) {
    return messageService.listRecentMessage();
  }

}
