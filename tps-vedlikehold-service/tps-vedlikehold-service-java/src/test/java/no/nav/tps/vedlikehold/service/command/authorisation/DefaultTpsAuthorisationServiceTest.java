package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.User.User;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.RestSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SearchSecurityStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultTpsAuthorisationServiceTest {

    private static final String FNR = "01018012345";
    private static final String ENVIRONMENT = "environment";


    @Spy
    private List<SearchSecurityStrategy> searchPersonSecurityStrategies = new ArrayList<>();

    @Spy
    private List<RestSecurityStrategy> restSecurityStrategies = new ArrayList<>();

    @InjectMocks
    private DefaultTpsAuthorisationService command;

    @Before
    public void before() {
    }


    @Test
    public void isAuthorisedToSeePersonReturnsTrueIfUserIsAuthorisedForAllSearchStrategies() {
        AuthorisationStrategy a1 = mock(AuthorisationStrategy.class);
        AuthorisationStrategy a2 = mock(AuthorisationStrategy.class);

        TpsServiceRoutineDefinition serviceRoutine = mock(TpsServiceRoutineDefinition.class);

        when(serviceRoutine.getSecurityServiceStrategies()).thenReturn(Arrays.asList(a1, a2));

        SearchSecurityStrategy s1 = mock(SearchSecurityStrategy.class);
        SearchSecurityStrategy s2 = mock(SearchSecurityStrategy.class);

        searchPersonSecurityStrategies.add(s1);
        searchPersonSecurityStrategies.add(s2);

        when(s1.isSupported(a1)).thenReturn(true);
        when(s1.isSupported(a2)).thenReturn(false);

        when(s2.isSupported(a1)).thenReturn(false);
        when(s2.isSupported(a2)).thenReturn(true);

        when(s1.isAuthorised(any(), any())).thenReturn(true);
        when(s2.isAuthorised(any(), any())).thenReturn(true);

        User user = new User("name", "username", Sets.newSet("rolle"));

        boolean isAuthorised = command.isAuthorisedToSeePerson(serviceRoutine, FNR, user);

        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isAuthorisedToSeePersonReturnsFalseIfUserNotIsAuthorisedForAllSearchStrategies() {
        AuthorisationStrategy a1 = mock(AuthorisationStrategy.class);
        AuthorisationStrategy a2 = mock(AuthorisationStrategy.class);

        TpsServiceRoutineDefinition serviceRoutine = mock(TpsServiceRoutineDefinition.class);

        when(serviceRoutine.getSecurityServiceStrategies()).thenReturn(Arrays.asList(a1, a2));

        SearchSecurityStrategy s1 = mock(SearchSecurityStrategy.class);
        SearchSecurityStrategy s2 = mock(SearchSecurityStrategy.class);

        searchPersonSecurityStrategies.add(s1);
        searchPersonSecurityStrategies.add(s2);

        when(s1.isSupported(a1)).thenReturn(true);
        when(s1.isSupported(a2)).thenReturn(false);

        when(s2.isSupported(a1)).thenReturn(false);
        when(s2.isSupported(a2)).thenReturn(true);

        when(s1.isAuthorised(any(), any())).thenReturn(true);
        when(s2.isAuthorised(any(), any())).thenReturn(false);

        User user = new User("name", "username", Sets.newSet("rolle"));

        boolean isAuthorised = command.isAuthorisedToSeePerson(serviceRoutine, FNR, user);

        assertThat(isAuthorised, is(false));
    }


    @Test
    public void authoriseRestCallsHandleUnauthorizedForSupportedAndUnauthorizedStrategies() {

        AuthorisationStrategy a1 = mock(AuthorisationStrategy.class);
        AuthorisationStrategy a2 = mock(AuthorisationStrategy.class);
        AuthorisationStrategy a3 = mock(AuthorisationStrategy.class);

        TpsServiceRoutineDefinition serviceRoutine = mock(TpsServiceRoutineDefinition.class);

        when(serviceRoutine.getSecurityServiceStrategies()).thenReturn(Arrays.asList(a1, a2, a3));

        RestSecurityStrategy s1 = mock(RestSecurityStrategy.class);
        RestSecurityStrategy s2 = mock(RestSecurityStrategy.class);
        RestSecurityStrategy s3 = mock(RestSecurityStrategy.class);

        restSecurityStrategies.add(s1);
        restSecurityStrategies.add(s2);
        restSecurityStrategies.add(s3);

        when(s1.isSupported(a1)).thenReturn(true);
        when(s1.isSupported(a2)).thenReturn(false);
        when(s1.isSupported(a3)).thenReturn(false);

        when(s2.isSupported(a1)).thenReturn(false);
        when(s2.isSupported(a2)).thenReturn(true);
        when(s2.isSupported(a3)).thenReturn(false);

        when(s3.isSupported(a1)).thenReturn(false);
        when(s3.isSupported(a2)).thenReturn(false);
        when(s3.isSupported(a3)).thenReturn(false);

        when(s1.isAuthorised(any(), any())).thenReturn(true);
        when(s2.isAuthorised(any(), any())).thenReturn(false);
        when(s3.isAuthorised(any(), any())).thenReturn(false);

        Set<String> roles = Sets.newSet("rolle");
        User user = new User("name", "username", roles);

        command.authoriseRestCall(serviceRoutine, ENVIRONMENT, user);

        verify(s1, times(0)).handleUnauthorised(eq(roles), anyString());
        verify(s2, times(1)).handleUnauthorised(eq(roles), anyString());
        verify(s3, times(0)).handleUnauthorised(eq(roles), anyString());

    }
}
