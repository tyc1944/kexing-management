package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.Message;

import java.util.List;

/** @author lh */
public interface MessageRepository extends EntityRepository<Message, Long> {
    List<Message> findFirst20ByOrderByCreatedDateDesc();
}
