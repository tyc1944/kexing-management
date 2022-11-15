package org.kexing.management.api_reset.resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yunmo.domain.common.Principal;
import com.yunmo.domain.common.Tenant;
import com.yunmo.iot.api.core.DeviceService;
import com.yunmo.iot.domain.core.Device;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kexing.management.domin.model.mysql.IotErpDeviceBindMap;
import org.kexing.management.domin.repository.mysql.IotErpDeviceBindMapRepository;
import org.kexing.management.infrastruction.query.BaseParam;
import org.kexing.management.infrastruction.query.dto.sql_server.*;
import org.kexing.management.infrastruction.service.sql_server.ProductionOrderViewService;
import org.kexing.management.domin.util.StringUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/** @author lh */
@RestController
@RequestMapping("/api/productionOrders")
@RequiredArgsConstructor
@Transactional
@Tag(name = "订单资源")
public class ProductionOrderResource {
  private final ProductionOrderViewService productionOrderViewService;
  private final IotErpDeviceBindMapRepository iotErpDeviceBindMapRepository;
  private final DeviceService deviceService;

  @GetMapping
  @Operation(description = "生产订单列表")
  public IPage<ListProductionOrderResponse> listProductionOrder(
      @Principal Tenant tenant,
      @Validated(value = BaseParam.BaseParamVaGroup.class) @ModelAttribute
          ListProductionOrderRequest listProductionOrderRequest) {
    return productionOrderViewService.listProductionOrder(listProductionOrderRequest);
  }

  @GetMapping("/{productionOrderNo}")
  @Operation(description = "生产订单详情")
  public ProductionOrderResponse getProductionOrder(@PathVariable int productionOrderNo) {
    return productionOrderViewService.getProductionOrder(productionOrderNo);
  }

  @GetMapping("/{productionOrderNo}/products")
  @Operation(description = "订单产品列表")
  public IPage<ListProductionOrderProductResponse> listProductionOrderProduct(
      @PathVariable int productionOrderNo,
      @Validated(value = BaseParam.BaseParamVaGroup.class) @ModelAttribute
          ListProductionOrderProductRequest listProductionOrderProductRequest) {
    return productionOrderViewService.listProductionOrderProduct(
        productionOrderNo, listProductionOrderProductRequest);
  }

  @GetMapping("/{productionOrderNo}/products/{productId}")
  @Operation(description = "生产订单产品详情")
  public ProductionOrderProductResponse getProductionOrderProduct(
      @PathVariable int productionOrderNo, @PathVariable int productId) {
    return productionOrderViewService.getProductionOrderProduct(productionOrderNo, productId);
  }

  @GetMapping("/-/products/{productId}/crafts")
  @Operation(description = "生产订单产品工艺列表")
  public IPage<ListProductionOrderProductCraftResponse> listProductionOrderProductCraft(
      @PathVariable int productId,
      @Validated(value = BaseParam.BaseParamVaGroup.class) @ModelAttribute
          ListProductionOrderProductCraftRequest listProductionOrderProductCraftRequest) {
    return productionOrderViewService.listProductionOrderProductCraft(
        productId, listProductionOrderProductCraftRequest);
  }

  @GetMapping("/-/products/{productId}/crafts/{lineNum}/video")
  @Operation(description = "获取产品工艺的视频信息")
  public String getProductCraftVideo(
          @PathVariable int productId,
          @PathVariable int lineNum){
    ListProductionOrderProductCraftResponse craftResponse = productionOrderViewService.getProductionOrderProductCraft(productId,lineNum);
    List<IotErpDeviceBindMap> iotErpDeviceBindMapList = iotErpDeviceBindMapRepository.findByErpDeviceId(craftResponse.getDeviceId());
    if(iotErpDeviceBindMapList==null||iotErpDeviceBindMapList.size()==0){
      throw new IllegalArgumentException("此车间设备没有绑定摄像头");
    }
    Device device = deviceService.getDeviceById(iotErpDeviceBindMapList.get(0).getIotDeviceId());
    return device.getAttributes().get("video_recoder_rtsp").toString();
  }


}
