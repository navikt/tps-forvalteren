package no.nav.tps.forvalteren.service.command.authorisation;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.service.command.authorisation.strategy.RestSecurityStrategy;
import no.nav.tps.forvalteren.service.command.authorisation.strategy.SearchSecurityStrategy;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DefaultDBAuthorisationServiceTest {

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
    private DefaultDBAuthorisationService authorisationService;

    @Test
    public void isAuthorisedToSeePersonReturnsTrueIfUserIsAuthorisedForAllSearchStrategies() {
        ServiceRutineAuthorisationStrategy a1 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy a2 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinitionRequest serviceRoutine = mock(TpsServiceRoutineDefinitionRequest.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(a1, a2));

        SearchSecurityStrategy s1 = mock(SearchSecurityStrategy.class);
        SearchSecurityStrategy s2 = mock(SearchSecurityStrategy.class);

        searchPersonSecurityStrategies.add(s1);
        searchPersonSecurityStrategies.add(s2);

        when(s1.isSupported(a1)).thenReturn(true);
        when(s1.isSupported(a2)).thenReturn(false);

        when(s2.isSupported(a1)).thenReturn(false);
        when(s2.isSupported(a2)).thenReturn(true);

        when(s1.isAuthorised(any())).thenReturn(true);
        when(s2.isAuthorised(any())).thenReturn(true);

        boolean isAuthorised = authorisationService.isAuthorisedToFetchPersonInfo(serviceRoutine, FNR);

        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isAuthorisedToSeePersonReturnsFalseIfUserNotIsAuthorisedForAllSearchStrategies() {
        ServiceRutineAuthorisationStrategy a1 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy a2 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinitionRequest serviceRoutine = mock(TpsServiceRoutineDefinitionRequest.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(a1, a2));

        SearchSecurityStrategy s1 = mock(SearchSecurityStrategy.class);
        SearchSecurityStrategy s2 = mock(SearchSecurityStrategy.class);

        searchPersonSecurityStrategies.add(s1);
        searchPersonSecurityStrategies.add(s2);

        when(s1.isSupported(a1)).thenReturn(true);
        when(s1.isSupported(a2)).thenReturn(false);

        when(s2.isSupported(a1)).thenReturn(false);
        when(s2.isSupported(a2)).thenReturn(true);

        when(s1.isAuthorised(any())).thenReturn(true);
        when(s2.isAuthorised(any())).thenReturn(false);

        boolean isAuthorised = authorisationService.isAuthorisedToFetchPersonInfo(serviceRoutine, FNR);

        assertThat(isAuthorised, is(false));
    }

    @Test
    public void authoriseRestCallWillCallHandleUnauthorizedForStrategiesThatAreSupportedButNotAuthorised() {

        ServiceRutineAuthorisationStrategy a1 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy a2 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy a3 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinitionRequest serviceRoutine = mock(TpsServiceRoutineDefinitionRequest.class);

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

        when(s1.isAuthorised()).thenReturn(true);
        when(s2.isAuthorised()).thenReturn(false);
        when(s3.isAuthorised()).thenReturn(false);

        authorisationService.authoriseRestCall(serviceRoutine);

        verify(s1, times(0)).handleUnauthorised();
        verify(s2, times(1)).handleUnauthorised();
        verify(s3, times(0)).handleUnauthorised();
    }

    @Test
    public void authoriseRestCallWillNotTriggerHandleUnauthorisedInAnyStrategyIfUserIsAuthorisedToUserServiceRutine(){
        ServiceRutineAuthorisationStrategy rutineAuthStrat1 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy rutineAuthStrat2 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy rutineAuthStrat3 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinitionRequest serviceRoutine = mock(TpsServiceRoutineDefinitionRequest.class);

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

        when(restStrat1.isAuthorised()).thenReturn(true);
        when(restStrat2.isAuthorised()).thenReturn(true);
        when(restStrat3.isAuthorised()).thenReturn(true);

        authorisationService.authoriseRestCall(serviceRoutine);

        verify(restStrat1, never()).handleUnauthorised();
        verify(restStrat2, never()).handleUnauthorised();
        verify(restStrat3, never()).handleUnauthorised();
    }


    @Test
    public void authorisePersonSearchWillCallHandleUnauthorizedForStrategiesThatAreSupportedButNotAuthorised() {

        ServiceRutineAuthorisationStrategy a1 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy a2 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy a3 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinitionRequest serviceRoutine = mock(TpsServiceRoutineDefinitionRequest.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(a1, a2, a3));

        SearchSecurityStrategy s1 = mock(SearchSecurityStrategy.class);
        SearchSecurityStrategy s2 = mock(SearchSecurityStrategy.class);
        SearchSecurityStrategy s3 = mock(SearchSecurityStrategy.class);

        searchPersonSecurityStrategies.add(s1);
        searchPersonSecurityStrategies.add(s2);
        searchPersonSecurityStrategies.add(s3);

        when(s1.isSupported(a1)).thenReturn(true);
        when(s1.isSupported(a2)).thenReturn(false);
        when(s1.isSupported(a3)).thenReturn(false);

        when(s2.isSupported(a1)).thenReturn(false);
        when(s2.isSupported(a2)).thenReturn(true);
        when(s2.isSupported(a3)).thenReturn(false);

        when(s3.isSupported(a1)).thenReturn(false);
        when(s3.isSupported(a2)).thenReturn(false);
        when(s3.isSupported(a3)).thenReturn(false);

        when(s1.isAuthorised(anyString())).thenReturn(true);
        when(s2.isAuthorised(anyString())).thenReturn(false);
        when(s3.isAuthorised(anyString())).thenReturn(false);

        authorisationService.authorisePersonSearch(serviceRoutine, FNR);

        verify(s1, times(0)).handleUnauthorised();
        verify(s2, times(1)).handleUnauthorised();
        verify(s3, times(0)).handleUnauthorised();
    }

    @Test
    public void authorisePersonSearchWillNotTriggerHandleUnauthorisedInAnyStrategyIfUserIsAuthorisedToUserServiceRutine(){
        ServiceRutineAuthorisationStrategy rutineAuthStrat1 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy rutineAuthStrat2 = mock(ServiceRutineAuthorisationStrategy.class);
        ServiceRutineAuthorisationStrategy rutineAuthStrat3 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinitionRequest serviceRoutine = mock(TpsServiceRoutineDefinitionRequest.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(rutineAuthStrat1, rutineAuthStrat2, rutineAuthStrat3));

        SearchSecurityStrategy s1 = mock(SearchSecurityStrategy.class);
        SearchSecurityStrategy s2 = mock(SearchSecurityStrategy.class);
        SearchSecurityStrategy s3 = mock(SearchSecurityStrategy.class);

        searchPersonSecurityStrategies.add(s1);
        searchPersonSecurityStrategies.add(s2);
        searchPersonSecurityStrategies.add(s3);

        when(s1.isSupported(rutineAuthStrat1)).thenReturn(true);
        when(s1.isSupported(rutineAuthStrat2)).thenReturn(true);

        when(s2.isSupported(rutineAuthStrat1)).thenReturn(true);
        when(s2.isSupported(rutineAuthStrat2)).thenReturn(true);

        when(s3.isSupported(rutineAuthStrat1)).thenReturn(true);
        when(s3.isSupported(rutineAuthStrat2)).thenReturn(true);

        when(s1.isAuthorised(anyString())).thenReturn(true);
        when(s2.isAuthorised(anyString())).thenReturn(true);
        when(s3.isAuthorised(anyString())).thenReturn(true);

        authorisationService.authorisePersonSearch(serviceRoutine, FNR);

        verify(s1, never()).handleUnauthorised();
        verify(s2, never()).handleUnauthorised();
        verify(s3, never()).handleUnauthorised();
    }

    @Test
    public void authorisedToUseServiceRutinesReturnsFalseIfRequiredRolesIsMissing() {
        ServiceRutineAuthorisationStrategy a1 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinitionRequest serviceRoutine = mock(TpsServiceRoutineDefinitionRequest.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(a1));

        RestSecurityStrategy s1 = mock(RestSecurityStrategy.class);

        restSecurityStrategies.add(s1);

        when(s1.isSupported(a1)).thenReturn(true);

        when(s1.isAuthorised()).thenReturn(false);

        Boolean res = authorisationService.isAuthorisedToUseServiceRutine(serviceRoutine);

        assertThat(res, is(false));
    }

    @Test
    public void authorisedToUseServiceRutineReturnsTrueIfCorrectRolesIsPresent() {
        ServiceRutineAuthorisationStrategy a1 = mock(ServiceRutineAuthorisationStrategy.class);

        TpsServiceRoutineDefinitionRequest serviceRoutine = mock(TpsServiceRoutineDefinitionRequest.class);

        when(serviceRoutine.getRequiredSecurityServiceStrategies()).thenReturn(Arrays.asList(a1));

        RestSecurityStrategy s1 = mock(RestSecurityStrategy.class);

        restSecurityStrategies.add(s1);

        when(s1.isSupported(a1)).thenReturn(true);

        when(s1.isAuthorised()).thenReturn(true);

        Boolean res = authorisationService.isAuthorisedToUseServiceRutine(serviceRoutine);

        assertThat(res, is(true));
    }
}
