package no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies;

import no.nav.tps.vedlikehold.domain.service.command.User.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class ReadAuthorisationStrategyTest {

    private static final String ROLE_READ_T = "readTRoles";
    private static final String ROLE_READ_P = "readPRoles";

    @Mock
    private User userMock;

    @InjectMocks
    private ReadAuthorisationStrategy readEnvironmentStrategy;

    @Before
    public void setUp() {
    }
/*
    @Test
    public void userIsAuthorisedIfItHasAValidRole() {
        readEnvironmentStrategy.setReadRoles(singleton(ROLE_READ_T));

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_T) );

        Boolean result = readEnvironmentStrategy.isAuthorised(userMock.getRoles());

        assertThat(result, is(true));
    }

    @Test
    public void onlyOneValidRoleIsNeededToBeAuthorised() {

        readEnvironmentStrategy.setReadRoles( newSet(ROLE_READ_T, ROLE_READ_P) );

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_P) );

        Boolean result = readEnvironmentStrategy.isAuthorised(userMock.getRoles());

        assertThat(result, is(true));
    }

    private <T> Set<T> newSet(T... strings) {
        return new HashSet<T>(
                Arrays.asList(strings)
        );
    }
    */
}
