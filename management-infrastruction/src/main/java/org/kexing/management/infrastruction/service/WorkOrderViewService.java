package org.kexing.management.infrastruction.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.infrastruction.mapstruct.PageParamMapper;
import org.kexing.management.infrastruction.query.dto.ListCcWorkOrderRequest;
import org.kexing.management.infrastruction.query.dto.ListWorkOrderRequest;
import org.kexing.management.infrastruction.query.dto.ListWorkOrderResponse;
import org.kexing.management.infrastruction.repository.mybatis.mysql.WorkOrderQueryMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/** @author lh */
@Service
@RequiredArgsConstructor
public class WorkOrderViewService {
  private final PageParamMapper pageParamMapper;
  private final WorkOrderQueryMapper workOrderQueryMapper;

  public IPage<ListWorkOrderResponse> listWorkOrder(ListWorkOrderRequest listWorkOrderRequest) {
    Page page = pageParamMapper.mapper(listWorkOrderRequest);
    return workOrderQueryMapper.selectWorkOrders(page, listWorkOrderRequest);
  }

}
