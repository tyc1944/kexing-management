package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.domin.model.mysql.jz.JzHuWaiExceptionReportedRecord;
import org.kexing.management.domin.model.mysql.jz.JzWeiKeOldExceptionReportedRecord;
import org.kexing.management.infrastruction.query.dto.ListExceptionReportedRecordRequest;

public interface JZWeiKeOldExceptionReportedRecordQueryMapper
    extends BaseMapper<JzWeiKeOldExceptionReportedRecord> {

  IPage<JzWeiKeOldExceptionReportedRecord> selectListExceptionReportedRecordRequestPage(
          @Param("page") Page page,
          @Param("deviceId") String deviceId,
          @Param("listExceptionReportedRecordRequest") ListExceptionReportedRecordRequest listExceptionReportedRecordRequest);
}
