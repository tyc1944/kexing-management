package org.kexing.management.domin.model.sql_server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.javatuples.Triplet;
import org.kexing.management.domin.service.ErpDeviceRunStatusConfigService;
import org.kexing.management.domin.util.CheckedExceptionResultUtils.UploadDateWithConform;
import lombok.Setter;
import org.javatuples.Pair;
import org.kexing.management.domin.model.mysql.ErpDeviceConfig;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.kexing.management.domin.util.CheckedExceptionResultUtils.*;

@Setter
@Getter
/** @author lh */
@Schema(description = "浇注机户外上传数据")
public class JZHuWaiDataUploadDate {
  private Instant credate;

  @Schema(description = "固化剂灌A真空")
  private Double hardenerJarAVacuum;

  @Schema(description = "固化剂灌A温度")
  private Double hardenerJarATemperature;

  @Schema(description = "固化剂灌B温度")
  private Double hardenerJarBTemperature;

  @Schema(description = "树脂罐A真空")
  private Double resinJarAVacuum;

  @Schema(description = "树脂罐A温度")
  private Double resinJarATemperature;

  @Schema(description = "树脂罐B温度")
  private Double resinJarBTemperature;

  @Schema(description = "浇注罐A真空")
  private Double pouringJarAVacuum;

  @Schema(description = "静态混料器温度")
  private Double staticMixerTemperature;

  public static List<JZHuWaiDataUploadDate> convert(List<TagUIDUploadData> list) {
    Map<Instant, List<TagUIDUploadData>> groupMap =
            list.stream().collect(Collectors.groupingBy(TagUIDUploadData::getUpdateTime));
    return groupMap.entrySet().stream()
            .map(
                    instantListEntry -> {
                      JZHuWaiDataUploadDate jzHuWaiDataUploadDate = new JZHuWaiDataUploadDate();
                      jzHuWaiDataUploadDate.setCredate(instantListEntry.getKey());

                      tagUIDUploadDataToJZHuWaiDataUploadDate(instantListEntry.getValue(), jzHuWaiDataUploadDate);
                      return jzHuWaiDataUploadDate;
                    })
            .collect(Collectors.toList());
  }

  public static JZHuWaiDataUploadDate screen(
          List<TagUIDUploadData> list){
    List<TagUIDUploadData> tagUIDUploadDataList=list.stream().filter(tagUIDUploadData ->
            ErpDeviceRunStatusConfigService.isValidUploadDate(tagUIDUploadData.getUpdateTime())).collect(Collectors.toList());
    JZHuWaiDataUploadDate jzHuWaiDataUploadDate=new JZHuWaiDataUploadDate();
    tagUIDUploadDataToJZHuWaiDataUploadDate(tagUIDUploadDataList,jzHuWaiDataUploadDate);
    return jzHuWaiDataUploadDate;
  }
  private static void tagUIDUploadDataToJZHuWaiDataUploadDate(List<TagUIDUploadData> instantListEntry, JZHuWaiDataUploadDate jzHuWaiDataUploadDate) {
    for (TagUIDUploadData tagUIDUploadData : instantListEntry) {
      switch (tagUIDUploadData.getTagUID()) {
        case "AIO8工况网关\\JZ0003\\固化剂罐真空值":
          jzHuWaiDataUploadDate.setHardenerJarAVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0003\\固化剂罐4.1温度实际值":
          jzHuWaiDataUploadDate.setHardenerJarATemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0003\\固化剂罐5.3温度实际值":
          jzHuWaiDataUploadDate.setHardenerJarBTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0003\\树脂罐真空值":
          jzHuWaiDataUploadDate.setResinJarAVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0003\\树脂罐2.1温度实际值":
          jzHuWaiDataUploadDate.setResinJarATemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0003\\树脂罐3.3温度实际值":
          jzHuWaiDataUploadDate.setResinJarBTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0003\\浇注箱真空值":
          jzHuWaiDataUploadDate.setPouringJarAVacuum(tagUIDUploadData.getDoubleUploadNumber());
          break;
        case "AIO8工况网关\\JZ0003\\静态混料器8.1温度实际值":
          jzHuWaiDataUploadDate.setStaticMixerTemperature(tagUIDUploadData.getDoubleUploadNumber());
          break;
      }
    }
  }

