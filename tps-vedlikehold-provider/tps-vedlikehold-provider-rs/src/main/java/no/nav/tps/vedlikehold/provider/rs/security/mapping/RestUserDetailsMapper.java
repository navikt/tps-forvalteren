package no.nav.tps.vedlikehold.provider.rs.security.mapping;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

import java.util.Collection;


public class RestUserDetailsMapper extends LdapUserDetailsMapper {

    private static final String DISPLAY_NAME_ATTRIBUTE = "displayname";

    @Override
    public LdapUserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        LdapUserDetailsImpl.Essence essence = new LdapUserDetailsImpl.Essence(ctx);

        essence.setUsername(username);
        essence.setAuthorities(authorities);
        essence.setDn(getDistinguishedName(ctx));

        return essence.createUserDetails();
    }

    private String getDistinguishedName(DirContextOperations ctx) {
        if (ctx.attributeExists(DISPLAY_NAME_ATTRIBUTE)) {
            return ctx.getStringAttribute(DISPLAY_NAME_ATTRIBUTE);
        } else {
            return ctx.getNameInNamespace();
        }
    }
}
