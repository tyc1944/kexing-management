package org.kexing.management;

import org.apache.catalina.connector.Connector;
import org.kexing.management.api_reset.config.AttendanceApiConfig;
import org.kexing.management.api_reset.config.HaikangDeviceApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zalando.problem.spring.web.autoconfigure.security.ProblemSecurityAutoConfiguration;

@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@SpringBootApplication(exclude = ProblemSecurityAutoConfiguration.class)
@Import({HaikangDeviceApiConfig.class, AttendanceApiConfig.class})
public class KeXingManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(KeXingManagementApplication.class, args);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * fix:The valid characters are defined in RFC 7230 and RFC 3986
   * @return
   */
  @Bean
  public ConfigurableServletWebServerFactory webServerFactory() {
    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
    factory.addConnectorCustomizers(
        new TomcatConnectorCustomizer() {
          @Override
          public void customize(Connector connector) {
            connector.setProperty("relaxedQueryChars", "|{}[]");
          }
        });
    return factory;
  }

  public static final long projectId = 291724349243654146l;
  public static final long cameraProductId = 319468817648517121l;
  public static final long temperatureAndHumiditySensorProductId = 329468817648517121l;
  public static final long temperatureSensorProductId = 339468817648517121l;
  public static final long gatewayProductId = 359468817648517121l;
  public static final long videoRecorderProductId = 349468817648517121l;
}
