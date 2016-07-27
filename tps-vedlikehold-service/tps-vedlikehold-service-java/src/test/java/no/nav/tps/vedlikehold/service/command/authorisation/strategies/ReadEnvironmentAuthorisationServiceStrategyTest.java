package no.nav.tps.vedlikehold.service.command.authorisation.strategies;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class ReadEnvironmentAuthorisationServiceStrategyTest {

    private static final String ROLE_READ_U = "readURoles";
    private static final String ROLE_READ_T = "readTRoles";
    private static final String ROLE_READ_Q = "readQRoles";
    private static final String ROLE_READ_P = "readPRoles";

    private static final String ENVIRONMENT_U = "u";
    private static final String ENVIRONMENT_T = "t";
    private static final String ENVIRONMENT_Q = "q";
    private static final String ENVIRONMENT_P = "p";

    @Mock
    private User userMock;

    @InjectMocks
    private ReadEnvironmentAuthorisationServiceStrategy readEnvironmentStrategy;

    @Before
    public void setUp() {
        readEnvironmentStrategy.setEnvironment(ENVIRONMENT_T);
    }

    @Test
    public void userIsAuthorisedIfItHasAValidRole() {
        readEnvironmentStrategy.addRoleForEnvironment(ROLE_READ_T, ENVIRONMENT_T);

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_T) );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(true));
    }

    @Test
    public void userIsNotAuthorisedIfItHasAValidRoleInAnotherEnvironment() {
        readEnvironmentStrategy.addRoleForEnvironment(ROLE_READ_T, ENVIRONMENT_T);
        readEnvironmentStrategy.addRoleForEnvironment(ROLE_READ_U, ENVIRONMENT_U);

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_U) );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(false));
    }

    @Test
    public void onlyOneValidRoleIsNeededToBeAuthorised() {

        readEnvironmentStrategy.addRolesForEnvironment(
                newSet(ROLE_READ_T, ROLE_READ_P),
                ENVIRONMENT_T
        );

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_T) );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(true));
    }

    @Test
    public void addingNewRolesKeepsPreviousRoles() {

        readEnvironmentStrategy.addRoleForEnvironment(
                ROLE_READ_T,
                ENVIRONMENT_T
        );

        readEnvironmentStrategy.addRoleForEnvironment(
                ROLE_READ_U,
                ENVIRONMENT_T
        );

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_T) );

        Boolean resultForT = readEnvironmentStrategy.isAuthorised();

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_U) );

        Boolean resultForU = readEnvironmentStrategy.isAuthorised();

        assertThat(resultForU, is(true));
        assertThat(resultForT, is(true));
    }

    @Test
    public void userIsNotAuthorisedIfEnvironmentDoesNotExist() {
        readEnvironmentStrategy.setEnvironment("!-");

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_Q, ROLE_READ_U, ROLE_READ_T) );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(false));
    }

    @Test
    public void userIsNotAuthorisedIfEnvironmentIsUndefined() {
        readEnvironmentStrategy.setEnvironment(null);

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_Q, ROLE_READ_U, ROLE_READ_T) );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(false));
    }



    private <T> Set<T> newSet(T... strings) {
        return new HashSet<T>(
                Arrays.asList(strings)
        );
    }
}
