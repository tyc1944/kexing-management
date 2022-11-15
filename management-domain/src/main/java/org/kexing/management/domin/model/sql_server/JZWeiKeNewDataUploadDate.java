package org.kexing.management.domin.model.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;
import org.kexing.management.domin.service.ErpDeviceRunStatusConfigService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.kexing.management.domin.util.CheckedExceptionResultUtils.*;

@Setter
@Getter
/** @author lh */
@Schema(description = "浇注机微克新上传数据")
public class JZWeiKeNewDataUploadDate {
  private Instant credate;

  @Schema(description = "固化剂罐真空")
  private Double hardenerJarVacuum;

  @Schema(description = "固化剂罐温度")
  private Double hardenerJarTemperature;

  @Schema(description = "树脂罐真空配置")
  private Double resinJarVacuum;

  @Schema(description = "树脂罐A温度")
  private Double resinJarATemperature;

  @Schema(description = "树脂罐B温度")
  private Double resinJarBTemperature;

  @Schema(description = "填料罐真空")
  private Double filterJarVacuum;

  @Schema(description = "填料罐温度")
  private Double filterJarTemperature;

  @Schema(description = "浇注罐A真空")
  private Double pouringJarAVacuum;

  @Schema(description = "浇注罐B真空")
  private Double pouringJarBVacuum;

  @Schema(description = "静态混料器A温度")
  private Double staticMixerATemperature;

  @Schema(description = "静态混料器B温度")
  private Double staticMixerBTemperature;

  public static List<JZWeiKeNewDataUploadDate> convert(List<TagUIDUploadData> list) {

    Map<Instant, List<TagUIDUploadData>> groupMap =
            list.stream().collect(Collectors.groupingBy(TagUIDUploadData::getUpdateTime));
    return groupMap.entrySet().stream()
            .map(
                    instantListEntry -> {
                      JZWeiKeNewDataUploadDate jzWeiKeOldDataUploadDate = new JZWeiKeNewDataUploadDate();
                      jzWeiKeOldDataUploadDate.setCredate(instantListEntry.getKey());

                      tagUIDUploadDataToJZWeiKeNewDataUploadDate(instantListEntry.getValue(), jzWeiKeOldDataUploadDate);
                      return jzWeiKeOldDataUploadDate;
                    })
            .collect(Collectors.toList());  }

  public static JZWeiKeNewDataUploadDate screen(
          List<TagUIDUploadData> list){
    List<TagUIDUploadData> tagUIDUploadDataList=list.stream().filter(tagUIDUploadData ->
            ErpDeviceRunStatusConfigService.isValidUploadDate(tagUIDUploadData.getUpdateTime())).collect(Collectors.toList());
    JZWeiKeNewDataUploadDate jzWeiKeNewDataUploadDate=new JZWeiKeNewDataUploadDate();
    tagUIDUploadDataToJZWeiKeNewDataUploadDate(tagUIDUploadDataList,jzWeiKeNewDataUploadDate);
    return jzWeiKeNewDataUploadDate;
  }

  private static void tagUIDUploadDataToJZWeiKeNewDataUploadDate(List<TagUIDUploadData> instantListEntry, JZWeiKeNewDataUploadDate jzWeiKeOldDataUploadDate) {
    for (TagUIDUploadData tagUIDUploadData : instantListEntry) {
      switch (tagUIDUploadData.getTagUID()) {
        case "AIO8工况网关\\JZ0004\\固化剂6.1实际温度":
          jzWeiKeOldDataUploadDate.setHardenerJarTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0004\\固化剂真空":
          jzWeiKeOldDataUploadDate.setHardenerJarVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0004\\混料器10.3A实际温度":
          jzWeiKeOldDataUploadDate.setStaticMixerATemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0004\\混料器10.3B实际温度":
          jzWeiKeOldDataUploadDate.setStaticMixerBTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0004\\树脂23.1实际温度":
          jzWeiKeOldDataUploadDate.setResinJarBTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0004\\树脂3.1实际温度":
          jzWeiKeOldDataUploadDate.setResinJarATemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0004\\树脂罐真空":
          jzWeiKeOldDataUploadDate.setResinJarVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0004\\填料18.1实际温度":
          jzWeiKeOldDataUploadDate.setFilterJarTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0004\\填料罐真空":
          jzWeiKeOldDataUploadDate.setFilterJarVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0004\\右浇注真空":
          jzWeiKeOldDataUploadDate.setPouringJarAVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0004\\左浇注真空":
          jzWeiKeOldDataUploadDate.setPouringJarBVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
      }
    }
  }

