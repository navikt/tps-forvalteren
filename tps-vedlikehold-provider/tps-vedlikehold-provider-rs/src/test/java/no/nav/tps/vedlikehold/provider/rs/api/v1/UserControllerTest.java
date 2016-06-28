package no.nav.tps.vedlikehold.provider.rs.api.v1;

import no.nav.tps.vedlikehold.domain.rs.User;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserRole;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final String DISPLAY_NAME    = "displayName";
    private static final String USERNAME        = "username";
    private static final String SESSION_ID      = "sessionID";
    private static final List<UserRole> ROLES   = Arrays.asList(ROLE_READ_T, ROLE_WRITE_T,ROLE_READ_Q, ROLE_WRITE_Q);

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
        when( userContextHolderMock.getDisplayName() ).thenReturn(DISPLAY_NAME);

        when( httpSessionMock.getId() ).thenReturn(SESSION_ID);
    }

    @Test
    public void getUserReturnsMappedUser() {
        User user = controller.getUser(httpSessionMock);

        assertThat(user.getRoles(), containsInAnyOrder(ROLE_READ_T.name(), ROLE_WRITE_T.name(), ROLE_READ_Q.name(), ROLE_WRITE_Q.name()));
        assertThat(user.getRoles(), hasSize(ROLES.size()));
        assertThat(user.getName(), is(DISPLAY_NAME));
        assertThat(user.getUsername(), is(USERNAME));
        assertThat(user.getToken(), is(SESSION_ID));
    }

}
