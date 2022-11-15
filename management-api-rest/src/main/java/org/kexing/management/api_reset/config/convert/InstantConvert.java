package org.kexing.management.api_reset.config.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

/** @author lh */
@Component
public class InstantConvert implements Converter<String, Instant> {
  @Override
  public Instant convert(String source) {
    if (source == null) {
      return null;
    }
    return Instant.ofEpochSecond(Long.valueOf(source));
  }
}
