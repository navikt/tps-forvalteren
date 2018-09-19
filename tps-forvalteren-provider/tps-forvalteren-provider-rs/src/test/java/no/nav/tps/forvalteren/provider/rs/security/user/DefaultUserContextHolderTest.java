package no.nav.tps.forvalteren.provider.rs.security.user;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.user.UserRole;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserContextHolderTest {

    private static final String USERNAME = "username";
    private static final String DISPLAY_NAME = "displayName";
    private static final List<? extends GrantedAuthority> ROLES = Arrays.asList(UserRole.ROLE_ACCESS, UserRole.ROLE_EGEN_ANSATT_READ);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private LdapUserDetails userDetailsMock;

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @InjectMocks
    private DefaultUserContextHolder userContextHolder;

    @Before
    public void setUp() {
        SecurityContextHolder.setContext(securityContextMock);

        doReturn(authenticationMock).when(securityContextMock).getAuthentication();

        doReturn(userDetailsMock).when(authenticationMock).getPrincipal();

        when(authenticationMock.isAuthenticated()).thenReturn(true);

        doReturn(ROLES).when(authenticationMock).getAuthorities();

        when(userDetailsMock.getUsername()).thenReturn(USERNAME);

        when(userDetailsMock.getDn()).thenReturn(DISPLAY_NAME);
    }

    @Test
    public void getDisplayNameReturnsDisplayNameFromUserDetails() {
        assertThat(userContextHolder.getDisplayName(), is(DISPLAY_NAME));
    }

    @Test
    public void getDisplayNameThrowsExceptionIfPrincipalIsOfWrongType() {
        Principal principalMock = mock(Principal.class);
        when(authenticationMock.getPrincipal()).thenReturn(principalMock);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(containsString(principalMock.getClass().toString()));

        userContextHolder.getDisplayName();
    }

    @Test
    public void getUsernameReturnsUsernameFromUserDetails() {
        assertThat(userContextHolder.getUsername(), is(USERNAME));
    }

    @Test
    public void getUsernameThrowsExceptionIfPrincipalIsOfWrongType() {
        Principal principalMock = mock(Principal.class);

        when(authenticationMock.getPrincipal()).thenReturn(principalMock);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(containsString(principalMock.getClass().toString()));

        userContextHolder.getUsername();
    }

    @Test
    public void getAuthenticationReturnsObjectFromSecurityContext() {
        userContextHolder.isAuthenticated();
        verify(authenticationMock).isAuthenticated();
    }

    @Test
    public void isAuthenticatedReturnsFalseIfAuthenticationIsNull() {
        when(securityContextMock.getAuthentication()).thenReturn(null);

        assertThat(userContextHolder.isAuthenticated(), is(false));
    }

    @Test
    public void isAuthenticatedReturnsFalseIfAuthenticationIsNotNullAndNotAuthenticated() {
        when(authenticationMock.isAuthenticated()).thenReturn(false);

        assertThat(userContextHolder.isAuthenticated(), is(false));
    }

    @Test
    public void isAuthenticatedReturnsTrueIfAuthenticationIsNotNullAndAuthenticated() {
        assertThat(userContextHolder.isAuthenticated(), is(true));
    }

    @Test
    public void getUserReturnsUser() {
        User result = userContextHolder.getUser();

        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is(DISPLAY_NAME));
        assertThat(result.getUsername(), is(USERNAME));
        assertThat(result.getToken(), is(nullValue()));
    }

    @Test
    public void getRolesReturnsRolesFromAuthentication() {
        assertThat(userContextHolder.getRoles(), containsInAnyOrder(UserRole.ROLE_ACCESS, UserRole.ROLE_EGEN_ANSATT_READ));
    }

    @Test
    public void getUserReturnUserContainingRolesInStringFormat(){
        userContextHolder.getRoles()
                .containsAll(
                Arrays.asList(
                        UserRole.ROLE_ACCESS.toString(),
                        UserRole.ROLE_EGEN_ANSATT_READ)
        );
    }

}