  public Pair<Boolean, HuWaiCheckedExceptionResult> inconformity(
      ErpDeviceConfig.JZHuWaiDeviceConfig jzHuWaiDeviceConfig) {
    Boolean conformity = null;
    HuWaiCheckedExceptionResult checkedExceptionResult =
        new HuWaiCheckedExceptionResult();
    checkedExceptionResult.setCredate(credate);

    ErpDeviceConfig.VacuumAndTemperatureProperties expectHardenerJarA =
        jzHuWaiDeviceConfig.getHardenerJarA();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectHardenerJarB =
            jzHuWaiDeviceConfig.getHardenerJarB();
    ErpDeviceConfig.VacuumAndTemperatureProperties expectResinJarA =
        jzHuWaiDeviceConfig.getResinJarA();

    ErpDeviceConfig.VacuumAndTemperatureProperties expectResinJarB =
            jzHuWaiDeviceConfig.getResinJarB();
    Triplet<
                Boolean,
                UploadDateWithConform<Double>,
                UploadDateWithConform<Double>>
      triplet =
            vacuumAndTemperaturePropertiesCheck(
                expectHardenerJarA, hardenerJarATemperature, hardenerJarAVacuum);
    checkedExceptionResult.setHardenerJarAVacuum(triplet.getValue2());
    checkedExceptionResult.setHardenerJarATemperature(triplet.getValue1());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    triplet =
        vacuumAndTemperaturePropertiesCheck(
            expectHardenerJarB, hardenerJarBTemperature, null);
    checkedExceptionResult.setHardenerJarBTemperature(triplet.getValue1());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    triplet =
        vacuumAndTemperaturePropertiesCheck(
            expectResinJarA, resinJarATemperature, resinJarAVacuum);
    checkedExceptionResult.setResinJarATemperature(triplet.getValue1());
    checkedExceptionResult.setResinJarAVacuum(triplet.getValue2());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    triplet =
            vacuumAndTemperaturePropertiesCheck(
                    expectResinJarB, resinJarBTemperature, null);
    checkedExceptionResult.setResinJarBTemperature(triplet.getValue1());
    conformity = getCheckedResult(conformity, triplet.getValue0());

    Pair<Boolean, UploadDateWithConform<Double>> uploadDateWithConformVaccumPouringJarAPair =
        vacuumPouringChecked(jzHuWaiDeviceConfig.getPouringJarAVacuum(), pouringJarAVacuum);
    checkedExceptionResult.setPouringJarAVacuum(
        uploadDateWithConformVaccumPouringJarAPair.getValue1());
    conformity =
        getCheckedResult(conformity, uploadDateWithConformVaccumPouringJarAPair.getValue0());

    Pair<Boolean, UploadDateWithConform<Double>> uploadDateWithConformStaticMixerTemperaturePair =
        staticMixerTemperatureChecked(
            jzHuWaiDeviceConfig.getStaticMixerTemperature(), staticMixerTemperature);
    checkedExceptionResult.setStaticMixerTemperature(
        uploadDateWithConformStaticMixerTemperaturePair.getValue1());
    conformity =
        getCheckedResult(conformity, uploadDateWithConformStaticMixerTemperaturePair.getValue0());
    return Pair.with(conformity, checkedExceptionResult);
  }

  @Setter
  @Getter
  public static class HuWaiCheckedExceptionResult {
    private Instant credate;

    @Schema(description = "固化剂灌A真空")
    private UploadDateWithConform<Double> hardenerJarAVacuum;

    @Schema(description = "固化剂灌A温度")
    private UploadDateWithConform<Double> hardenerJarATemperature;

    @Schema(description = "固化剂灌B温度")
    private UploadDateWithConform<Double> hardenerJarBTemperature;

    @Schema(description = "树脂罐A真空")
    private UploadDateWithConform<Double> resinJarAVacuum;

    @Schema(description = "树脂罐A温度")
    private UploadDateWithConform<Double> resinJarATemperature;

    @Schema(description = "树脂罐B温度")
    private UploadDateWithConform<Double> resinJarBTemperature;

    @Schema(description = "浇注罐A真空")
    private UploadDateWithConform<Double> pouringJarAVacuum;

    @Schema(description = "静态混料器温度")
    private UploadDateWithConform<Double> staticMixerTemperature;
  }
}
