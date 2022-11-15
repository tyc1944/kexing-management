package org.kexing.management.infrastruction.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.kexing.management.infrastruction.mapstruct.PageParamMapper;
import org.kexing.management.infrastruction.query.dto.FaceDoorRecordResponse;
import org.kexing.management.infrastruction.query.dto.ListFaceDoorRecordRequest;
import org.kexing.management.infrastruction.repository.mybatis.mysql.FaceDoorRecordQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaceDoorRecordService {

  private final FaceDoorRecordQueryMapper faceDoorRecordQueryMapper;
  private final PageParamMapper pageParamMapper;

  public IPage<FaceDoorRecordResponse> listRecord(ListFaceDoorRecordRequest recordRequest) {
    Page page = pageParamMapper.mapper(recordRequest);
    return faceDoorRecordQueryMapper.selectListFaceDoorRecord(page, recordRequest);
  }
}
