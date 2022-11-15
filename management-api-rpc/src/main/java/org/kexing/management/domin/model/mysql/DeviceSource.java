package org.kexing.management.domin.model.mysql;

import io.genrpc.annotation.ProtoEnum;
import io.swagger.v3.oas.annotations.media.Schema;

/** @author lh */
@Schema(description = "来源类型:ALARM->")
@ProtoEnum
public enum DeviceSource {
  ERP,
  IOT,
  ;
}
