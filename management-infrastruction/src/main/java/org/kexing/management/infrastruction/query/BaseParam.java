package org.kexing.management.infrastruction.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseParam {
  private String searchField;

  @NotNull(
      groups = {BaseParamVaGroup.class},
      message = "每页数量不能为空")
  @Null(
      groups = {AllRecordParamVaGroup.class},
      message = "全量获取参数,不支持分页")
  private Long size;

  @NotNull(
      groups = {BaseParamVaGroup.class},
      message = "当前页不能为空")
  @Null(
      groups = {AllRecordParamVaGroup.class},
      message = "全量获取参数,不支持分页")
  private Long current;

  public interface BaseParamVaGroup {}

  public interface AllRecordParamVaGroup {}
}
