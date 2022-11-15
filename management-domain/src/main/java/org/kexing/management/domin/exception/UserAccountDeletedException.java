package org.kexing.management.domin.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author lh
 */
public class UserAccountDeletedException extends AuthenticationException {
    public UserAccountDeletedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserAccountDeletedException(String msg) {
        super(msg);
    }
}
