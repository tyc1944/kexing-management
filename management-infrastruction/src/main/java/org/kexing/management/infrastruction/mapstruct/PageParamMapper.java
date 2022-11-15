package org.kexing.management.infrastruction.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.kexing.management.infrastruction.query.BaseParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PageParamMapper {
  @Mapping(target = "total", ignore = true)
  @Mapping(target = "searchCount", ignore = true)
  @Mapping(target = "records", ignore = true)
  @Mapping(target = "pages", ignore = true)
  @Mapping(target = "orders", ignore = true)
  @Mapping(target = "optimizeCountSql", ignore = true)
  @Mapping(target = "descs", ignore = true)
  @Mapping(target = "desc", ignore = true)
  @Mapping(target = "ascs", ignore = true)
  @Mapping(target = "asc", ignore = true)
  Page mapper(BaseParam param);
}
