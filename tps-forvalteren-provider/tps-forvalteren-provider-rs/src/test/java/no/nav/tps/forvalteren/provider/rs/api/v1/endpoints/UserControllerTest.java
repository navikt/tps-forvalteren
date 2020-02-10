package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final String DISTINGUISHED_NAME  = "distinguishedName";
    private static final String USERNAME            = "username";
    private static final String SESSION_ID          = "sessionID";
    private static final Set<UserRole> ROLES       = new HashSet(asList(UserRole.ROLE_ACCESS));

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
