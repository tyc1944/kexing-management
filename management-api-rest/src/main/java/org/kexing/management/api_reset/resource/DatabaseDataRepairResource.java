package org.kexing.management.api_reset.resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunmo.attendance.api.entity.Member;
import com.yunmo.haikang.device.api.entity.FaceDoorRecord;
import org.kexing.management.domin.repository.mysql.MemberRepository;
import org.kexing.management.infrastruction.repository.jpa.mysql.FaceDoorRecordJpaRepository;
import org.kexing.management.infrastruction.repository.mybatis.mysql.MemberQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lh
 */
@RestController
@Transactional
@RequestMapping("/api/database")
public class DatabaseDataRepairResource {
  @Autowired FaceDoorRecordJpaRepository faceDoorRecordRepository;

  @Autowired MemberQueryMapper memberQueryMapper;

  @PostMapping("/face_door_record/restoration")
  public boolean repairFaceDoorRecord() {
    int page = 0;
    List<FaceDoorRecord> faceDoorRecordList = new ArrayList<>();
    while (true) {
      int size = 2000;
      PageRequest pageRequest =
          PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdDate"));
      List<FaceDoorRecord> itemList =
          faceDoorRecordRepository.findByOrganizationId(0L, pageRequest);
      page++;
      faceDoorRecordList.addAll(itemList);
      if (itemList.size() < size) {
        break;
      }
    }
    for (FaceDoorRecord faceDoorRecord : faceDoorRecordList) {
      LambdaQueryWrapper<Member> lambdaQueryWrapper = new LambdaQueryWrapper<>();
      lambdaQueryWrapper.eq(Member::getStaffId, faceDoorRecord.getStaffId());
      lambdaQueryWrapper.orderByAsc(Member::getCreatedDate);
      lambdaQueryWrapper.last("limit 1");
      Member member = memberQueryMapper.selectOne(lambdaQueryWrapper);
      if (member == null) {
        throw new RuntimeException("组织信息不存在");
      }

      faceDoorRecordRepository.save(faceDoorRecord.setOrganizationId(member.getOrganizationId()));
    }
    return true;
  }
}
