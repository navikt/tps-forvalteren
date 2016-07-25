package no.nav.tps.vedlikehold.provider.rs.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Specifies that the given method requires read permissions
 *
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ROLE_WRITE_T', 'ROLE_WRITE_Q', 'ROLE_WRITE_P')")
public @interface RoleWrite {
}