  public Pair<Boolean, WeiKeNewCheckedExceptionResult> inconformity(ErpDeviceConfig.JZWeiKeNewDeviceConfig jzWeiKeNewDeviceConfig) {
        Boolean conformity = null;
    WeiKeNewCheckedExceptionResult checkedExceptionResult = new WeiKeNewCheckedExceptionResult();
    checkedExceptionResult.setCredate(credate);

    ErpDeviceConfig.VacuumAndTemperatureProperties expectHardenerJar =
            jzWeiKeNewDeviceConfig.getHardenerJar();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectResinJarA =
            jzWeiKeNewDeviceConfig.getResinJarA();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectResinJarB =
            jzWeiKeNewDeviceConfig.getResinJarB();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectFilterJar =
        jzWeiKeNewDeviceConfig.getFilterJar();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectPouringJarA =
        jzWeiKeNewDeviceConfig.getPouringJarA();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectPouringJarB =
            jzWeiKeNewDeviceConfig.getPouringJarB();

    Triplet<
            Boolean,
            UploadDateWithConform<Double>,
            UploadDateWithConform<Double>>
            triplet =
            vacuumAndTemperaturePropertiesCheck(
                    expectHardenerJar, hardenerJarTemperature, hardenerJarVacuum);
    checkedExceptionResult.setHardenerJarTemperature(triplet.getValue1());
    checkedExceptionResult.setHardenerJarVacuum(triplet.getValue2());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    triplet =
            vacuumAndTemperaturePropertiesCheck(
                    expectResinJarA, resinJarATemperature, null);
    checkedExceptionResult.setResinJarATemperature(triplet.getValue1());
    conformity = getCheckedResult(conformity, triplet.getValue0());
    triplet =
            vacuumAndTemperaturePropertiesCheck(
                    expectResinJarB, resinJarBTemperature, null );
    checkedExceptionResult.setResinJarBTemperature(triplet.getValue1());
    conformity = getCheckedResult(conformity, triplet.getValue0());
    Pair<Boolean, UploadDateWithConform<Double>> resinJarVacuumCheckPair =
            vacuumPouringChecked(
                    jzWeiKeNewDeviceConfig.getResinJarVacuum() == null
                            ? null
                            : jzWeiKeNewDeviceConfig.getResinJarVacuum(),
                    resinJarVacuum);
    checkedExceptionResult.setResinJarVacuum(resinJarVacuumCheckPair.getValue1());
    conformity = getCheckedResult(conformity, resinJarVacuumCheckPair.getValue0());

    triplet =
            vacuumAndTemperaturePropertiesCheck(
                expectFilterJar, filterJarTemperature, filterJarVacuum);
    checkedExceptionResult.setFilterJarTemperature(triplet.getValue1());
    checkedExceptionResult.setFilterJarVacuum(triplet.getValue2());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    triplet =
            vacuumAndTemperaturePropertiesCheck(
                    expectPouringJarA, null,pouringJarAVacuum);
    checkedExceptionResult.setPouringJarAVacuum(triplet.getValue2());
    conformity = getCheckedResult(conformity, triplet.getValue0());
    triplet =
            vacuumAndTemperaturePropertiesCheck(
                    expectPouringJarB, null, pouringJarBVacuum);
    checkedExceptionResult.setPouringJarBVacuum(triplet.getValue2());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    Pair<Boolean,UploadDateWithConform<Double>>  uploadDateWithConformStaticMixerATemperaturePair = staticMixerTemperatureChecked(jzWeiKeNewDeviceConfig.getStaticMixerATemperature(),staticMixerATemperature);
    checkedExceptionResult.setStaticMixerATemperature(uploadDateWithConformStaticMixerATemperaturePair.getValue1());
    conformity = getCheckedResult(conformity,uploadDateWithConformStaticMixerATemperaturePair.getValue0());

    Pair<Boolean,UploadDateWithConform<Double>>  uploadDateWithConformStaticMixerBTemperaturePair = staticMixerTemperatureChecked(jzWeiKeNewDeviceConfig.getStaticMixerBTemperature(),staticMixerBTemperature);
    checkedExceptionResult.setStaticMixerBTemperature(uploadDateWithConformStaticMixerBTemperaturePair.getValue1());
    conformity = getCheckedResult(conformity,uploadDateWithConformStaticMixerBTemperaturePair.getValue0());

    return  Pair.with(conformity, checkedExceptionResult);
  }

  @Setter
  @Getter
  public static class WeiKeNewCheckedExceptionResult {

    private Instant credate;

    @Schema(description = "固化剂罐真空")
    private UploadDateWithConform<Double> hardenerJarVacuum;

    @Schema(description = "固化剂罐温度")
    private UploadDateWithConform<Double> hardenerJarTemperature;

    @Schema(description = "树脂罐真空配置")
    private UploadDateWithConform<Double> resinJarVacuum;

    @Schema(description = "树脂罐A温度")
    private UploadDateWithConform<Double> resinJarATemperature;

    @Schema(description = "树脂罐B温度")
    private UploadDateWithConform<Double> resinJarBTemperature;

    @Schema(description = "填料罐真空")
    private UploadDateWithConform<Double> filterJarVacuum;

    @Schema(description = "填料罐温度")
    private UploadDateWithConform<Double> filterJarTemperature;

    @Schema(description = "浇注罐A真空")
    private UploadDateWithConform<Double> pouringJarAVacuum;

    @Schema(description = "浇注罐B真空")
    private UploadDateWithConform<Double> pouringJarBVacuum;

    @Schema(description = "静态混料器A温度")
    private UploadDateWithConform<Double> staticMixerATemperature;

    @Schema(description = "静态混料器B温度")
    private UploadDateWithConform<Double> staticMixerBTemperature;


  }
}
