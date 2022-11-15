package org.kexing.management.domin.model.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;
import org.kexing.management.domin.service.ErpDeviceRunStatusConfigService;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.kexing.management.domin.util.CheckedExceptionResultUtils.*;

@Setter
@Getter
/** @author lh */
@Schema(description = "浇注机微克旧上传数据")
public class JZWeiKeOldDataUploadDate {
  private Instant credate;

  @Schema(description = "填料罐真空")
  private Double filterJarVacuum;

  @Schema(description = "填料罐温度")
  private Double filterJarTemperature;

  @Schema(description = "填料罐壁温度")
  private Double filterJarWallTemperature;

  @Schema(description = "固化剂罐A真空")
  private Double hardenerJarAVacuum;

  @Schema(description = "固化剂罐B真空")
  private Double hardenerJarBVacuum;

  @Schema(description = "固化剂罐A温度")
  private Double hardenerJarATemperature;

  @Schema(description = "固化剂罐B温度")
  private Double hardenerJarBTemperature;

  @Schema(description = "树脂罐A真空")
  private Double resinJarAVacuum;

  @Schema(description = "树脂罐B真空")
  private Double resinJarBVacuum;

  @Schema(description = "树脂罐A温度")
  private Double resinJarATemperature;

  @Schema(description = "树脂罐B温度")
  private Double resinJarBTemperature;

  @Schema(description = "浇注箱A真空")
  private Double pouringJarAVacuum;

  @Schema(description = "浇注箱B真空")
  private Double pouringJarBVacuum;

  @Schema(description = "静态混料器温度")
  private Double staticMixerTemperature;

  @Valid
  @Schema(description = "浇注罐左模具温度配置")
  private Double pouringJarLeftMoldsTemperature;

  @Valid
  @Schema(description = "浇注罐右模具温度配置")
  private Double pouringJarRightMoldsTemperature;

  public static List<JZWeiKeOldDataUploadDate> convert(List<TagUIDUploadData> list) {

    Map<Instant, List<TagUIDUploadData>> groupMap =
            list.stream().collect(Collectors.groupingBy(TagUIDUploadData::getUpdateTime));
    return groupMap.entrySet().stream()
            .map(
                    instantListEntry -> {
                      JZWeiKeOldDataUploadDate jzWeiKeOldDataUploadDate = new JZWeiKeOldDataUploadDate();
                      jzWeiKeOldDataUploadDate.setCredate(instantListEntry.getKey());
                      tagUIDUploadDataToJZWeiKeOldDataUploadDate(instantListEntry.getValue(), jzWeiKeOldDataUploadDate);
                      return jzWeiKeOldDataUploadDate;
                    })
            .collect(Collectors.toList());  }

  public static JZWeiKeOldDataUploadDate screen(
          List<TagUIDUploadData> list){
    List<TagUIDUploadData> tagUIDUploadDataList=list.stream().filter(tagUIDUploadData ->
            ErpDeviceRunStatusConfigService.isValidUploadDate(tagUIDUploadData.getUpdateTime())).collect(Collectors.toList());
    JZWeiKeOldDataUploadDate jzWeiKeOldDataUploadDate=new JZWeiKeOldDataUploadDate();
    tagUIDUploadDataToJZWeiKeOldDataUploadDate(tagUIDUploadDataList,jzWeiKeOldDataUploadDate);
    return jzWeiKeOldDataUploadDate;
  }
  private static void tagUIDUploadDataToJZWeiKeOldDataUploadDate(List<TagUIDUploadData> instantListEntry, JZWeiKeOldDataUploadDate jzWeiKeOldDataUploadDate) {
    for (TagUIDUploadData tagUIDUploadData : instantListEntry) {
      switch (tagUIDUploadData.getTagUID()) {
        case "AIO8工况网关\\JZ0001\\填料罐真空度":
          jzWeiKeOldDataUploadDate.setFilterJarVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\填料罐干燥温度":
          jzWeiKeOldDataUploadDate.setFilterJarTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\填料罐罐壁温度":
          jzWeiKeOldDataUploadDate.setFilterJarWallTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\固化剂罐3.1A真空实际值":
          jzWeiKeOldDataUploadDate.setHardenerJarAVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\固化剂罐3.1B真空实际值":
          jzWeiKeOldDataUploadDate.setHardenerJarBVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\固化剂罐3.1A温度实际值":
          jzWeiKeOldDataUploadDate.setHardenerJarATemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\固化剂罐3.1B温度实际值":
          jzWeiKeOldDataUploadDate.setHardenerJarBTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\树脂罐3.1A真空实际值":
          jzWeiKeOldDataUploadDate.setResinJarAVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\树脂罐3.1B真空实际值":
          jzWeiKeOldDataUploadDate.setResinJarBVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\树脂罐3.1A温度实际值":
          jzWeiKeOldDataUploadDate.setResinJarATemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\树脂罐3.1B温度实际值":
          jzWeiKeOldDataUploadDate.setResinJarBTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\浇注罐1.1A真空实际值":
          jzWeiKeOldDataUploadDate.setPouringJarAVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\浇注罐1.1B真空实际值":
          jzWeiKeOldDataUploadDate.setPouringJarBVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\浇注右罐模具温度实际值":
          jzWeiKeOldDataUploadDate.setPouringJarRightMoldsTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\浇注左罐模具温度实际值":
          jzWeiKeOldDataUploadDate.setPouringJarLeftMoldsTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0001\\静态混料器温度实际值":
          jzWeiKeOldDataUploadDate.setStaticMixerTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
      }
    }
  }


