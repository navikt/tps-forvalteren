package no.nav.tps.vedlikehold.provider.rs.security.mapping;

import no.nav.tps.vedlikehold.provider.rs.security.user.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import javax.naming.Name;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class RestUserDetailsMapperTest {
    private static final GrantedAuthority ROLE = UserRole.ROLE_READ_Q;
    private static final Set<GrantedAuthority> ROLES = newHashSet(ROLE);
    private static final String USERNAME = "username";
    private static final String SURNAME = "surname";
    private static final String GIVEN_NAME = "givenName";
    private static final String NAME_IN_NAMESPACE = "nameInNamespace";

    @Mock
    private DirContextAdapter dcaMock;

    @InjectMocks
    private RestUserDetailsMapper mapper;

    @Before
    public void before() {
        when(dcaMock.attributeExists(RestUserDetailsMapper.SURNAME_ATTRIBUTE)).thenReturn(true);
        when(dcaMock.attributeExists(RestUserDetailsMapper.GIVEN_NAME_ATTRIBUTE)).thenReturn(true);

        when(dcaMock.getStringAttribute(RestUserDetailsMapper.SURNAME_ATTRIBUTE)).thenReturn(SURNAME);
        when(dcaMock.getStringAttribute(RestUserDetailsMapper.GIVEN_NAME_ATTRIBUTE)).thenReturn(GIVEN_NAME);
        when(dcaMock.getNameInNamespace()).thenReturn(NAME_IN_NAMESPACE);
        when(dcaMock.getDn()).thenReturn(mock(Name.class));
    }

    @Test
    public void mapUserFromContext() {
        LdapUserDetails userDetails = mapper.mapUserFromContext(dcaMock, USERNAME, ROLES);

        assertThat(userDetails.getAuthorities(), contains(ROLE));
        assertThat(userDetails.getUsername(), is(USERNAME));
        assertThat(userDetails.getDn(), is(GIVEN_NAME + " " + SURNAME));
    }

    @Test
    public void mapUserFromContextUsingNameInNamespaceWhenMissingGivenName() {
        when(dcaMock.attributeExists(RestUserDetailsMapper.GIVEN_NAME_ATTRIBUTE)).thenReturn(false);

        LdapUserDetails userDetails = mapper.mapUserFromContext(dcaMock, USERNAME, ROLES);

        assertThat(userDetails.getAuthorities(), contains(ROLE));
        assertThat(userDetails.getUsername(), is(USERNAME));
        assertThat(userDetails.getDn(), is(NAME_IN_NAMESPACE));
    }

    @Test
    public void mapUserFromContextUsingNameInNamespaceWhenMissingSurname() {
        when(dcaMock.attributeExists(RestUserDetailsMapper.SURNAME_ATTRIBUTE)).thenReturn(false);

        LdapUserDetails userDetails = mapper.mapUserFromContext(dcaMock, USERNAME, ROLES);

        assertThat(userDetails.getAuthorities(), contains(ROLE));
        assertThat(userDetails.getUsername(), is(USERNAME));
        assertThat(userDetails.getDn(), is(NAME_IN_NAMESPACE));
    }
}
