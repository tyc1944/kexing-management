package org.kexing.management.domin.model.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;
import org.kexing.management.domin.service.ErpDeviceRunStatusConfigService;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.kexing.management.domin.util.CheckedExceptionResultUtils.*;

@Setter
@Getter
/** @author lh */
@Schema(description = "浇注机户内上传数据")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JZHuNeiDataUploadDate {
  private Instant credate;

  @Schema(description = "填料灌真空")
  private Double filterJarVacuum;

  @Schema(description = "填料灌温度")
  private Double filterJarTemperature;

  @Schema(description = "固化剂灌A真空")
  private Double hardenerJarAVacuum;

  @Schema(description = "固化剂灌B真空")
  private Double hardenerJarBVacuum;

  @Schema(description = "固化剂灌A温度")
  private Double hardenerJarATemperature;

  @Schema(description = "固化剂灌B温度")
  private Double hardenerJarBTemperature;

  @Schema(description = "树脂罐A真空")
  private Double resinJarAVacuum;

  @Schema(description = "树脂罐B真空")
  private Double resinJarBVacuum;

  @Schema(description = "树脂罐A温度")
  private Double resinJarATemperature;

  @Schema(description = "树脂罐B温度")
  private Double resinJarBTemperature;

  @Schema(description = "浇注罐A真空")
  private Double pouringJarAVacuum;

  @Schema(description = "浇注罐B真空")
  private Double pouringJarBVacuum;

  @Schema(description = "静态混料器温度")
  private Double staticMixerTemperature;

  public static List<JZHuNeiDataUploadDate> convert(List<TagUIDUploadData> list) {
    Map<Instant, List<TagUIDUploadData>> groupMap =
        list.stream().collect(Collectors.groupingBy(TagUIDUploadData::getUpdateTime));
    return groupMap.entrySet().stream()
        .map(
            instantListEntry -> {
              JZHuNeiDataUploadDate jzHuNeiDataUploadDate = new JZHuNeiDataUploadDate();
              jzHuNeiDataUploadDate.setCredate(instantListEntry.getKey());

              tagUIDUploadDataToJZHuNeiDataUploadDate(instantListEntry.getValue(), jzHuNeiDataUploadDate);
              return jzHuNeiDataUploadDate;
            })
        .collect(Collectors.toList());
  }

  public static JZHuNeiDataUploadDate screen(
          List<TagUIDUploadData> list){
    List<TagUIDUploadData> tagUIDUploadDataList=list.stream().filter(tagUIDUploadData ->
            ErpDeviceRunStatusConfigService.isValidUploadDate(tagUIDUploadData.getUpdateTime())).collect(Collectors.toList());
    JZHuNeiDataUploadDate jzHuNeiDataUploadDate=new JZHuNeiDataUploadDate();
    tagUIDUploadDataToJZHuNeiDataUploadDate(tagUIDUploadDataList,jzHuNeiDataUploadDate);
    return jzHuNeiDataUploadDate;
  }

  private static void tagUIDUploadDataToJZHuNeiDataUploadDate(List<TagUIDUploadData> instantListEntry, JZHuNeiDataUploadDate jzHuNeiDataUploadDate) {
    for (TagUIDUploadData tagUIDUploadData : instantListEntry) {
      switch (tagUIDUploadData.getTagUID()) {
        case "AIO8工况网关\\JZ0002\\填料罐真空度":
          jzHuNeiDataUploadDate.setFilterJarVacuum(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\填料罐罐壁温度":
          jzHuNeiDataUploadDate.setFilterJarTemperature(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\固化剂罐24.1真空实际值":
          jzHuNeiDataUploadDate.setHardenerJarAVacuum(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\固化剂罐4.1真空实际值":
          jzHuNeiDataUploadDate.setHardenerJarBVacuum(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\固化剂罐24.1温度实际值":
          jzHuNeiDataUploadDate.setHardenerJarATemperature(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\固化剂罐4.1温度实际值":
          jzHuNeiDataUploadDate.setHardenerJarBTemperature(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\树脂罐2.1真空实际值":
          jzHuNeiDataUploadDate.setResinJarAVacuum(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\树脂罐22.1真空实际值":
          jzHuNeiDataUploadDate.setResinJarBVacuum(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\树脂罐2.1温度实际值":
          jzHuNeiDataUploadDate.setResinJarATemperature(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\树脂罐22.1温度实际值":
          jzHuNeiDataUploadDate.setResinJarBTemperature(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\浇注罐1.1A真空实际值":
          jzHuNeiDataUploadDate.setPouringJarAVacuum(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\浇注罐1.1B真空实际值":
          jzHuNeiDataUploadDate.setPouringJarBVacuum(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0002\\静态混料器温度实际值":
          jzHuNeiDataUploadDate.setStaticMixerTemperature(
              tagUIDUploadData.getDoubleUploadNumber());
          break;
      }
    }
  }

  public Pair<Boolean, HuNeiCheckedExceptionResult> inconformity(
      ErpDeviceConfig.JZHuNeiDeviceConfig jzHuneiDeviceConfig) {
    Boolean conformity = null;
    HuNeiCheckedExceptionResult checkedExceptionResult = new HuNeiCheckedExceptionResult();
    checkedExceptionResult.setCredate(credate);

    ErpDeviceConfig.VacuumAndTemperatureProperties expectFilterJar =
        jzHuneiDeviceConfig.getFilterJar();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectHardenerJarA =
        jzHuneiDeviceConfig.getHardenerJarA();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectHardenerJarB =
        jzHuneiDeviceConfig.getHardenerJarB();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectResinJarA =
        jzHuneiDeviceConfig.getResinJarA();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectResinJarB =
        jzHuneiDeviceConfig.getResinJarB();

    Triplet<Boolean, UploadDateWithConform<Double>, UploadDateWithConform<Double>> quartet =
        vacuumAndTemperaturePropertiesCheck(expectFilterJar, filterJarTemperature, filterJarVacuum);
    checkedExceptionResult.setFilterJarTemperature(quartet.getValue1());
    checkedExceptionResult.setFilterJarVacuum(quartet.getValue2());
    conformity = getCheckedResult(conformity, quartet.getValue0());

    quartet =
        vacuumAndTemperaturePropertiesCheck(
            expectHardenerJarA, hardenerJarATemperature, hardenerJarAVacuum);
    checkedExceptionResult.setHardenerJarATemperature(quartet.getValue1());
    checkedExceptionResult.setHardenerJarAVacuum(quartet.getValue2());
    conformity = getCheckedResult(conformity, quartet.getValue0());

    quartet =
        vacuumAndTemperaturePropertiesCheck(
            expectHardenerJarB, hardenerJarBTemperature, hardenerJarBVacuum);
    checkedExceptionResult.setHardenerJarBTemperature(quartet.getValue1());
    checkedExceptionResult.setHardenerJarBVacuum(quartet.getValue2());
    conformity = getCheckedResult(conformity, quartet.getValue0());

    quartet =
        vacuumAndTemperaturePropertiesCheck(
            expectResinJarA, resinJarATemperature, resinJarAVacuum);
    checkedExceptionResult.setResinJarATemperature(quartet.getValue1());
    checkedExceptionResult.setResinJarAVacuum(quartet.getValue2());
    conformity = getCheckedResult(conformity, quartet.getValue0());

    quartet =
        vacuumAndTemperaturePropertiesCheck(
            expectResinJarB, resinJarBTemperature, resinJarBVacuum);
    checkedExceptionResult.setResinJarBTemperature(quartet.getValue1());
    checkedExceptionResult.setResinJarBVacuum(quartet.getValue2());
    conformity = getCheckedResult(conformity, quartet.getValue0());

    Pair<Boolean, UploadDateWithConform<Double>> uploadDateWithConformVacuumPouringJarAPair =
        vacuumPouringChecked(jzHuneiDeviceConfig.getPouringJarAVacuum(), pouringJarAVacuum);
    checkedExceptionResult.setPouringJarAVacuum(uploadDateWithConformVacuumPouringJarAPair.getValue1());
    conformity = getCheckedResult(conformity, uploadDateWithConformVacuumPouringJarAPair.getValue0());

    Pair<Boolean, UploadDateWithConform<Double>> uploadDateWithConformVacuumPouringJarBPair =
            vacuumPouringChecked(jzHuneiDeviceConfig.getPouringJarBVacuum(), pouringJarBVacuum);
    checkedExceptionResult.setPouringJarBVacuum(uploadDateWithConformVacuumPouringJarBPair.getValue1());
    conformity = getCheckedResult(conformity,uploadDateWithConformVacuumPouringJarBPair.getValue0());

    Pair<Boolean,UploadDateWithConform<Double>>  uploadDateWithConformStaticMixerTemperaturePair = staticMixerTemperatureChecked(jzHuneiDeviceConfig.getStaticMixerTemperature(),staticMixerTemperature);
    checkedExceptionResult.setStaticMixerTemperature(uploadDateWithConformStaticMixerTemperaturePair.getValue1());
    conformity = getCheckedResult(conformity,uploadDateWithConformStaticMixerTemperaturePair.getValue0());
    return  Pair.with(conformity, checkedExceptionResult);
  }




  @Setter
  @Getter
  public static class HuNeiCheckedExceptionResult {
    private Instant credate;

    @Schema(description = "填料灌真空")
    private UploadDateWithConform<Double> filterJarVacuum;

    @Schema(description = "填料灌温度")
    private UploadDateWithConform<Double> filterJarTemperature;

    @Schema(description = "固化剂灌A真空")
    private UploadDateWithConform<Double> hardenerJarAVacuum;

    @Schema(description = "固化剂灌B真空")
    private UploadDateWithConform<Double> hardenerJarBVacuum;

    @Schema(description = "固化剂灌A温度")
    private UploadDateWithConform<Double> hardenerJarATemperature;

    @Schema(description = "固化剂灌B温度")
    private UploadDateWithConform<Double> hardenerJarBTemperature;

    @Schema(description = "树脂罐A真空")
    private UploadDateWithConform<Double> resinJarAVacuum;

    @Schema(description = "树脂罐B真空")
    private UploadDateWithConform<Double> resinJarBVacuum;

    @Schema(description = "树脂罐A温度")
    private UploadDateWithConform<Double> resinJarATemperature;

    @Schema(description = "树脂罐B温度")
    private UploadDateWithConform<Double> resinJarBTemperature;

    @Schema(description = "浇注罐A真空")
    private UploadDateWithConform<Double> pouringJarAVacuum;

    @Schema(description = "浇注罐B真空")
    private UploadDateWithConform<Double> pouringJarBVacuum;

    @Schema(description = "静态混料器温度")
    private UploadDateWithConform<Double> staticMixerTemperature;
  }
}
