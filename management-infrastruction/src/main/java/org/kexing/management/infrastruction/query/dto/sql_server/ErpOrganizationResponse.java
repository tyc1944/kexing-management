package org.kexing.management.infrastruction.query.dto.sql_server;

import com.yunmo.attendance.api.entity.Organization;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lh
 */
@Setter
@Getter
public class ErpOrganizationResponse {
  private Organization organization;
  private String parentID;
}
