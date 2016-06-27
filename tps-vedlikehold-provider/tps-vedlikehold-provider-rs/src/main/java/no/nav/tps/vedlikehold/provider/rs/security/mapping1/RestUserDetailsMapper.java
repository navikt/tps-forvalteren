package no.nav.tps.vedlikehold.provider.rs.security.mapping1;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

import java.util.Collection;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public class RestUserDetailsMapper extends LdapUserDetailsMapper {

    public static final String SURNAME_ATTRIBUTE = "sn";
    public static final String GIVEN_NAME_ATTRIBUTE = "givenName";

    @Override
    public LdapUserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        LdapUserDetailsImpl.Essence essence = new LdapUserDetailsImpl.Essence(ctx);
        essence.setUsername(username);
        essence.setAuthorities(authorities);
        essence.setDn(getDisplayName(ctx));
        return essence.createUserDetails();
    }

    private String getDisplayName(DirContextOperations ctx) {
        if (ctx.attributeExists(GIVEN_NAME_ATTRIBUTE) && ctx.attributeExists(SURNAME_ATTRIBUTE)) {
            return ctx.getStringAttribute(GIVEN_NAME_ATTRIBUTE) + " " + ctx.getStringAttribute(SURNAME_ATTRIBUTE);
        } else {
            return ctx.getNameInNamespace();
        }
    }
}
