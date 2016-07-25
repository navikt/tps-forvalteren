package no.nav.tps.vedlikehold.service.command.authorisation.strategies;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class DiskresjonskodeAuthorisationServiceStrategyTest {

    private static final String FNR = "12345678910";

    private static final String ROLE_READ_DISKRESJONSKODE_6 = "0000-GA-GOSYS_KODE6";
    private static final String ROLE_READ_DISKRESJONSKODE_7 = "0000-GA-GOSYS_KODE7";

    @Mock
    private DiskresjonskodeConsumer diskresjonskodeConsumerMock;

    @Mock
    private HentDiskresjonskodeResponse hentDiskresjonskodeResponse;

    @Mock
    private User userMock;

    @InjectMocks
    private DiskresjonskodeAuthorisationServiceStrategy diskresjonskodeStrategy;

    @Before
    public void setUp() throws Exception {
        diskresjonskodeStrategy.setDiskresjonskodeConsumer(diskresjonskodeConsumerMock);
        diskresjonskodeStrategy.setUser(userMock);
        diskresjonskodeStrategy.setFnr(FNR);

        when( diskresjonskodeConsumerMock.getDiskresjonskode(anyString()) ).thenReturn(hentDiskresjonskodeResponse);

        when( hentDiskresjonskodeResponse.getDiskresjonskode() ).thenReturn("0");
    }

    @Test
    public void userIsNotAuthorisedIfExceptionIsThrown() throws Exception {
        when(diskresjonskodeConsumerMock.getDiskresjonskode(anyString())).thenThrow(new RuntimeException());

        Boolean result = diskresjonskodeStrategy.isAuthorised();

        assertThat(result, is(false));
    }

    @Test
    public void userIsNotAuthorisedForDiskresjonskode6IfRoleIsNotDefined() {
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn("6");
        when(userMock.getRoles()).thenReturn(newSet());

        Boolean result = diskresjonskodeStrategy.isAuthorised();

        assertThat(result, is(false));
    }

    @Test
    public void userIsAuthorisedForDiskresjonskode6IfRoleIsDefined() {
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn("6");
        when(userMock.getRoles()).thenReturn(newSet(ROLE_READ_DISKRESJONSKODE_6));

        Boolean result = diskresjonskodeStrategy.isAuthorised();

        assertThat(result, is(true));
    }

    @Test
    public void userIsNotAuthorisedForDiskresjonskode7IfRoleIsNotDefined() {
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn("7");
        when(userMock.getRoles()).thenReturn(newSet());

        Boolean result = diskresjonskodeStrategy.isAuthorised();

        assertThat(result, is(false));
    }

    @Test
    public void userIsAuthorisedForDiskresjonskode7IfRoleIsDefined() {
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn("7");
        when(userMock.getRoles()).thenReturn(newSet(ROLE_READ_DISKRESJONSKODE_7));

        Boolean result = diskresjonskodeStrategy.isAuthorised();

        assertThat(result, is(true));
    }

    @Test
    public void userIsAuthorisedForOtherDiskresjonskodes() {
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn("2");
        when(userMock.getRoles()).thenReturn(newSet());

        Boolean result = diskresjonskodeStrategy.isAuthorised();

        assertThat(result, is(true));
    }

    private <T> Set<T> newSet(T... strings) {
        return new HashSet<T>(
                Arrays.asList(strings)
        );
    }
}
