package no.nav.tps.vedlikehold.service.command.authorisation.strategies;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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

    private static final String ENVIRONMENT_U = "u2";
    private static final String ENVIRONMENT_T = "t4";
    private static final String ENVIRONMENT_Q = "q1";
    private static final String ENVIRONMENT_P = "p";

    @Mock
    private User userMock;

    @InjectMocks
    private ReadEnvironmentAuthorisationServiceStrategy readEnvironmentStrategy;

    @Before
    public void setUp() {
        readEnvironmentStrategy.setEnvironment(ENVIRONMENT_U);

        readEnvironmentStrategy.setReadTRoles(newSet(ROLE_READ_T));
        readEnvironmentStrategy.setReadURoles(newSet(ROLE_READ_U));
        readEnvironmentStrategy.setReadQRoles(newSet(ROLE_READ_Q));
        readEnvironmentStrategy.setReadPRoles(newSet(ROLE_READ_P));
    }

    @Test
    public void userIsAuthorisedInTIfItHasReadTRole() {
        readEnvironmentStrategy.setEnvironment(ENVIRONMENT_T);

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_T) );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(true));
    }

    @Test
    public void userIsNotAuthorisedInTIfItDoesNotHaveReadTRole() {
        readEnvironmentStrategy.setEnvironment(ENVIRONMENT_U);

        when(userMock.getRoles()).thenReturn( newSet() );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(false));
    }

    @Test
    public void userIsAuthorisedInUIfItHasReadURole() {
        readEnvironmentStrategy.setEnvironment(ENVIRONMENT_U);

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_U) );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(true));
    }

    @Test
    public void userIsNotAuthorisedInUIfItDoesNotHaveReadURole() {
        readEnvironmentStrategy.setEnvironment(ENVIRONMENT_U);

        when(userMock.getRoles()).thenReturn( newSet() );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(false));
    }

    @Test
    public void userIsAuthorisedInQIfItHasReadQRole() {
        readEnvironmentStrategy.setEnvironment(ENVIRONMENT_Q);

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_Q) );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(true));
    }

    @Test
    public void userIsNotAuthorisedInQIfItDoesNotHaveReadQRole() {
        readEnvironmentStrategy.setEnvironment(ENVIRONMENT_Q);

        when(userMock.getRoles()).thenReturn( newSet() );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(false));
    }

    @Test
    public void userIsAuthorisedInPIfItHasReadPRole() {
        readEnvironmentStrategy.setEnvironment(ENVIRONMENT_P);

        when(userMock.getRoles()).thenReturn( newSet(ROLE_READ_P) );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(true));
    }

    @Test
    public void userIsNotAuthorisedInPIfItDoesNotHaveReadPRole() {
        readEnvironmentStrategy.setEnvironment(ENVIRONMENT_P);

        when(userMock.getRoles()).thenReturn( newSet() );

        Boolean result = readEnvironmentStrategy.isAuthorised();

        assertThat(result, is(false));
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
