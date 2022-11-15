package org.kexing.management.infrastruction.service;

import lombok.RequiredArgsConstructor;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.repository.mysql.PatrolRuleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatrolRuleService {
    private final PatrolRuleRepository patrolRuleRepository;

    public void repeatTime(PatrolRule.TaskStartTime taskStartTime) {
        for (PatrolRule patrolRule : patrolRuleRepository.findAll()) {
            if (patrolRule.getTaskStartTime().equals(taskStartTime)) {
                throw new RuntimeException("任务时间冲突");
            }
        }
    }
}
