package org.kexing.management.infrastruction.repository.mybatis.sql_server;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.infrastruction.query.dto.sql_server.*;

/** @author lh */
public interface ProductionOrderQueryMapper extends BaseMapper {
  IPage<ListProductionOrderResponse> selectProductOrders(
      @Param("page") Page page,
      @Param("listProductOrderRequest") ListProductionOrderRequest listProductOrderRequest);

  ProductionOrderResponse selectProductionOrderByProductOrderNo(
      @Param("productionOrderNo") int productionOrderNo);

  IPage<ListProductionOrderProductResponse> selectProductionOrderProducts(
          @Param("page") Page page,
          @Param("productionOrderNo") int productionOrderNo,
          @Param("listProductionOrderProductRequest")
          ListProductionOrderProductRequest listProductionOrderProductRequest, @Param("escapeChar") char escapeChar);

  ProductionOrderProductResponse getProductionOrderProduct(
      @Param("productionOrderNo") int productionOrderNo, @Param("productId") int productId);

  ListProductionOrderProductCraftResponse getProductionOrderProductCrafts(@Param("productId")int productId,@Param("lineNum")int lineNum);

  IPage<ListProductionOrderProductCraftResponse> selectProductionOrderProductCrafts(
      @Param("page") Page page,
      @Param("productId") int productId,
      @Param("listProductionOrderProductCraftRequest")
          ListProductionOrderProductCraftRequest listProductionOrderProductCraftRequest);
}
