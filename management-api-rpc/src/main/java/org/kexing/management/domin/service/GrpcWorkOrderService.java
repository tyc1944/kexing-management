package org.kexing.management.domin.service;

import io.genrpc.annotation.GrpcService;
import org.kexing.management.domin.model.mysql.DeviceSource;
import org.kexing.management.domin.model.mysql.WorkOrder;

import javax.validation.Valid;
import java.util.Map;

/** @author lh */
@GrpcService
public interface GrpcWorkOrderService {

  WorkOrder createWorkOrder(
      @Valid WorkOrder.Source source, String processArea, String problemDescription);

  WorkOrder createWorkOrder1(long deviceId,
                             String processArea,
                             String problemDescription,
                             WorkOrder.Type type,
                             Map<String,Object> attributes,
                             DeviceSource deviceSource);
}
