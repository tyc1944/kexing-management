package org.kexing.management.api_reset.resource;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yunmo.haikang.device.api.FaceDoorRecordRpcService;
import com.yunmo.haikang.device.api.FaceRpcService;
import com.yunmo.haikang.device.api.entity.FaceDoorRecord;
import com.yunmo.iot.api.core.AssetEntityService;
import com.yunmo.iot.api.core.DeviceOtaRecordService;
import com.yunmo.iot.api.core.DeviceService;
import com.yunmo.iot.api.core.TelemetryRecordService;
import com.yunmo.iot.domain.assets.AssetEntity;
import com.yunmo.iot.domain.core.Device;
import com.yunmo.iot.domain.ota.DeviceOTARecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.kexing.management.KeXingManagementApplication;
import org.kexing.management.domin.model.mysql.DevicePictureMap;
import org.kexing.management.domin.model.mysql.IotErpDeviceBindMap;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.domin.repository.mysql.DevicePictureMapRepository;
import org.kexing.management.domin.repository.mysql.IotErpDeviceBindMapRepository;
import org.kexing.management.infrastruction.mapstruct.DeviceWrapMapping;
import org.kexing.management.infrastruction.query.BaseParam;
import org.kexing.management.infrastruction.query.dto.*;
import org.kexing.management.infrastruction.query.dto.sql_server.GetErpDeviceResponse;
import org.kexing.management.infrastruction.query.dto.statistics.IotDeviceStatusStatisticsResponse;
import org.kexing.management.infrastruction.service.DeviceViewService;
import org.kexing.management.infrastruction.service.FaceDoorRecordService;
import org.kexing.management.domin.util.DateUtil;
import org.kexing.management.domin.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/** @author lh */
@RequestMapping("/api/devices")
@RestController
@RequiredArgsConstructor
@Tag(name = "????????????")
public class DeviceResource {
  private final DevicePictureMapRepository devicePictureMapRepository;

  @Autowired private DeviceViewService deviceViewService;
  @Autowired private DeviceService deviceService;
  @Autowired private FaceDoorRecordService faceDoorRecordService;
  @Autowired FaceDoorRecordRpcService faceDoorRecordRpcService;
  @Autowired private AssetEntityService assetEntityService;
  @Autowired private DeviceWrapMapping deviceWrapMapping;
  @Autowired private IotErpDeviceBindMapRepository iotErpDeviceBindMapRepository;
  @Autowired private FaceRpcService faceRpcService;
  @Autowired private DeviceOtaRecordService deviceOtaRecordService;
  @Autowired private TelemetryRecordService telemetryRecordService;

  @GetMapping("/erpdevice/{macId}")
  @Operation(description = "??????????????????ID ???????????????????????????")
  public Device getByMacId(@PathVariable String macId){
    List<IotErpDeviceBindMap> erpDeviceBindMaps = iotErpDeviceBindMapRepository.findByErpDeviceId(macId);
    if(erpDeviceBindMaps==null||erpDeviceBindMaps.size()==0){
      throw new IllegalArgumentException("??????????????????????????????????????????");
    }
    return deviceService.getDeviceById(erpDeviceBindMaps.get(0).getIotDeviceId());
  }

  @GetMapping("/map")
  @Operation(description = "??????????????????")
  public DevicePictureMap getDevicePictureMap(@RequestParam("type") DevicePictureMap.Type type) {
    return devicePictureMapRepository.findByType(type).orElse(null);
  }

  @GetMapping("/map/door_face/{id}")
  @Operation(description = "????????????????????????????????????????????????????????????????????????????????????")
  public List<Device> getFaceDoorInMap(@PathVariable long id){
    Device device = deviceService.getDeviceById(id);
    return deviceService.findByAttributesProperty("groupIndex",device.getAttributes().get("groupIndex").toString());
  }

  @GetMapping
  @Operation(description = "????????????(??????????????????????????????????????????????????????????????????)")
  public IPage<IotDeviceWrap> listDevices(
      @Validated(value = BaseParam.BaseParamVaGroup.class) @ModelAttribute
          ListDeviceRequest listDeviceRequest) {
    listDeviceRequest.setProjectId(KeXingManagementApplication.projectId);
    IPage<IotDeviceWrap> devices = deviceViewService.listDevice(listDeviceRequest);
    //???????????????????????????????????????ERP???????????????????????????
    if(!WorkOrder.SourceType.camera.name().equals(listDeviceRequest.getDeviceType())){
      devices.getRecords().forEach(d -> {
        convertDevice(d.getId(),d);
      });
    }
    return devices;
  }


  @Operation(description = "??????????????????")
  @GetMapping("/locations")
  public List<ErpDeviceResponse> listDeviceLocation() {
    return deviceViewService.listDeviceLocation();
  }

