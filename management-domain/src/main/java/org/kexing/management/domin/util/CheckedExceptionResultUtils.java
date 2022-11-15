package org.kexing.management.domin.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;

import java.time.Instant;

/** @author lh */
@Schema(description = "浇注机上传数据检查不和规范的结果")
public class CheckedExceptionResultUtils {

  public static Pair<Boolean,UploadDateWithConform<Double>> staticMixerTemperatureChecked(
          ErpDeviceConfig.Temperature expectedTemperature,
          Double uploadTemperature) {
    if(ObjectUtils.isEmpty(uploadTemperature)){
      return Pair.with(null,null);
    }
    Boolean uploadDataConformity = null;
    if (ObjectUtils.isNotEmpty(uploadTemperature) && ObjectUtils.isNotEmpty(expectedTemperature)) {
      uploadDataConformity = expectedTemperature.contain(uploadTemperature);
    }
    return Pair.with(
            uploadDataConformity,
            UploadDateWithConform.<Double>builder()
                    .conform(uploadDataConformity)
                    .uploadData(uploadTemperature)
                    .build());
  }

  public static Pair<Boolean,UploadDateWithConform<Double>> vacuumPouringChecked(
          ErpDeviceConfig.Vacuum expectedVacuum,
          Double uploadVacuum) {
    if(ObjectUtils.isEmpty(uploadVacuum)){
      return Pair.with(null,null);
    }

    Boolean uploadDataConformity = null;
    if (ObjectUtils.isNotEmpty(uploadVacuum) && ObjectUtils.isNotEmpty(expectedVacuum)) {
      uploadDataConformity = expectedVacuum.contain(uploadVacuum);
    }
    return Pair.with(
            uploadDataConformity,
            UploadDateWithConform.<Double>builder()
                    .conform(uploadDataConformity)
                    .uploadData(uploadVacuum)
                    .build());
  }

  public static Pair<Boolean, UploadDateWithConform<Double>> temperatureCheck(
      ErpDeviceConfig.Temperature temperatureConfig, Double temperature) {

    Boolean temperatureCheckResult = null;

    UploadDateWithConform<Double> uploadDateWithConformTemperature = null;
    if (ObjectUtils.isNotEmpty(temperature)) {
      if (ObjectUtils.isNotEmpty(temperatureConfig)) {
        temperatureCheckResult = temperatureConfig.contain(temperature);
      }
      uploadDateWithConformTemperature =
              UploadDateWithConform.<Double>builder()
                      .conform(temperatureCheckResult)
                      .uploadData(temperature)
                      .build();
    }
    return Pair.with(temperatureCheckResult, uploadDateWithConformTemperature);
  }

  public static Triplet<Boolean, UploadDateWithConform<Double>, UploadDateWithConform<Double>>
      vacuumAndTemperaturePropertiesCheck(
          ErpDeviceConfig.VacuumAndTemperatureProperties vacuumAndTemperatureProperties,
          Double temperature,
          Double vacuum) {
    Boolean checkedResult = null;

    Pair<Boolean, UploadDateWithConform<Double>> temperatureCheckResult =
        temperatureCheck(vacuumAndTemperatureProperties.getTemperature(), temperature);
    UploadDateWithConform<Double> uploadDateWithConformTemperature =
        temperatureCheckResult.getValue1();
    checkedResult = getCheckedResult(checkedResult, temperatureCheckResult.getValue0());

    UploadDateWithConform<Double> uploadDateWithConformVacuum = null;
    if (ObjectUtils.isNotEmpty(vacuum)) {
      Boolean vacuumCheckedResult = null;
      if (ObjectUtils.isNotEmpty(vacuumAndTemperatureProperties.getVacuum())) {
        vacuumCheckedResult = vacuumAndTemperatureProperties.getVacuum().contain(vacuum);
        checkedResult = getCheckedResult(checkedResult, vacuumCheckedResult);
      }
      uploadDateWithConformVacuum =
          UploadDateWithConform.<Double>builder()
              .conform(vacuumCheckedResult)
              .uploadData(vacuum)
              .build();
    }

    return Triplet.with(
        checkedResult, uploadDateWithConformTemperature, uploadDateWithConformVacuum);
  }

  public static Boolean getCheckedResult(Boolean checkedResult, Boolean uploadCheckResult) {
    if (ObjectUtils.isEmpty(uploadCheckResult)) {
      return checkedResult;
    }
    if (ObjectUtils.isEmpty(checkedResult)) {
      return uploadCheckResult;
    }
    if (ObjectUtils.isNotEmpty(checkedResult) && !checkedResult) {
      return checkedResult;
    }
    return uploadCheckResult;
  }
  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UploadDateWithConform<T> {
    private Boolean conform;
    private T uploadData;
  }
}
