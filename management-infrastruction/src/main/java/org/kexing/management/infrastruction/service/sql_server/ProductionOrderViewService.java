package org.kexing.management.infrastruction.service.sql_server;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.kexing.management.infrastruction.mapstruct.PageParamMapper;
import org.kexing.management.infrastruction.query.dto.sql_server.*;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.ProductionOrderQueryMapper;
import org.kexing.management.infrastruction.util.SqlServerSpecialCharacterEscapeUtil;
import org.kexing.management.infrastruction.util.SqlServerTimeUtil;
import org.springframework.stereotype.Service;

import static org.kexing.management.infrastruction.util.SqlServerSpecialCharacterEscapeUtil.ESCAPE_CHAR;

/** @author lh */
@Service
@RequiredArgsConstructor
public class ProductionOrderViewService {
  private final ProductionOrderQueryMapper productionOrderQueryMapper;
  private final PageParamMapper pageParamMapper;

  public IPage<ListProductionOrderResponse> listProductionOrder(
      ListProductionOrderRequest listProductionOrderRequest) {
    Page page = pageParamMapper.mapper(listProductionOrderRequest);
    return productionOrderQueryMapper.selectProductOrders(page, listProductionOrderRequest);
  }

  public ProductionOrderResponse getProductionOrder(int productOrderNo) {
    ProductionOrderResponse productionOrderResponse =
        productionOrderQueryMapper.selectProductionOrderByProductOrderNo(productOrderNo);
    productionOrderResponse.setDeliveryDateTime(
        SqlServerTimeUtil.columnTimeAddToDate(
            productionOrderResponse.getDeliveryDateTime(),
            productionOrderResponse.getDeliveryTime()));
    return productionOrderResponse;
  }

  public IPage<ListProductionOrderProductResponse> listProductionOrderProduct(
      int productionOrderNo, ListProductionOrderProductRequest listProductionOrderProductRequest) {
    Page page = pageParamMapper.mapper(listProductionOrderProductRequest);

    char escapeChar = ESCAPE_CHAR;
    if (StringUtils.isNoneBlank(listProductionOrderProductRequest.getSearchField())) {
      listProductionOrderProductRequest.setSearchField(
          SqlServerSpecialCharacterEscapeUtil.escape(
              listProductionOrderProductRequest.getSearchField(),escapeChar));
    }

    IPage<ListProductionOrderProductResponse> iPage =
        productionOrderQueryMapper.selectProductionOrderProducts(
            page, productionOrderNo, listProductionOrderProductRequest, escapeChar);
    for (ListProductionOrderProductResponse listProductionOrderProductResponse :
        iPage.getRecords()) {
      columnTimeAddToDate(listProductionOrderProductResponse);
    }
    return iPage;
  }

  private void columnTimeAddToDate(ListProductionOrderProductResponse responseItem) {
    responseItem.setStartProductionDateTime(
        SqlServerTimeUtil.columnTimeAddToDate(
            responseItem.getStartProductionDateTime(), responseItem.getStartProductionTime()));
  }

  public ProductionOrderProductResponse getProductionOrderProduct(
      int productionOrderNo, int productId) {
    ProductionOrderProductResponse productionOrderProductResponse =
        productionOrderQueryMapper.getProductionOrderProduct(productionOrderNo, productId);
    if (ObjectUtils.isNotEmpty(productionOrderProductResponse)) {
      columnTimeAddToDate(productionOrderProductResponse);
    }
    productionOrderProductResponse.setEndProductionDateTime(
        SqlServerTimeUtil.columnTimeAddToDate(
            productionOrderProductResponse.getEndProductionDateTime(),
            productionOrderProductResponse.getEndProductionTime()));
    productionOrderProductResponse.setProductionOrder(getProductionOrder(productionOrderNo));
    return productionOrderProductResponse;
  }

  public IPage<ListProductionOrderProductCraftResponse> listProductionOrderProductCraft(
      int productId,
      ListProductionOrderProductCraftRequest listProductionOrderProductCraftRequest) {
    Page page = pageParamMapper.mapper(listProductionOrderProductCraftRequest);

    IPage<ListProductionOrderProductCraftResponse> listProductionOrderProductCraftResponseIPage =
        productionOrderQueryMapper.selectProductionOrderProductCrafts(
            page, productId, listProductionOrderProductCraftRequest);

    listProductionOrderProductCraftResponseIPage
        .getRecords()
        .forEach(
            listProductionOrderProductCraftResponse -> {
              listProductionOrderProductCraftResponse.setEndProductionDateTime(
                  SqlServerTimeUtil.columnTimeAddToDate(
                      listProductionOrderProductCraftResponse.getEndProductionDateTime(),
                      listProductionOrderProductCraftResponse.getEndProductionTime()));
              listProductionOrderProductCraftResponse.setStartProductionDateTime(
                      SqlServerTimeUtil.columnTimeAddToDate(
                              listProductionOrderProductCraftResponse.getStartProductionDateTime(),
                              listProductionOrderProductCraftResponse.getStartProductionTime()));
            });
    return listProductionOrderProductCraftResponseIPage;
  }

  public ListProductionOrderProductCraftResponse getProductionOrderProductCraft(int productId,int lineNum){
    return productionOrderQueryMapper.getProductionOrderProductCrafts(productId,lineNum);
  }

}