  public Pair<Boolean, WeiKeOldCheckedExceptionResult> inconformity(ErpDeviceConfig.JZWeiKeOldDeviceConfig jzWeiKeOldDeviceConfig) {
        Boolean conformity = null;
    WeiKeOldCheckedExceptionResult checkedExceptionResult = new WeiKeOldCheckedExceptionResult();
    checkedExceptionResult.setCredate(credate);

    ErpDeviceConfig.VacuumAndTemperatureProperties expectFilterJar =
        jzWeiKeOldDeviceConfig.getFilterJar();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectHardenerJarA =
        jzWeiKeOldDeviceConfig.getHardenerJarA();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectHardenerJarB =
        jzWeiKeOldDeviceConfig.getHardenerJarB();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectResinJarA =
        jzWeiKeOldDeviceConfig.getResinJarA();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectResinJarB =
        jzWeiKeOldDeviceConfig.getResinJarB();

    Triplet<
                    Boolean,
                    UploadDateWithConform<Double>,
                    UploadDateWithConform<Double>>
        triplet =
            vacuumAndTemperaturePropertiesCheck(
                expectFilterJar, filterJarTemperature, filterJarVacuum);
    checkedExceptionResult.setFilterJarTemperature(triplet.getValue1());
    checkedExceptionResult.setFilterJarVacuum(triplet.getValue2());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    Pair<Boolean, UploadDateWithConform<Double>> wallTemperatureCheckPair =
        temperatureCheck(
            jzWeiKeOldDeviceConfig.getFilterJar() == null
                ? null
                : jzWeiKeOldDeviceConfig.getFilterJar().getWallTemperature(),
            filterJarWallTemperature);
    checkedExceptionResult.setFilterJarWallTemperature(wallTemperatureCheckPair.getValue1());
    conformity = getCheckedResult(conformity, wallTemperatureCheckPair.getValue0());

    triplet =
        vacuumAndTemperaturePropertiesCheck(
            expectHardenerJarA, hardenerJarATemperature, hardenerJarAVacuum);
    checkedExceptionResult.setHardenerJarATemperature(triplet.getValue1());
    checkedExceptionResult.setHardenerJarAVacuum(triplet.getValue2());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    triplet =
        vacuumAndTemperaturePropertiesCheck(
            expectHardenerJarB, hardenerJarBTemperature, hardenerJarBVacuum);
    checkedExceptionResult.setHardenerJarBTemperature(triplet.getValue1());
    checkedExceptionResult.setHardenerJarBVacuum(triplet.getValue2());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    triplet =
        vacuumAndTemperaturePropertiesCheck(
            expectResinJarA, resinJarATemperature, resinJarAVacuum);
    checkedExceptionResult.setResinJarATemperature(triplet.getValue1());
    checkedExceptionResult.setResinJarAVacuum(triplet.getValue2());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    triplet =
        vacuumAndTemperaturePropertiesCheck(
            expectResinJarB, resinJarBTemperature, resinJarBVacuum );
    checkedExceptionResult.setResinJarBTemperature(triplet.getValue1());
    checkedExceptionResult.setResinJarBVacuum(triplet.getValue2());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    Pair<Boolean, UploadDateWithConform<Double>> uploadDateWithConformVaccumPouringJarAPair =
        vacuumPouringChecked(jzWeiKeOldDeviceConfig.getPouringJarAVacuum(), pouringJarAVacuum);
    checkedExceptionResult.setPouringJarAVacuum(uploadDateWithConformVaccumPouringJarAPair.getValue1());
    conformity = getCheckedResult(conformity, uploadDateWithConformVaccumPouringJarAPair.getValue0());

    Pair<Boolean, UploadDateWithConform<Double>> uploadDateWithConformVaccumPouringJarBPair =
            vacuumPouringChecked(jzWeiKeOldDeviceConfig.getPouringJarBVacuum(), pouringJarBVacuum);
    checkedExceptionResult.setPouringJarBVacuum(uploadDateWithConformVaccumPouringJarBPair.getValue1());
    conformity = getCheckedResult(conformity,uploadDateWithConformVaccumPouringJarBPair.getValue0());
    
    Pair<Boolean, UploadDateWithConform<Double>> uploadDateWithTemperaturePouringJarLeftMoldsConformPair =
            temperatureCheck(jzWeiKeOldDeviceConfig.getPouringJarLeftMoldsTemperature(), pouringJarLeftMoldsTemperature);
    checkedExceptionResult.setPouringJarLeftMoldsTemperature(uploadDateWithTemperaturePouringJarLeftMoldsConformPair.getValue1());
    conformity = getCheckedResult(conformity,uploadDateWithTemperaturePouringJarLeftMoldsConformPair.getValue0());

    Pair<Boolean, UploadDateWithConform<Double>> uploadDateWithTemperaturePouringJarRightMoldsConformPair =
            temperatureCheck(jzWeiKeOldDeviceConfig.getPouringJarRightMoldsTemperature(), pouringJarRightMoldsTemperature);
    checkedExceptionResult.setPouringJarRightMoldsTemperature(uploadDateWithTemperaturePouringJarRightMoldsConformPair.getValue1());
    conformity = getCheckedResult(conformity,uploadDateWithTemperaturePouringJarRightMoldsConformPair.getValue0());

    Pair<Boolean,UploadDateWithConform<Double>>  uploadDateWithConformStaticMixerTemperaturePair = staticMixerTemperatureChecked(jzWeiKeOldDeviceConfig.getStaticMixerTemperature(),staticMixerTemperature);
    checkedExceptionResult.setStaticMixerTemperature(uploadDateWithConformStaticMixerTemperaturePair.getValue1());
    conformity = getCheckedResult(conformity,uploadDateWithConformStaticMixerTemperaturePair.getValue0());
    return  Pair.with(conformity, checkedExceptionResult);
  }

