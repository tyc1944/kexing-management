package org.kexing.management.infrastruction.query.dto.sql_server;

import com.yunmo.attendance.api.entity.Staff;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lh
 */
@Setter
@Getter
public class ErpStaffResponse {
  private Staff staff;
  private String depId;
}
