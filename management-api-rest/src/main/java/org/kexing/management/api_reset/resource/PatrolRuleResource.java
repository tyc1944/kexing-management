package org.kexing.management.api_reset.resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yunmo.domain.common.Events;
import com.yunmo.domain.common.Problems;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kexing.management.domin.event.RobotRuleEvent;
import org.kexing.management.domin.model.mysql.robot.PatrolRule;
import org.kexing.management.domin.model.mysql.robot.RobotPatrolLine;
import org.kexing.management.domin.model.mysql.robot.value.PatrolRuleCreate;
import org.kexing.management.domin.repository.mysql.PatrolRuleRepository;
import org.kexing.management.domin.util.NoGeneratorUtils;
import org.kexing.management.infrastruction.mapstruct.PageParamMapper;
import org.kexing.management.infrastruction.query.BaseParam;
import org.kexing.management.infrastruction.query.dto.ListPatrolRequest;
import org.kexing.management.infrastruction.repository.jpa.mysql.RobotPatrolLineJpaRepository;
import org.kexing.management.infrastruction.repository.mybatis.mysql.PatrolRuleQueryMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.kexing.management.domin.model.mysql.robot.PatrolRule.PatrolType.REPEAT_INSPECTION;

@RestController
@RequestMapping("/api/patrol/rules")
@RequiredArgsConstructor
@Transactional
@Tag(name = "巡检规则")
public class PatrolRuleResource {
  private final RobotPatrolLineJpaRepository robotPatrolLineRepository;
  private final PatrolRuleRepository patrolRuleRepository;
  private final PageParamMapper paramMapper;
  private final PatrolRuleQueryMapper patrolRuleQueryMapper;

  @GetMapping
  @Operation(description = "巡检规则列表")
  public IPage<PatrolRule> listPatrolRule(
      @Validated(value = {BaseParam.BaseParamVaGroup.class, Default.class}) @ModelAttribute
          ListPatrolRequest listPatrolRequest) {
    return patrolRuleQueryMapper.selectListPatrolPage(
        paramMapper.mapper(listPatrolRequest), listPatrolRequest);
  }

  @GetMapping("/{id}")
  @Operation(description = "某条日常巡检规则记录")
  public PatrolRule getPatrolRule(@PathVariable("id") Long id) {
    return patrolRuleRepository
        .findById(id)
        .orElseThrow(
            () -> {
              throw new RuntimeException("巡检规则不存在");
            });
  }

  @GetMapping("/usedTime")
  @Operation(description = "冲突时间列表")
  public List<PatrolRule.TaskStartTime> getUsedTimes() {
    return patrolRuleRepository.findByPatrolType(REPEAT_INSPECTION).stream()
        .flatMap(patrolRule -> patrolRule.getTaskStartTime().stream())
        .collect(Collectors.toList());
  }

  @PostMapping
  @Operation(description = "新增巡检规则")
  public PatrolRule creatPatrolRule(@Valid @RequestBody PatrolRuleCreate patrolRuleCreate) {
    assignPatrolRule(patrolRuleCreate);

    checkTaskStartTime(
        patrolRuleCreate.getPatrolType(), getUsedTimes(), patrolRuleCreate.getTaskStartTime());

    PatrolRule patrolRule = patrolRuleCreate.create();
    patrolRule.setTaskId(NoGeneratorUtils.noGenerator(NoGeneratorUtils.NoGeneratorPrefix.XJ));

    patrolRuleRepository.save(patrolRule);

    Events.post(RobotRuleEvent.AddEvent.builder().addedPatrolRule(patrolRule).build());
    return patrolRule;
  }

  private void assignPatrolRule(PatrolRuleCreate patrolRuleCreate) {
    RobotPatrolLine robotPatrolLine =
        robotPatrolLineRepository
            .findById(patrolRuleCreate.getRobotPatrolLine().getId())
            .orElseThrow(
                () -> {
                  throw Problems.hint("巡检路线不存在");
                });
    patrolRuleCreate.setRobotPatrolLine(robotPatrolLine);
  }

  private void checkTaskStartTime(
      PatrolRule.PatrolType patrolType,
      List<PatrolRule.TaskStartTime> oldTaskStartTimes,
      List<PatrolRule.TaskStartTime> newTaskStartTimes) {
    if (patrolType == REPEAT_INSPECTION) {
      Problems.ensure(!PatrolRule.existRepeatedStartTime(newTaskStartTimes), "设置的任务开始时间存在冲突");
      Problems.ensure(
          !PatrolRule.existRepeatedStartTime(
              Stream.concat(oldTaskStartTimes.stream(), newTaskStartTimes.stream())
                  .collect(Collectors.toList())),
          "新任务时间和已设置的任务开始时间冲突");
    }
  }

  @DeleteMapping("/{id}")
  @Operation(description = "删除巡检规则")
  public void deletePatrolRule(@PathVariable("id") PatrolRule patrolRule) {
    patrolRuleRepository.delete(patrolRule);
    Events.post(RobotRuleEvent.DeleteEvent.builder().deletedPatrolRule(patrolRule).build());
  }

  @PutMapping("/{id}")
  @Operation(description = "编辑巡检规则")
  public void updatePatrolRule(
      @PathVariable("id") PatrolRule patrolRule, @RequestBody PatrolRuleCreate patrolRuleCreate) {
    Problems.ensure(patrolRuleCreate.getPatrolType() == REPEAT_INSPECTION, "只支持日常规则的修改");
    assignPatrolRule(patrolRuleCreate);
    Problems.ensure(
        patrolRule
            .getRobotPatrolLine()
            .getPatrolNum()
            .equals(patrolRuleCreate.getRobotPatrolLine().getPatrolNum()),
        "不支持巡检路线变化");

    List<PatrolRule.TaskStartTime> usedTimes =
        getUsedTimes().stream()
            .filter(usedTime -> !patrolRule.getTaskStartTime().contains(usedTime))
            .collect(Collectors.toList());

    checkTaskStartTime(
        patrolRuleCreate.getPatrolType(), usedTimes, patrolRuleCreate.getTaskStartTime());

    List<PatrolRule.TaskStartTime> oldTaskStartTime =
        new ArrayList<>(patrolRule.getTaskStartTime());

    patrolRuleRepository.save(patrolRuleCreate.assignTo(patrolRule));

    Events.post(
        RobotRuleEvent.UpdateEvent.builder()
            .oldTaskStartTimes(oldTaskStartTime)
            .updatedPatrolRule(patrolRule)
            .build());
  }
}
