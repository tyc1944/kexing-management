package org.kexing.management.domin.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author lh
 */
public class LoginPasswordChangeException extends AuthenticationException {
    public LoginPasswordChangeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LoginPasswordChangeException(String msg) {
        super(msg);
    }
}
