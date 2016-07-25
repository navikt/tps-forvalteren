package no.nav.tps.vedlikehold.service.command.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserFactoryTest {

    private static final String TOKEN              = "token";
    private static final String USERNAME           = "username";
    private static final String DISTINGUISHED_NAME = "distinguishedName";

    private static final Set<String> ROLES = new HashSet<>(Arrays.asList(
            "ROLE_READ_T",
            "ROLE_WRITE_T",
            "ROLE_READ_Q",
            "ROLE_WRITE_Q"
    ));

    @Mock
    private UserFactoryStrategy userFactoryStrategyMock;

    private DefaultUserFactory userFactory = new DefaultUserFactory();

    @Before
    public void setUp() {
        when(userFactoryStrategyMock.getUsername()).thenReturn(USERNAME);
        when(userFactoryStrategyMock.getDistinguishedName()).thenReturn(DISTINGUISHED_NAME);
        when(userFactoryStrategyMock.getToken()).thenReturn(TOKEN);
        when(userFactoryStrategyMock.getRoles()).thenReturn(ROLES);
    }

    @Test
    public void createUserConsumesTheProvidedStrategy() {
        User user = userFactory.createUser(userFactoryStrategyMock);

        assertThat(user.getName(), is(DISTINGUISHED_NAME));
        assertThat(user.getUsername(), is(USERNAME));
        assertThat(user.getToken(), is(TOKEN));
        assertThat(user.getRoles(), is(ROLES));

        verify(userFactoryStrategyMock).getDistinguishedName();
        verify(userFactoryStrategyMock).getUsername();
        verify(userFactoryStrategyMock).getToken();
        verify(userFactoryStrategyMock).getRoles();
    }
}
