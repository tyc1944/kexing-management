package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.domin.model.mysql.HongXiangExceptionReportedRecord;
import org.kexing.management.infrastruction.query.dto.ListExceptionReportedRecordRequest;

public interface HongXiangExceptionReportedRecordQueryMapper
    extends BaseMapper<HongXiangExceptionReportedRecord> {

  IPage<HongXiangExceptionReportedRecord> selectListHongXiangExceptionReportedRecordRequestPage(
          @Param("page") Page page,
          @Param("deviceId") String deviceId,
          @Param("listHongXiangExceptionReportedRecordRequest") ListExceptionReportedRecordRequest listHongXiangExceptionReportedRecordRequest);
}
