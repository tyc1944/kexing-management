package org.kexing.management.api_reset;

import org.apiguardian.api.API;
import org.kexing.management.domin.exception.LoginPasswordChangeException;
import org.kexing.management.domin.exception.PermissionConfChangeException;
import org.kexing.management.domin.exception.UserAccountDeletedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import static org.apiguardian.api.API.Status.STABLE;

@ControllerAdvice
public class ExceptionHandling extends com.yunmo.boot.web.ExceptionHandling {

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<Problem> handleInvalidBearerTokenException(
            final InvalidBearerTokenException exception,
            final NativeWebRequest request) {
    return ResponseEntity.status(Status.UNAUTHORIZED.getStatusCode())
        .body(
            Problem.builder()
                .withStatus(ExtStatus.UNAUTHORIZED_INVALID_TOKEN)
                .withTitle(ExtStatus.UNAUTHORIZED_INVALID_TOKEN.getReasonPhrase())
                .withDetail(ExtStatus.UNAUTHORIZED_INVALID_TOKEN.getReasonPhrase())
                .build());
    }

    @ExceptionHandler(PermissionConfChangeException.class)
    public ResponseEntity<Problem> handlePermissionConfChangeException(
            final PermissionConfChangeException exception,
            final NativeWebRequest request) {
        return ResponseEntity.status(Status.UNAUTHORIZED.getStatusCode()).body(Problem.builder()
                .withStatus(ExtStatus.UNAUTHORIZED_LOGIN_ACCOUNT_PERMISSION_CONF_CHANGE_TOKEN)
                .withTitle(ExtStatus.UNAUTHORIZED_LOGIN_ACCOUNT_PERMISSION_CONF_CHANGE_TOKEN.getReasonPhrase())
                .withDetail(ExtStatus.UNAUTHORIZED_LOGIN_ACCOUNT_PERMISSION_CONF_CHANGE_TOKEN.getReasonPhrase())
                .build());
    }

    @ExceptionHandler(LoginPasswordChangeException.class)
    public ResponseEntity<Problem> handleLoginPasswordChangeException(
            final LoginPasswordChangeException exception,
            final NativeWebRequest request) {
        return ResponseEntity.status(Status.UNAUTHORIZED.getStatusCode()).body(Problem.builder()
                .withStatus(ExtStatus.UNAUTHORIZED_LOGIN_ACCOUNT_PASSWORD_CHANGE_TOKEN)
                .withTitle(ExtStatus.UNAUTHORIZED_LOGIN_ACCOUNT_PASSWORD_CHANGE_TOKEN.getReasonPhrase())
                .withDetail(ExtStatus.UNAUTHORIZED_LOGIN_ACCOUNT_PASSWORD_CHANGE_TOKEN.getReasonPhrase())
                .build());
    }

    @ExceptionHandler(UserAccountDeletedException.class)
    public ResponseEntity<Problem> handleUserAccountDeletedException(
            final UserAccountDeletedException exception,
            final NativeWebRequest request) {
        return ResponseEntity.status(Status.UNAUTHORIZED.getStatusCode()).body(Problem.builder()
                .withStatus(ExtStatus.UNAUTHORIZED_LOGIN_ACCOUNT_DELETED)
                .withTitle(ExtStatus.UNAUTHORIZED_LOGIN_ACCOUNT_DELETED.getReasonPhrase())
                .withDetail(ExtStatus.UNAUTHORIZED_LOGIN_ACCOUNT_DELETED.getReasonPhrase())
                .build());
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEntityNotFound(
            final DisabledException exception,
            final NativeWebRequest request) {
        return ResponseEntity.status(Status.UNAUTHORIZED.getStatusCode()).body(Problem.builder()
                .withDetail("账户已禁用")
                .withStatus(ExtStatus.UNAUTHORIZED_DISABLED_ACCOUNT_TOKEN)
                .withTitle(ExtStatus.UNAUTHORIZED_DISABLED_ACCOUNT_TOKEN.getReasonPhrase())
                .withDetail(ExtStatus.UNAUTHORIZED_DISABLED_ACCOUNT_TOKEN.getReasonPhrase())
                .build());
    }


    public  enum ExtStatus implements StatusType {

        /**
         * 401 Unauthorized, see <a href=
         * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.2">HTTP/1.1
         * documentation</a>.
         */
        UNAUTHORIZED_INVALID_TOKEN(Status.UNAUTHORIZED.getStatusCode(), "无效的token"),
        UNAUTHORIZED_DISABLED_ACCOUNT_TOKEN(Status.UNAUTHORIZED.getStatusCode(), "账户已禁用"),
        UNAUTHORIZED_LOGIN_ACCOUNT_PASSWORD_CHANGE_TOKEN(Status.UNAUTHORIZED.getStatusCode(), "登录账户密码变更"),
        UNAUTHORIZED_LOGIN_ACCOUNT_PERMISSION_CONF_CHANGE_TOKEN(Status.UNAUTHORIZED.getStatusCode(), "登录账户权限变更"),
        UNAUTHORIZED_LOGIN_ACCOUNT_DELETED(Status.UNAUTHORIZED.getStatusCode(), "账户已删除"),
        ;


        private final int code;
        private final String reason;

        ExtStatus(final int statusCode, final String reasonPhrase) {
            this.code = statusCode;
            this.reason = reasonPhrase;
        }
        /**
         * Get the associated status code.
         *
         * @return the status code.
         */
        @Override
        public int getStatusCode() {
            return code;
        }

        /**
         * Get the reason phrase.
         *
         * @return the reason phrase.
         */
        @Override
        public String getReasonPhrase() {
            return reason;
        }

        /**
         * Get the Status String representation.
         *
         * @return the status code and reason.
         */
        @Override
        public String toString() {
            return getStatusCode() + " " + getReasonPhrase();
        }

    }


}
