package no.nav.tps.vedlikehold.provider.rs.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the given method requires read permissions
 * <p>
 * Created by Tobias Hansen (Visma Consulting AS)
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ROLE_READ_T', 'ROLE_READ_Q', 'ROLE_READ_P')")
public @interface RoleRead {
}