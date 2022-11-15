package org.kexing.management.domin.model.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yunmo.domain.common.Audited;
import com.yunmo.domain.common.Problems;
import com.yunmo.generator.annotation.AutoValueDTO;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.kexing.management.domin.util.JsonUtil.objectMapper;

/** @author lh */
@Entity
@Setter
@Getter
@Builder
@AutoValueDTO
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "erp设备配置")
@org.hibernate.annotations.Table(appliesTo = "erp_device_config", comment = "erp设备配置")
@Table(
        indexes = {
                @Index(columnList = "deviceId", unique = true)
        })
public class ErpDeviceConfig extends Audited {
  @Id
  @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
  @GeneratedValue(generator = "sequence_id")
  private Long id;

  private String deviceId;

  @Schema(description = "erp device config,json 字符串")
  @Column(columnDefinition="text")
  private String config;

  @Setter
  @Getter
  public static class HongXiangDeviceConfig {
    @Valid
    @NotNull
    private Temperature temperature;
  }

  @Schema(description = "温度")
  @Setter
  @Getter
  public static class Temperature {
    @NotNull
    private Integer min;
    @NotNull
    private Integer max;

    public Boolean contain(Double temperature) {
      return min <= temperature && temperature <= max;
    }

    @JsonIgnore
    public boolean isNotEmpty(){
      return min !=null && max !=null;
    }
  }

  @Schema(description = "真空")
  @Setter
  @Getter
  public static class Vacuum {
    @NotNull
    private Integer min;
    @NotNull
    private Integer max;

    public boolean contain(double vacuum) {
      return min <= vacuum && vacuum <= max;
    }

    @JsonIgnore
    public boolean isNotEmpty(){
      return min !=null && max !=null;
    }

  }

  @Schema(description = "重量")
  @Setter
  @Getter
  public static class Weight {
    @NotNull
    private Double max;

    public boolean contain(Double weight) {
      return weight <= max;
    }
  }

  @Schema(description = "重量真空温度属性")
  @Setter
  @Getter
  public static class VacuumAndTemperatureProperties {
    @Valid
    private Temperature temperature;
    @Valid
    private Vacuum vacuum;

  }

  public HongXiangDeviceConfig hongXiangDeviceConfig() throws JsonProcessingException {
    return objectMapper.readValue(config, HongXiangDeviceConfig.class);
  }

  @Setter
  @Getter
  @Schema(description = "维克旧浇筑机配置")
  public static class JZWeiKeOldDeviceConfig extends JZConfig{
    @Schema(required = true)
    private Type type;

    @Override
    public boolean hasEmptyConfig() {
      return ObjectUtils.allNull(
          filterJar,
          hardenerJarA,
          hardenerJarB,
          resinJarA,
          resinJarB,
          pouringJarAVacuum,
          pouringJarBVacuum,
          staticMixerTemperature);
    }

    @Override
    public void check() {
      
    }

    public  enum Type{
      JZ_WEIKE_OLD
    }

    @Schema(description = "填料罐配置")
    @Valid
    private FilterJarConfig filterJar;

