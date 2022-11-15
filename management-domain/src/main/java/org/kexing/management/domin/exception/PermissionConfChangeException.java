package org.kexing.management.domin.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author lh
 */
public class PermissionConfChangeException extends AuthenticationException {
    public PermissionConfChangeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PermissionConfChangeException(String msg) {
        super(msg);
    }
}
