package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisation;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisation;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.ReadAuthorisation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by F148888 on 11.11.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultDisreksjonskodeSecurityStrategyTest {

    private static final String KODE_SEKS = "6";
    private static final String KODE_SYV = "7";
    private static final String ROLE_READ_DISKRESJONSKODE_6 = "0000-GA-GOSYS_KODE6";
    private static final String ROLE_READ_DISKRESJONSKODE_7 = "0000-GA-GOSYS_KODE7";

    private HashSet<String> userRoles = new HashSet<>();

    @Mock
    private DiskresjonskodeConsumer diskresjonskodeConsumer;

    @Mock
    private MessageProvider messageProvider;

    @Mock
    private DiskresjonskodeAuthorisation diskresjonskodeAuthorisation;

    @Mock
    private EgenAnsattAuthorisation egenAnsattAuthorisation;

    @Mock
    private ReadAuthorisation readAuthorisation;

    @Mock
    private HentDiskresjonskodeResponse hentDiskresjonskodeResponse;

    @InjectMocks
    private DefaultDisreksjonskodeSecurityStrategy defaultDisreksjonskodeSecurityStrategy;

    @Test
    public void isSupported() throws Exception {
        assertEquals(defaultDisreksjonskodeSecurityStrategy.isSupported(diskresjonskodeAuthorisation), true);
        assertEquals(defaultDisreksjonskodeSecurityStrategy.isSupported(egenAnsattAuthorisation), false);
        assertEquals(defaultDisreksjonskodeSecurityStrategy.isSupported(readAuthorisation), false);
    }

    @Test
    public void isAuthoriseReturnsTrueWhenUserHasRequiredRoles() throws Exception {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn(KODE_SEKS);

        userRoles.add(ROLE_READ_DISKRESJONSKODE_6);
        userRoles.add(ROLE_READ_DISKRESJONSKODE_7);
        boolean isAuthorised = defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any");
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isAuthorisedReturnsFalseForCodeSix() throws Exception {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn(KODE_SEKS);

        boolean isAuthorised = defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any");
        assertThat(isAuthorised, is(false));
    }

    @Test
    public void isAuthorisedReturnsFalseForCodeSeven() throws Exception {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn(KODE_SYV);

        boolean isAuthorised = defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any");
        assertThat(isAuthorised, is(false));
    }

    public void authorisationAlwaysSuccessfulWhenResultGotNoDiskresjonskoder() {
        when(diskresjonskodeConsumer.getDiskresjonskodeResponse(anyString())).thenReturn(hentDiskresjonskodeResponse);
        when(hentDiskresjonskodeResponse.getDiskresjonskode()).thenReturn("");

        assertThat(defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any"), is(true));

        userRoles.add(ROLE_READ_DISKRESJONSKODE_6);
        assertThat(defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any"), is(true));

        userRoles.add(ROLE_READ_DISKRESJONSKODE_7);
        assertThat(defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any"), is(true));

        userRoles.remove(ROLE_READ_DISKRESJONSKODE_6);
        assertThat(defaultDisreksjonskodeSecurityStrategy.isAuthorised(userRoles, "any"), is(true));
    }
}