    @Schema(description = "固化剂罐A配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties hardenerJarA;

    @Schema(description = "固化剂罐B配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties hardenerJarB;

    @Schema(description = "树脂罐A配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties resinJarA;

    @Schema(description = "树脂罐B配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties resinJarB;

    @Valid
    @Schema(description = "浇注罐A真空配置")
    private Vacuum pouringJarAVacuum;

    @Valid
    @Schema(description = "浇注罐B真空配置")
    private Vacuum pouringJarBVacuum;

    @Valid
    @Schema(description = "浇注罐左模具温度配置")
    private Temperature pouringJarLeftMoldsTemperature;

    @Valid
    @Schema(description = "浇注罐右模具温度配置")
    private Temperature pouringJarRightMoldsTemperature;

    @Valid
    @Schema(description = "静态混料器温度配置")
    private Temperature staticMixerTemperature;

    @Schema(description = "填料罐配置")
    @Setter
    @Getter
    public static class FilterJarConfig extends VacuumAndTemperatureProperties {
      @Valid
      @Schema(description = "壁温度配置")
      private Temperature wallTemperature;
    }

  }


  @Setter
  @Getter
  @Schema(description = "维克新浇筑机配置")
  public static class JZWeiKeNewDeviceConfig extends JZConfig{
    @Schema(required = true)
    private Type type;

    @Override
    public boolean hasEmptyConfig() {
      return ObjectUtils.allNull(
              hardenerJar,
              resinJarVacuum,
              resinJarA,
              resinJarB,
              filterJar,
              pouringJarA,
              pouringJarB,
              staticMixerATemperature,
              staticMixerBTemperature);
    }

    @Override
    public void check() {
      Problems.ensure(resinJarA.getTemperature()!=null&&resinJarA.getTemperature().isNotEmpty(),"树脂罐A,温度配置不能为空");
      Problems.ensure(resinJarA.getVacuum()==null,"树脂罐A,真空不支持配置");

      Problems.ensure(resinJarB.getTemperature()!=null&&resinJarB.getTemperature().isNotEmpty(),"树脂罐B,温度配置不能为空");
      Problems.ensure(resinJarB.getVacuum()==null,"树脂罐B,真空不支持配置");

      Problems.ensure(pouringJarA.getTemperature()==null,"浇注罐A,温度不支持配置");
      Problems.ensure(pouringJarA.getVacuum()!=null && pouringJarA.getVacuum().isNotEmpty(),"浇注灌A,真空配置不能为空");

      Problems.ensure(pouringJarB.getTemperature()==null,"浇注灌B,温度不支持配置");
      Problems.ensure(pouringJarB.getVacuum()!=null && pouringJarB.getVacuum().isNotEmpty(),"浇注灌B,真空配置不能为空");
    }

    public  enum Type{
      JZ_WEIKE_NEW
    }

    @Schema(description = "固化剂罐配置")
    private ErpDeviceConfig.VacuumAndTemperatureProperties hardenerJar;


    @Schema(description = "树脂罐A配置")
//    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties resinJarA;

    @Schema(description = "树脂罐B配置")
//    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties resinJarB;

    @Schema(description = "树脂罐真空配置")
    @Valid
    private ErpDeviceConfig.Vacuum resinJarVacuum;

    @Schema(description = "填料罐配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties filterJar;

//    @Valid
    @Schema(description = "浇注罐A配置")
    private ErpDeviceConfig.VacuumAndTemperatureProperties pouringJarA;

//    @Valid
    @Schema(description = "浇注罐B配置")
    private ErpDeviceConfig.VacuumAndTemperatureProperties pouringJarB;

    @Valid
    @Schema(description = "静态混料器A温度配置")
    private Temperature staticMixerATemperature;

    @Valid
    @Schema(description = "静态混料器B温度配置")
    private Temperature staticMixerBTemperature;

  }

  @Setter
  @Getter
  @Schema(description = "户外浇筑机配置")
  public static class JZHuWaiDeviceConfig extends JZConfig {

    @Schema(required = true)
    private Type type;

    @Override
    public boolean hasEmptyConfig() {
      return ObjectUtils.allNull(
           hardenerJarA, resinJarA, pouringJarAVacuum, staticMixerTemperature);
    }

    @Override
    public void check() {
      Problems.ensure(hardenerJarB.getTemperature()!=null&& hardenerJarB.getTemperature().isNotEmpty(),"固化剂罐B,温度配置不能为空");
      Problems.ensure(hardenerJarB.getVacuum()==null,"固化剂罐B,不支持真空配置");

      Problems.ensure(resinJarB.getVacuum()==null,"树脂罐B,不支持真空配置");
    }

    public  enum Type{
      JZ_HUWAI
    }

    @Schema(description = "固化剂罐A配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties hardenerJarA;

    @Schema(description = "固化剂罐B配置")
//    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties hardenerJarB;

    @Schema(description = "树脂罐A配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties resinJarA;

    @Schema(description = "树脂罐B配置")
//    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties resinJarB;

    @Valid
    @Schema(description = "浇注罐A真空配置")
    private Vacuum pouringJarAVacuum;

    @Valid
    @Schema(description = "静态混料器温度配置")
    private Temperature staticMixerTemperature;
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true,property = "type",include = JsonTypeInfo.As.EXISTING_PROPERTY)
  @JsonSubTypes({
          @JsonSubTypes.Type(
                  value = JZHuNeiDeviceConfig.class,
                  name = "JZ_HUNEI"),
          @JsonSubTypes.Type(
                  value = JZHuWaiDeviceConfig.class,
                  name = "JZ_HUWAI"),
          @JsonSubTypes.Type(
                  value = JZWeiKeOldDeviceConfig.class,
                  name = "JZ_WEIKE_OLD"),
          @JsonSubTypes.Type(
                  value = JZWeiKeNewDeviceConfig.class,
                  name = "JZ_WEIKE_NEW"),
  })
  @Schema(
          discriminatorProperty = "type",
          discriminatorMapping = {
                  @DiscriminatorMapping(
                          schema = JZHuNeiDeviceConfig.class,
                          value = "JZ_HUNEI"),
                  @DiscriminatorMapping(
                          schema = JZHuWaiDeviceConfig.class,
                          value = "JZ_HUWAI"),
                  @DiscriminatorMapping(
                          schema = JZWeiKeOldDeviceConfig.class,
                          value = "JZ_WEIKE_OLD"),
                  @DiscriminatorMapping(
                          schema = JZWeiKeNewDeviceConfig.class,
                          value = "JZ_WEIKE_NEW"),
          })
  @Getter
  @Setter
  public static abstract class JZConfig{
    public abstract boolean hasEmptyConfig();

    public abstract void check();
  }

  @Setter
  @Getter
  @Schema(description = "户内浇筑机配置")
  public static class JZHuNeiDeviceConfig extends JZConfig{
    @Schema(required = true)
    private Type type;

    @Override
    public boolean hasEmptyConfig() {
      return ObjectUtils.allNull(
          filterJar,
          hardenerJarA,
          hardenerJarB,
          resinJarA,
          resinJarB,
          pouringJarAVacuum,
          pouringJarBVacuum,
          staticMixerTemperature);
    }

    @Override
    public void check() {

    }

    public  enum Type{
      JZ_HUNEI
    }


    @Schema(description = "填料罐配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties filterJar;

    @Schema(description = "固化剂罐A配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties hardenerJarA;

    @Schema(description = "固化剂罐B配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties hardenerJarB;

    @Schema(description = "树脂罐A配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties resinJarA;

    @Schema(description = "树脂罐B配置")
    @Valid
    private ErpDeviceConfig.VacuumAndTemperatureProperties resinJarB;

    @Valid
    @Schema(description = "浇注罐A真空配置")
    private Vacuum pouringJarAVacuum;

    @Valid
    @Schema(description = "浇注罐B真空配置")
    private Vacuum pouringJarBVacuum;

    @Valid
    @Schema(description = "静态混料器温度配置")
    private Temperature staticMixerTemperature;
  }
}

