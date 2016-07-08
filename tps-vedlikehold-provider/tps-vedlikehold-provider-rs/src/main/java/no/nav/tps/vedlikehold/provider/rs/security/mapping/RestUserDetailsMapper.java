package no.nav.tps.vedlikehold.provider.rs.security.mapping;

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

    public static final String SURNAME_ATTRIBUTE = "surname";
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
            String givenName = ctx.getStringAttribute(GIVEN_NAME_ATTRIBUTE);
            String surname =  ctx.getStringAttribute(SURNAME_ATTRIBUTE);

            return givenName + " " + surname;
        } else {
            return ctx.getNameInNamespace();
        }
    }
}
