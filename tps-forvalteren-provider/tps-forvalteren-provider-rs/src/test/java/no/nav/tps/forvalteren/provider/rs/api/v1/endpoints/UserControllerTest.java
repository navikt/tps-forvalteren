package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;
import org.springframework.security.core.context.SecurityContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContextHolder;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final String DISTINGUISHED_NAME  = "distinguishedName";
    private static final String USERNAME            = "username";
    private static final String SESSION_ID          = "sessionID";
    private static final Set<UserRole> ROLES       = new HashSet<>(Arrays.asList(UserRole.ROLE_ACCESS));

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private UserContextHolder userContextHolderMock;

    @Mock
    private SecurityContext securityContextMock;

    @InjectMocks
    private UserController controller;

    @Before
    public void setUp() {
        SecurityContextHolder.setContext(securityContextMock);

        doReturn(ROLES).when(userContextHolderMock).getRoles();

        when( userContextHolderMock.getUsername() ).thenReturn(USERNAME);

        when( userContextHolderMock.getDisplayName() ).thenReturn(DISTINGUISHED_NAME);

        when( httpSessionMock.getId() ).thenReturn(SESSION_ID);
    }

    @Test
    public void getUserReturnsUserWithToken() {
        User user = mock(User.class);
        when(userContextHolderMock.getUser()).thenReturn(user);

        User result = controller.getUser(httpSessionMock);

//        assertThat(result, is(sameInstance(user)));
//        verify(user).setToken(SESSION_ID);    //TESTER nå med fiktiv bruker, for bruker er ikke nødvendig.
        assertThat(result.getUsername(), is("Bruker"));
        verify(user, never()).setToken(SESSION_ID);
    }

    @Test
    public void logoutLogsOutIfAuthenticated() {
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpServletResponse responseMock = mock(HttpServletResponse.class);

        when(userContextHolderMock.isAuthenticated()).thenReturn(true);
        controller.logout(requestMock, responseMock);

        // Metoden er kalt i SecurityContextLogoutHandler ved logout.
        verify(securityContextMock).setAuthentication(null);
    }

    @Test
    public void logoutDoesNothingIfNotAuthenticated() {
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpServletResponse responseMock = mock(HttpServletResponse.class);

        when(userContextHolderMock.isAuthenticated()).thenReturn(false);

        controller.logout(requestMock, responseMock);

        verify(securityContextMock, never()).setAuthentication(null);
    }

}
