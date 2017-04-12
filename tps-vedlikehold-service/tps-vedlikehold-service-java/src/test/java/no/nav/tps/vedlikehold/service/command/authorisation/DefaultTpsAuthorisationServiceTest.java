package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.RestSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SearchSecurityStrategy;
import no.nav.tps.vedlikehold.service.user.UserContextHolder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class DefaultTpsAuthorisationServiceTest {

    private static final String FNR = "01018012345";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private UserContextHolder userContextHolderMock;

    @Spy
    private List<SearchSecurityStrategy> searchPersonSecurityStrategies = new ArrayList<>();

    @Spy
    private List<RestSecurityStrategy> restSecurityStrategies = new ArrayList<>();

    @InjectMocks
    private DefaultTpsAuthorisationService command;

    @Test
    public void isAuthorisedToSeePersonReturnsTrueIfUserIsAuthorisedForAllSearchStrategies() {
        ServiceRutineAuthorisationStrategy a1 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy a2 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinition serviceRoutine = mock(TpsServiceRoutineDefinition.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(a1, a2));

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

        boolean isAuthorised = command.isAuthorisedToSeePerson(serviceRoutine, FNR);

        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isAuthorisedToSeePersonReturnsFalseIfUserNotIsAuthorisedForAllSearchStrategies() {
        ServiceRutineAuthorisationStrategy a1 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy a2 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinition serviceRoutine = mock(TpsServiceRoutineDefinition.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(a1, a2));

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

        boolean isAuthorised = command.isAuthorisedToSeePerson(serviceRoutine, FNR);

        assertThat(isAuthorised, is(false));
    }


    @Test
    public void authoriseRestCallsHandleUnauthorizedForStrategiesThatAreSupportedAndNotAuthorised() {

        ServiceRutineAuthorisationStrategy a1 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy a2 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy a3 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinition serviceRoutine = mock(TpsServiceRoutineDefinition.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(a1, a2, a3));

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

        when(s1.isAuthorised(any())).thenReturn(true);
        when(s2.isAuthorised(any())).thenReturn(false);
        when(s3.isAuthorised(any())).thenReturn(false);

        command.authoriseRestCall(serviceRoutine);

        verify(s1, times(0)).handleUnauthorised();
        verify(s2, times(1)).handleUnauthorised();
        verify(s3, times(0)).handleUnauthorised();
    }

    @Test
    public void authoriseRestCallWillNotTriggerHandleUnauthorisedInAnyStrategyIfUserIsAuthorisedToUserServiceRutine(){
        ServiceRutineAuthorisationStrategy rutineAuthStrat1 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy rutineAuthStrat2 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy rutineAuthStrat3 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinition serviceRoutine = mock(TpsServiceRoutineDefinition.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(rutineAuthStrat1, rutineAuthStrat2, rutineAuthStrat3));

        RestSecurityStrategy restStrat1 = mock(RestSecurityStrategy.class);
        RestSecurityStrategy restStrat2 = mock(RestSecurityStrategy.class);
        RestSecurityStrategy restStrat3 = mock(RestSecurityStrategy.class);

        restSecurityStrategies.add(restStrat1);
        restSecurityStrategies.add(restStrat2);
        restSecurityStrategies.add(restStrat3);

        when(restStrat1.isSupported(rutineAuthStrat1)).thenReturn(true);
        when(restStrat1.isSupported(rutineAuthStrat2)).thenReturn(true);

        when(restStrat2.isSupported(rutineAuthStrat1)).thenReturn(true);
        when(restStrat2.isSupported(rutineAuthStrat2)).thenReturn(true);

        when(restStrat3.isSupported(rutineAuthStrat1)).thenReturn(true);
        when(restStrat3.isSupported(rutineAuthStrat2)).thenReturn(true);

        when(restStrat1.isAuthorised(any())).thenReturn(true);
        when(restStrat2.isAuthorised(any())).thenReturn(true);
        when(restStrat3.isAuthorised(any())).thenReturn(true);

        command.authoriseRestCall(serviceRoutine);

        verify(restStrat1, never()).handleUnauthorised();
        verify(restStrat2, never()).handleUnauthorised();
        verify(restStrat3, never()).handleUnauthorised();

    }

    @Test
    public void authorisedToUseServiceRutinesReturnsFalseIfRequiredRolesIsMissing() {
        ServiceRutineAuthorisationStrategy a1 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinition serviceRoutine = mock(TpsServiceRoutineDefinition.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(a1));

        RestSecurityStrategy s1 = mock(RestSecurityStrategy.class);

        restSecurityStrategies.add(s1);

        when(s1.isSupported(a1)).thenReturn(true);

        when(s1.isAuthorised(any())).thenReturn(false);

        Boolean res = command.isAuthorisedToUseServiceRutine(serviceRoutine);

        assertThat(res, is(false));
    }

    @Test
    public void authorisedToUseServiceRutineReturnsTrueIfCorrectRolesIsPresent() {
        ServiceRutineAuthorisationStrategy a1 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinition serviceRoutine = mock(TpsServiceRoutineDefinition.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(a1));

        RestSecurityStrategy s1 = mock(RestSecurityStrategy.class);

        restSecurityStrategies.add(s1);

        when(s1.isSupported(a1)).thenReturn(true);

        when(s1.isAuthorised(any())).thenReturn(true);

        Boolean res = command.isAuthorisedToUseServiceRutine(serviceRoutine);

        assertThat(res, is(true));
    }
}
