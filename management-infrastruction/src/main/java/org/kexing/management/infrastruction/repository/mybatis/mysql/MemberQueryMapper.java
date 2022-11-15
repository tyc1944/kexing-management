package org.kexing.management.infrastruction.repository.mybatis.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.yunmo.attendance.api.entity.Member;

import java.util.List;

/**
 * @author lh
 */
public interface MemberQueryMapper extends BaseMapper<Member> {
}