  @Operation(description = "????????????(??????????????????????????????????????????????????????????????????)")
  @GetMapping("/{id}")
  public IotDeviceWrap getDevice(@PathVariable long id) {
    Device device = deviceService.getDeviceById(id);
    IotDeviceWrap deviceWrap = deviceWrapMapping.map(device);
    convertDevice(id,deviceWrap);
    return deviceWrap;
  }

  private void convertDevice(long deviceId,IotDeviceWrap deviceWrap){
    Device device = deviceService.getDeviceById(deviceId);
    List<Device> children = new ArrayList<>();
    if(device.getAttributes()!=null){
      String deviceType = device.getAttributes().get("deviceType").toString();
      //????????????????????????????????????
      if(WorkOrder.SourceType.camera.name().equals(deviceType)){
        List<GetErpDeviceResponse> erpDeviceResponses = new ArrayList<>();
        List<IotErpDeviceBindMap> erpDeviceBindMaps = iotErpDeviceBindMapRepository.findByIotDeviceId(deviceId);
        if(erpDeviceBindMaps!=null){
          for (IotErpDeviceBindMap erpDeviceBindMap : erpDeviceBindMaps) {
            GetErpDeviceResponse erpDeviceResponse = deviceViewService.getErpDevice(erpDeviceBindMap.getErpDeviceId());
            erpDeviceResponses.add(erpDeviceResponse);
          }
          deviceWrap.setErpDevices(erpDeviceResponses);
        }
        return;
      }
      //????????????
      if(WorkOrder.SourceType.gateway.name().equals(deviceType)){
        DeviceOTARecord deviceOTARecord = deviceOtaRecordService.getLastByDeviceId(deviceId);
        deviceWrap.setLastDeviceOTARecord(deviceOTARecord);
        AssetEntity assetEntity = assetEntityService.findByDeviceId(deviceId);
        if(assetEntity!=null){
          List<AssetEntity> assetEntities=assetEntityService.findByParentId(assetEntity.getId());
          if(assetEntities!=null&&assetEntities.size()>0){
            assetEntities.forEach(a->{
              children.add(deviceService.getDeviceById(a.getDeviceId()));
            });
            deviceWrap.setChildren(children);
          }
        }
      }
    }

  }


  @GetMapping("/faceDoor/record/{id}")
  @Operation(description = "????????????????????????")
  public FaceDoorRecord getRecordDetail(
      @Validated(value = BaseParam.BaseParamVaGroup.class) @PathVariable long id) {
    return faceDoorRecordRpcService.getById(id);
  }

  @GetMapping("/faceDoor/record")
  @Operation(description = "??????????????????")
  public IPage<FaceDoorRecordResponse> listFaceDoorRecord(
      @Validated(value = BaseParam.BaseParamVaGroup.class) @ModelAttribute
          ListFaceDoorRecordRequest listFaceDoorRecordRequest) {
    return pageFaceDoorRecord(listFaceDoorRecordRequest);
  }

  @SneakyThrows
  @GetMapping("/faceDoor/record/export")
  @Operation(description = "????????????????????????")
  public void listFaceDoorRecordExport(
      HttpServletResponse response,
      @ModelAttribute ListFaceDoorRecordRequest listFaceDoorRecordRequest) {
    listFaceDoorRecordRequest.setSize(-1l);
    IPage<FaceDoorRecordResponse> pages = pageFaceDoorRecord(listFaceDoorRecordRequest);
    String fileName =
        StringUtil.getDownloadFileName(
            listFaceDoorRecordRequest.getAbnormalTemperature()!=null&&listFaceDoorRecordRequest.getAbnormalTemperature() ? "??????????????????" : "??????????????????");
    response.setContentType("application/vnd.ms-excel");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    EasyExcel.write(response.getOutputStream(), FaceDoorRecordResponse.class)
        .sheet("sheet")
        .doWrite(pages.getRecords());
  }

  private IPage<FaceDoorRecordResponse> pageFaceDoorRecord(
      ListFaceDoorRecordRequest listFaceDoorRecordRequest) {
    IPage<FaceDoorRecordResponse> list =
        faceDoorRecordService.listRecord(listFaceDoorRecordRequest);
    list.getRecords()
        .forEach(
            r -> {
              r.setDate(
                  DateUtil.convert2String(r.getCreatedDate(), DateUtil.YYY_MM_DD)
                      .concat(" ")
                      .concat(DateUtil.getWeek(r.getCreatedDate())));
              r.setTime(DateUtil.convert2String(r.getCreatedDate(), DateUtil.HH_MM));
            });
    return list;
  }

  @GetMapping("/status/statistics")
  @Operation(description = "iot ???????????????????????????")
  public IotDeviceStatusStatisticsResponse.IotDiffDeviceTypeStatusStatisticsResponse getIotDeviceStatusStatistics() {
    return deviceViewService.getIotDeviceStatusStatistics();
  }

}
