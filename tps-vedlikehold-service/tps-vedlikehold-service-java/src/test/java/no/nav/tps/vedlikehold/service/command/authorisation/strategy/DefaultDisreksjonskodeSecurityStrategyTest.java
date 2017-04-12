package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.vedlikehold.service.user.UserRole;
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
public class DefaultDisreksjonskodeSecurityStrategyTest {

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

    @InjectMocks
    private DefaultDisreksjonskodeSecurityStrategy defaultDisreksjonskodeSecurityStrategy;

    @Test
    public void isSupported() throws Exception {
        assertTrue(defaultDisreksjonskodeSecurityStrategy.isSupported(diskresjonskodeAuthorisation));
        assertFalse(defaultDisreksjonskodeSecurityStrategy.isSupported(egenAnsattAuthorisation));
        assertFalse(defaultDisreksjonskodeSecurityStrategy.isSupported(readAuthorisation));
    }

    @Test
    public void isAuthorisedReturnsTrueForCodeSixWhenHasRequiredRoles() throws Exception {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn(KODE_SEKS);

        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_6_READ);
        boolean isAuthorised = defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any");
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isAuthorisedReturnsTrueForCodeSevenWhenHasRequiredRoles() throws Exception {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn(KODE_SYV);

        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_7_READ);
        boolean isAuthorised = defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any");
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isAuthorisedReturnsFalseForCodeSixIfRequiredRoleToReadPersonWithCodeSixIsMissing() throws Exception {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn(KODE_SEKS);

        boolean isAuthorised = defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any");
        assertThat(isAuthorised, is(false));
    }

    @Test
    public void isAuthorisedReturnsFalseForCodeSevenIfRequiredRoleToReadPersonWithCodeSevenIsMissing() throws Exception {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn(KODE_SYV);

        boolean isAuthorised = defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any");
        assertThat(isAuthorised, is(false));
    }

    public void authorisationAlwaysSuccessfulWhenResultGotNoDiskresjonskoder() {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn("");

        assertThat(defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any"), is(true));

        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_6_READ);
        assertThat(defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any"), is(true));

        userRoles.remove(UserRole.ROLE_DISKRESJONESKODE_6_READ);

        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_7_READ);
        assertThat(defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any"), is(true));

        userRoles.remove(UserRole.ROLE_DISKRESJONESKODE_7_READ);

        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_7_READ);
        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_6_READ);
        assertThat(defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any"), is(true));
    }
}