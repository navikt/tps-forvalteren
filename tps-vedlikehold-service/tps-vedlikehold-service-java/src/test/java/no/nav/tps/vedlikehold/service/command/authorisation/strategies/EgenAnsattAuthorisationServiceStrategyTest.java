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
public class EgenAnsattAuthorisationServiceStrategyTest {

    private static final String FNR                  = "12345678910";
    private static final String ROLE_READ_EGENANSATT = "0000-GA-PIP_EGENANSATT";

    @Mock
    EgenAnsattConsumer egenAnsattConsumerMock;

    @Mock
    private User userMock;

    @InjectMocks
    EgenAnsattAuthorisationServiceStrategy egenAnsattAuthorisationStrategy;


    @Before
    public void setUp() {

    }


    @Test
    public void userIsNotAuthorisedIfEgenAnsattAndRoleIsUndefined() {
        when(egenAnsattConsumerMock.isEgenAnsatt(FNR)).thenReturn(true);
        when(userMock.getRoles()).thenReturn(newSet());

        Boolean result = egenAnsattAuthorisationStrategy.userIsAuthorisedToReadPerson(userMock, FNR);

        assertThat(result, is(false));
    }

    @Test
    public void userIsAuthorisedIfEgenAnsattAndRoleIsDefined() {
        when(egenAnsattConsumerMock.isEgenAnsatt(FNR)).thenReturn(true);
        when(userMock.getRoles()).thenReturn(newSet(ROLE_READ_EGENANSATT));

        Boolean result = egenAnsattAuthorisationStrategy.userIsAuthorisedToReadPerson(userMock, FNR);

        assertThat(result, is(true));
    }

    @Test
    public void userIsAuthorisedIfNotEgenAnsattAndRoleIsDefined() {
        when(egenAnsattConsumerMock.isEgenAnsatt(FNR)).thenReturn(false);
        when(userMock.getRoles()).thenReturn(newSet(ROLE_READ_EGENANSATT));

        Boolean result = egenAnsattAuthorisationStrategy.userIsAuthorisedToReadPerson(userMock, FNR);

        assertThat(result, is(true));
    }

    @Test
    public void userIsAuthorisedIfNotEgenAnsattAndRoleIsUndefined() {
        when(egenAnsattConsumerMock.isEgenAnsatt(FNR)).thenReturn(false);
        when(userMock.getRoles()).thenReturn(newSet());

        Boolean result = egenAnsattAuthorisationStrategy.userIsAuthorisedToReadPerson(userMock, FNR);

        assertThat(result, is(true));
    }

    private <T> Set<T> newSet(T... strings) {
        return new HashSet<T>(
                Arrays.asList(strings)
        );
    }

}
