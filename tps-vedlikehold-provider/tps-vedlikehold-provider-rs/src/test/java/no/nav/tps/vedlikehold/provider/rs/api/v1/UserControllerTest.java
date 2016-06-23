package no.nav.tps.vedlikehold.provider.rs.api.v1;

import no.nav.tps.vedlikehold.domain.rs.User;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserRole;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final String DISPLAY_NAME    = "displayName";
    private static final String USER_NAME       = "username";
    private static final String SESSION_ID      = "sessionID";
    private static final List<UserRole> ROLES   = Arrays.asList(UserRole.READ, UserRole.WRITE);

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private UserContextHolder userContextHolderMock;

    @InjectMocks
    private UserController controller;

    @Before
    public void setUp() {
        Mockito.doReturn(ROLES).when(userContextHolderMock).roles();
        Mockito.doReturn(USER_NAME).when(userContextHolderMock).getUsername();
        Mockito.doReturn(DISPLAY_NAME).when(userContextHolderMock).getDisplayName();

        Mockito.doReturn(SESSION_ID).when(httpSessionMock).getId();
    }

    @Test
    public void getUserReturnsMappedUser() {
        User user = controller.getUser(httpSessionMock);

        assertThat(user.getRoles(), containsInAnyOrder(UserRole.READ.name(),  UserRole.WRITE.name()));
        assertThat(user.getRoles().size(), is(ROLES.size()));
        assertThat(user.getName(), is(DISPLAY_NAME));
        assertThat(user.getUsername(), is(USER_NAME));
        assertThat(user.getToken(), is(SESSION_ID));
    }

}
