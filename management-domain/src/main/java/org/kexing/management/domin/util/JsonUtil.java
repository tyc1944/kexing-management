package org.kexing.management.domin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/** @author lh */
public class JsonUtil {
  public static final ObjectMapper objectMapper = new ObjectMapper(); // // TODO_LH: 2022/3/29 替换优化

  static {
    objectMapper.registerModule(new JavaTimeModule());
  }
}
