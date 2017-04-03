package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_READ_Q;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_READ_T;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_WRITE_Q;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_WRITE_T;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.nav.tps.vedlikehold.domain.service.user.User;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserRole;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final String DISTINGUISHED_NAME  = "distinguishedName";
    private static final String USERNAME            = "username";
    private static final String SESSION_ID          = "sessionID";
    private static final List<UserRole> ROLES       = Arrays.asList(ROLE_READ_T, ROLE_WRITE_T,ROLE_READ_Q, ROLE_WRITE_Q);

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private UserContextHolder userContextHolderMock;

    @InjectMocks
    private UserController controller;

    @Before
    public void setUp() {
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

        assertThat(result, is(sameInstance(user)));
        verify(user).setToken(SESSION_ID);
    }

    @Test
    public void logoutCallsLogoutOnUserContextHolder() {
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpServletResponse responseMock = mock(HttpServletResponse.class);

        controller.logout(requestMock, responseMock);

        verify(userContextHolderMock).logout(requestMock, responseMock);
    }

}
