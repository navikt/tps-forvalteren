package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.AuthorisationServiceStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.DiskresjonskodeAuthorisationServiceStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.EgenAnsattAuthorisationServiceStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class AuthorisationServiceTest {

    private static final String FNR         = "12345678910";
    private static final String ENVIRONMENT = "t1";

    @Mock
    private User userMock;

    @Mock
    private DiskresjonskodeAuthorisationServiceStrategy diskresjonskodeAuthorisationStrategy;

    @Mock
    private EgenAnsattAuthorisationServiceStrategy egenAnsattAuthorisationStrategy;

    @Mock
    private EgenAnsattConsumer egenAnsattConsumerMock;

    @Mock
    DiskresjonskodeConsumer diskresjonskodeConsumerMock;

    @Mock
    HentDiskresjonskodeResponse hentDiskresjonskodeResponseMock;

    @InjectMocks
    private AuthorisationService authorisationService;


    @Before
    public void setUp() throws Exception {
        when( diskresjonskodeAuthorisationStrategy.isAuthorised()).thenReturn(true);
        when( egenAnsattAuthorisationStrategy.isAuthorised() ).thenReturn(true);

        when(diskresjonskodeConsumerMock.getDiskresjonskode(eq(FNR))).thenReturn(hentDiskresjonskodeResponseMock);
        when(hentDiskresjonskodeResponseMock.getDiskresjonskode()).thenReturn("1");
    }

    @Test
    public void userIsUnauthorisedIfAnyStrategyReturnsFalse()  {
        when( egenAnsattAuthorisationStrategy.isAuthorised() ).thenReturn(false);

        Collection<AuthorisationServiceStrategy> strategies = Arrays.asList(
                diskresjonskodeAuthorisationStrategy,
                egenAnsattAuthorisationStrategy
        );

        Boolean result = authorisationService.isAuthorised(strategies);

        assertThat(result, is(false));
    }

    @Test
    public void userIsAuthorisedIfAllStrategiesReturnTrue()  {
        Collection<AuthorisationServiceStrategy> strategies = Arrays.asList(
                diskresjonskodeAuthorisationStrategy,
                egenAnsattAuthorisationStrategy
        );

        Boolean result = authorisationService.isAuthorised(strategies);

        assertThat(result, is(true));
    }

    @Test
    public void userIsAuthorisedOverloadUsesDiskresjonskodeEgenAnsattAndEnvironmentAuthorisation() throws Exception {
        ReflectionTestUtils.setField(
                authorisationService,
                "readRolesT",
                new ArrayList<>(Arrays.asList("readTRole")));

        ReflectionTestUtils.setField(
                authorisationService,
                "readRolesQ",
                new ArrayList<>());

        ReflectionTestUtils.setField(
                authorisationService,
                "readRolesU",
                new ArrayList<>());

        ReflectionTestUtils.setField(
                authorisationService,
                "readRolesP",
                new ArrayList<>());

        ReflectionTestUtils.setField(
                authorisationService,
                "readRolesO",
                new ArrayList<>());

        when(userMock.getRoles()).thenReturn(new HashSet<>(Arrays.asList("readTRole")));

        authorisationService.userIsAuthorisedToReadPersonInEnvironment(userMock, FNR, ENVIRONMENT);

        verify(diskresjonskodeConsumerMock).getDiskresjonskode(eq(FNR));
        verify(egenAnsattConsumerMock).isEgenAnsatt(eq(FNR));
    }

}
