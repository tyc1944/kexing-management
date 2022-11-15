package org.kexing.management.domin.repository.mysql;

import com.yunmo.domain.common.EntityRepository;
import org.kexing.management.domin.model.mysql.WorkOrder;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** @author lh */
public interface WorkOrderRepository extends EntityRepository<WorkOrder, Long> {
  Optional<WorkOrder> findBySource(WorkOrder.Source source);

  WorkOrder findTop1ByDeviceIdOrderByCreatedDateDesc(long deviceId);

  long countByProcessorUserAccountIdAndStatusIn(long processorUserAccountId,List<WorkOrder.Status> statusList);

  @Query(value = "select * from work_order where date(created_date)=?1 and json_extract(attributes,'$.emp_id')=?2 and json_extract(attributes,'$.event_type')=?3",nativeQuery = true)
  List<WorkOrder> findByAlertTimeDay(LocalDate alertTime,String empId,String eventType);

  @Query(value = "select * from work_order where device_id=?1 and json_extract(attributes,'$.event_type')=?2 order by created_date desc limit 1",nativeQuery = true)
  List<WorkOrder> findLastByDeviceIdAndEventType(long deviceId,String eventType);

  @Query(value = "select * from work_order where device_id=?1 and json_extract(attributes,'$.event_type')=?2 and json_extract(attributes,'$.emp_id')=?3  order by created_date desc limit 1",nativeQuery = true)
  List<WorkOrder> findLastByDeviceIdAndEventTypeAndEmpId(long deviceId,String eventType,String empId);

}
