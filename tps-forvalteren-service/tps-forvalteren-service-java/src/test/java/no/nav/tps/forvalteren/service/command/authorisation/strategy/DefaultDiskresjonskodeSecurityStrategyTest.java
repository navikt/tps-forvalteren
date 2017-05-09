package no.nav.tps.forvalteren.service.command.authorisation.strategy;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;
import no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DefaultDiskresjonskodeSecurityStrategyTest {

    private static final String KODE_SEKS = "6";
    private static final String KODE_SYV = "7";

    private HashSet<UserRole> userRoles = new HashSet<>();

    @Mock
    private DiskresjonskodeConsumer diskresjonskodeConsumer;

    @Mock
    private MessageProvider messageProvider;

    @Mock
    private DiskresjonskodeServiceRutineAuthorisation diskresjonskodeAuthorisation;

    @Mock
    private EgenAnsattServiceRutineAuthorisation egenAnsattAuthorisation;

    @Mock
    private ReadServiceRutineAuthorisation readAuthorisation;

    @Mock
    private HentDiskresjonskodeResponse hentDiskresjonskodeResponse;

    @Mock
    private UserContextHolder userContextHolderMock;

    @InjectMocks
    private DefaultDiskresjonskodeSecurityStrategy defaultDiskresjonskodeSecurityStrategy;

    @Before
    public void setup(){
        userRoles.clear();
        when(userContextHolderMock.getRoles()).thenReturn(userRoles);
    }

    @Test
    public void isSupported() throws Exception {
        assertTrue(defaultDiskresjonskodeSecurityStrategy.isSupported(diskresjonskodeAuthorisation));
        assertFalse(defaultDiskresjonskodeSecurityStrategy.isSupported(egenAnsattAuthorisation));
        assertFalse(defaultDiskresjonskodeSecurityStrategy.isSupported(readAuthorisation));
    }

    @Test
    public void isAuthorisedReturnsTrueForCodeSixWhenHasRequiredRoles() throws Exception {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn(KODE_SEKS);

        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_6_READ);
        boolean isAuthorised = defaultDiskresjonskodeSecurityStrategy.isAuthorised( "any");
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isAuthorisedReturnsTrueForCodeSevenWhenHasRequiredRoles() throws Exception {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn(KODE_SYV);

        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_7_READ);
        boolean isAuthorised = defaultDiskresjonskodeSecurityStrategy.isAuthorised( "any");
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isAuthorisedReturnsFalseForCodeSixIfRequiredRoleToReadPersonWithCodeSixIsMissing() throws Exception {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn(KODE_SEKS);

        boolean isAuthorised = defaultDiskresjonskodeSecurityStrategy.isAuthorised( "any");
        assertThat(isAuthorised, is(false));
    }

    @Test
    public void isAuthorisedReturnsFalseForCodeSevenIfRequiredRoleToReadPersonWithCodeSevenIsMissing() throws Exception {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn(KODE_SYV);

        boolean isAuthorised = defaultDiskresjonskodeSecurityStrategy.isAuthorised("any");
        assertThat(isAuthorised, is(false));
    }

    public void authorisationAlwaysSuccessfulWhenResultGotNoDiskresjonskoder() {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn("");

        assertThat(defaultDiskresjonskodeSecurityStrategy.isAuthorised("any"), is(true));

        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_6_READ);
        assertThat(defaultDiskresjonskodeSecurityStrategy.isAuthorised("any"), is(true));

        userRoles.remove(UserRole.ROLE_DISKRESJONESKODE_6_READ);

        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_7_READ);
        assertThat(defaultDiskresjonskodeSecurityStrategy.isAuthorised("any"), is(true));

        userRoles.remove(UserRole.ROLE_DISKRESJONESKODE_7_READ);

        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_7_READ);
        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_6_READ);
        assertThat(defaultDiskresjonskodeSecurityStrategy.isAuthorised("any"), is(true));
    }
}