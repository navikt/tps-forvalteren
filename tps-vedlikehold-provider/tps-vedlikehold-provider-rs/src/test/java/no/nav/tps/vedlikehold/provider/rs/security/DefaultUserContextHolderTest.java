package no.nav.tps.vedlikehold.provider.rs.security;

import no.nav.tps.vedlikehold.provider.rs.security.user.DefaultUserContextHolder;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserRole;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserContextHolderTest {

    private static final String USERNAME                        = "username";
    private static final String DISPLAY_NAME                    = "displayName";
    private static final List<? extends GrantedAuthority> ROLES = Arrays.asList(UserRole.READ, UserRole.WRITE);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private LdapUserDetails userDetailsMock;

    @Mock
    private SecurityContext securityContextMock;

    @InjectMocks
    private DefaultUserContextHolder userContextHolder;

    private Authentication authenticationMock = Mockito.mock(Authentication.class);

    @Before
    public void setUp() {
        SecurityContextHolder.setContext(securityContextMock);

        Mockito.doReturn(authenticationMock).when(securityContextMock).getAuthentication();
        Mockito.doReturn(userDetailsMock).when(authenticationMock).getPrincipal();

        Mockito.when( authenticationMock.isAuthenticated() ).thenReturn(true);
    }

    /* getDisplayName() */

    @Test
    public void getDisplayNameReturnsDisplayNameFromUserDetails() {
        Mockito.when( userDetailsMock.getDn() ).thenReturn(DISPLAY_NAME);
        assertThat(userContextHolder.getDisplayName(), is(DISPLAY_NAME));
    }

    @Test
    public void getDisplayNameThrowsExceptionIfPrincipalIsOfWrongType() {
        Principal principalMock = Mockito.mock(Principal.class);
        Mockito.when( authenticationMock.getPrincipal() ).thenReturn( principalMock );

        expectedException.expect(RuntimeException.class);
        //TODO: Test error message

        userContextHolder.getDisplayName();
    }

    /* getUsername() */
    @Test
    public void getUsernameReturnsUsernameFromUserDetails() {
        Mockito.when( userDetailsMock.getUsername() ).thenReturn(USERNAME);
        assertThat(userContextHolder.getUsername(), is(USERNAME));
    }

    @Test
    public void getUsernameThrowsExceptionIfPrincipalIsOfWrongType() {
        Principal principalMock = Mockito.mock(Principal.class);
        Mockito.when( authenticationMock.getPrincipal() ).thenReturn( principalMock );

        expectedException.expect(RuntimeException.class);
        //TODO: Test error message

        userContextHolder.getUsername();
    }

    /* getAuthentication() */

    @Test
    public void getAuthenticationReturnsObjectFromSecurityContext() {
        assertThat(userContextHolder.getAuthentication(), is(authenticationMock));
    }

    /* isAuthenticated() */

    @Test
    public void isAuthenticatedReturnsFalseIfAuthenticationIsNull() {
        Mockito.when( securityContextMock.getAuthentication() ).thenReturn(null);
        assertThat(userContextHolder.isAuthenticated(), is(false));
    }

    @Test
    public void isAuthenticatedReturnsFalseIfAuthenticationIsNotNullAndNotAuthenticated() {
        Mockito.when( authenticationMock.isAuthenticated() ).thenReturn(false);
        assertThat(userContextHolder.isAuthenticated(), is(false));
    }

    @Test
    public void isAuthenticatedReturnsTrueIfAuthenticationIsNotNullAndAuthenticated() {
        assertThat(userContextHolder.isAuthenticated(), is(true));
    }

    /* getRoles() */

    @Test
    public void getRolesReturnsRolesFromAuthentication() {
        Mockito.doReturn(ROLES).when(userDetailsMock).getAuthorities();
        assertThat(userContextHolder.getRoles(), containsInAnyOrder((GrantedAuthority)UserRole.READ, (GrantedAuthority) UserRole.WRITE));
    }

    @Test
    public void getRolesThrowsExceptionIfPrincipalIsOfWrongType() {
        Principal principalMock = Mockito.mock(Principal.class);
        Mockito.when( authenticationMock.getPrincipal() ).thenReturn( principalMock );

        expectedException.expect(RuntimeException.class);
        //TODO: Test error message

        userContextHolder.getRoles();
    }

    /* logout() */

    @Test
    public void logoutLogsOutIfAuthenticated() {
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpServletResponse responseMock = mock(HttpServletResponse.class);

        userContextHolder.logout(requestMock, responseMock);

        verify(securityContextMock).setAuthentication(null);
    }

    @Test
    public void logoutDoesNothingIfNotAuthenticated() {
        when(authenticationMock.isAuthenticated()).thenReturn(false);

        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpServletResponse responseMock = mock(HttpServletResponse.class);

        userContextHolder.logout(requestMock, responseMock);

        verify(securityContextMock, never()).setAuthentication(null);
    }
}
