package no.nav.tps.vedlikehold.provider.rs.api.v1.strategies.user;

import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_READ_Q;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_READ_T;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_WRITE_Q;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.ROLE_WRITE_T;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@RunWith(MockitoJUnitRunner.class)
public class UserContextUserFactoryStrategyTest {

    private static final String TOKEN              = "token";
    private static final String USERNAME           = "username";
    private static final String DISTINGUISHED_NAME = "distinguishedName";

    private static final Collection<GrantedAuthority> ROLES = Arrays.asList(
            ROLE_READ_T,
            ROLE_WRITE_T,
            ROLE_READ_Q,
            ROLE_WRITE_Q
    );

    @Mock
    private UserContextHolder userContextHolderMock;

    @Mock
    private HttpSession httpSessionMock;

    @InjectMocks
    private UserContextUserFactoryStrategy userContextUserFactoryStrategy;

    @Before
    public void setUp() {
        doReturn(ROLES).when(userContextHolderMock).getRoles();
        when(userContextHolderMock.getDisplayName()).thenReturn(DISTINGUISHED_NAME);
        when(userContextHolderMock.getUsername()).thenReturn(USERNAME);

        when(httpSessionMock.getId()).thenReturn(TOKEN);
    }

    @Test
    public void getRolesCallsGetRolesOnUserContextHolder() {
        userContextUserFactoryStrategy.getRoles();

        verify(userContextHolderMock).getRoles();
    }

    @Test
    public void getRolesProperlyConvertsRolesFromUserContextHolderToStrings() {
        Set<String> result = userContextUserFactoryStrategy.getRoles();

        assertThat(result, containsInAnyOrder(
                ROLE_READ_T.getAuthority(),
                ROLE_WRITE_T.getAuthority(),
                ROLE_READ_Q.getAuthority(),
                ROLE_WRITE_Q.getAuthority()
        ));
    }

    @Test
    public void getUsernameReturnsUsernameFromUserContextHolder() {
        String result = userContextUserFactoryStrategy.getUsername();

        assertThat(result, is(USERNAME));

        verify(userContextHolderMock).getUsername();
    }

    @Test
    public void getDistinguishedNameReturnsDistinguishedNameFromUserContextHolder() {
        String result = userContextUserFactoryStrategy.getDistinguishedName();

        assertThat(result, is(DISTINGUISHED_NAME));

        verify(userContextHolderMock).getDisplayName();
    }

    @Test
    public void getTokenReturnsIdFromHttpSession() {
        String result = userContextUserFactoryStrategy.getToken();

        assertThat(result, is(TOKEN));

        verify(httpSessionMock).getId();
    }
}
