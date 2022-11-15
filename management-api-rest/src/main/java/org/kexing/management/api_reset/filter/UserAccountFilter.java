package org.kexing.management.api_reset.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunmo.domain.common.Tenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kexing.management.domin.exception.LoginPasswordChangeException;
import org.kexing.management.domin.exception.PermissionConfChangeException;
import org.kexing.management.domin.exception.UserAccountDeletedException;
import org.kexing.management.domin.model.mysql.UserAccount;
import org.kexing.management.domin.service.UserAccountService;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.persistence.EntityNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Base64;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserAccountFilter extends OncePerRequestFilter {

  private final UserAccountService userAccountService;
  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getRequestURI().toLowerCase().startsWith("/api")) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Tenant tenant = (Tenant) authentication.getPrincipal();
      UserAccount userAccount = null;
      try {
        userAccount = userAccountService.getUserAccount(tenant.getId());
      } catch (EntityNotFoundException e) {
        throw new UserAccountDeletedException(e.getMessage());
      }

      if (!userAccount.isEnabled()) {
        throw new DisabledException("账户已禁用");
      }

      String token = ((HttpServletRequest) request).getHeader("Authorization");
      Object iat =objectMapper.readTree(decodeToken(token)).get("iat");
      Instant iatInstant = null;
      if (iat != null) {
        iatInstant = Instant.ofEpochSecond(Long.parseLong(iat.toString()));
      }else {
         Instant expInstant =Instant.ofEpochSecond(Long.parseLong(objectMapper.readTree(decodeToken(token)).get("exp").toString()));
         iatInstant =  expInstant.minus(30, ChronoUnit.DAYS);
      }

      if (userAccount.getChangePasswordTime() != null && iatInstant.isBefore(userAccount.getChangePasswordTime())){
        throw new LoginPasswordChangeException("登录账号密码变更");
      }

      if (userAccount.getChangePermissionConfTime() != null
          && iatInstant.isBefore(userAccount.getChangePermissionConfTime())) {
        throw new PermissionConfChangeException("登录权限变更");
      }
    }
    filterChain.doFilter(request, response);
  }

  public String decodeToken(String token) {
    String[] chunks = token.split("\\.");
    Base64.Decoder decoder = Base64.getDecoder();
    return new String(decoder.decode(chunks[1]));

  }

}
