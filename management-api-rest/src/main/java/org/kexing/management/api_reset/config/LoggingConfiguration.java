package org.kexing.management.api_reset.config;

import org.kexing.management.api_reset.filter.RequestAndResponseLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// TODO_LH: 2022/2/10 待删除
//@Configuration
public class LoggingConfiguration {
   @Bean
   public RequestAndResponseLoggingFilter requestResponseLoggingFilter() {
       return new RequestAndResponseLoggingFilter();
   }

} 