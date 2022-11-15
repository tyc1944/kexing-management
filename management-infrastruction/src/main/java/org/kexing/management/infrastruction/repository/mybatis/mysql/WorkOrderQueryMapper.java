package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.kexing.management.infrastruction.query.dto.ListWorkOrderRequest;
import org.kexing.management.infrastruction.query.dto.ListWorkOrderResponse;

/** @author lh */
public interface WorkOrderQueryMapper extends BaseMapper<WorkOrder> {
  IPage<ListWorkOrderResponse> selectWorkOrders(
      @Param("page") Page page,
      @Param("listWorkOrderRequest") ListWorkOrderRequest listWorkOrderRequest);

}