  @Setter
  @Getter
  public static class WeiKeOldCheckedExceptionResult {

    private Instant credate;

    @Schema(description = "填料罐真空")
    private UploadDateWithConform<Double> filterJarVacuum;

    @Schema(description = "填料罐温度")
    private UploadDateWithConform<Double> filterJarTemperature;

    @Schema(description = "填料罐壁温度")
    private UploadDateWithConform<Double> filterJarWallTemperature;

    @Schema(description = "固化剂罐A真空")
    private UploadDateWithConform<Double> hardenerJarAVacuum;

    @Schema(description = "固化剂罐B真空")
    private UploadDateWithConform<Double> hardenerJarBVacuum;

    @Schema(description = "固化剂罐A温度")
    private UploadDateWithConform<Double> hardenerJarATemperature;

    @Schema(description = "固化剂罐B温度")
    private UploadDateWithConform<Double> hardenerJarBTemperature;

    @Schema(description = "树脂罐A真空")
    private UploadDateWithConform<Double> resinJarAVacuum;

    @Schema(description = "树脂罐B真空")
    private UploadDateWithConform<Double> resinJarBVacuum;

    @Schema(description = "树脂罐A温度")
    private UploadDateWithConform<Double> resinJarATemperature;

    @Schema(description = "树脂罐B温度")
    private UploadDateWithConform<Double> resinJarBTemperature;

    @Schema(description = "浇注箱A真空")
    private UploadDateWithConform<Double> pouringJarAVacuum;

    @Schema(description = "浇注箱B真空")
    private UploadDateWithConform<Double> pouringJarBVacuum;

    @Schema(description = "静态混料器温度")
    private UploadDateWithConform<Double> staticMixerTemperature;

    @Valid
    @Schema(description = "浇注罐左模具温度配置")
    private UploadDateWithConform<Double> pouringJarLeftMoldsTemperature;

    @Valid
    @Schema(description = "浇注罐右模具温度配置")
    private UploadDateWithConform<Double> pouringJarRightMoldsTemperature;

  }
}
