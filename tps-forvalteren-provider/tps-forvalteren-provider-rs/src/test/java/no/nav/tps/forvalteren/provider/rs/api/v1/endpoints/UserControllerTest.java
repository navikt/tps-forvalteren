package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private HttpSession httpSession;

    @Mock
    private UserContextHolder userContextHolderMock;

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private UserController controller;

    @Before
    public void setUp() {
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    public void logoutLogsOutIfAuthenticated() {
        when(userContextHolderMock.isAuthenticated()).thenReturn(true);
        controller.logout();

        // Metoden er kalt i SecurityContextLogoutHandler ved logout.
        verify(securityContextMock).setAuthentication(null);
    }

    @Test
    public void logoutDoesNothingIfNotAuthenticated() {
        when(userContextHolderMock.isAuthenticated()).thenReturn(false);

        controller.logout();

        verify(securityContextMock, never()).setAuthentication(null);
    }
}
