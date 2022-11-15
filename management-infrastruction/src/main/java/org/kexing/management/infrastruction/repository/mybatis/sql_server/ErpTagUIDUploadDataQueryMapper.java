package org.kexing.management.infrastruction.repository.mybatis.sql_server;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kexing.management.domin.model.sql_server.TagUIDUploadData;

import java.util.List;

/**
 * @author lh
 */
@Mapper
public interface ErpTagUIDUploadDataQueryMapper extends BaseMapper {
  TagUIDUploadData selectTagUIDUploadData(@Param("tagUID") String tagUID);

  List<TagUIDUploadData> selectTagUIDUploadDataByTagUIDLike(@Param("tagUID") String tagUID);
}
