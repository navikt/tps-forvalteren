package no.nav.tps.vedlikehold.service.command.authorisation.strategies;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class ReadEnvironmentAuthorisationServiceStrategyTest {

    private static final String ROLE_READ_U = "0000-GA-NORG_Skriv";
    private static final String ROLE_READ_T = "0000-GA-NORG_Skriv";
    private static final String ROLE_READ_Q = "0000-GA-NORG_Skriv";

    private static final String ENVIRONMENT_U = "u2";
    private static final String ENVIRONMENT_T = "t4";
    private static final String ENVIRONMENT_Q = "q1";

    @Mock
    private User userMock;

    @InjectMocks
    ReadEnvironmentAuthorisationServiceStrategy readEnvironmentStrategy;

    @Before
    public void setUp() {
        readEnvironmentStrategy.setEnvironment(ENVIRONMENT_U);
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
