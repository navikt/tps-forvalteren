package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.DiskresjonskodeAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.EgenAnsattAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ReadAuthorisation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by F148888 on 14.11.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultEgenAnsattSecurityStrategyTest {

    private static final String ROLE_READ_EGENANSATT = "0000-GA-GOSYS_UTVIDET";
    private static final String FNR_ANY = "any";
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
    private EgenAnsattConsumer egenAnsattConsumer;

    @InjectMocks
    private DefaultEgenAnsattSecurityStrategy defaultEgenAnsattSecurityStrategy;

    @Before
    public void before() {
        userRoles.clear();
    }

    @Test
    public void isSupported() throws Exception {
        assertEquals(defaultEgenAnsattSecurityStrategy.isSupported(egenAnsattAuthorisation), true);
        assertEquals(defaultEgenAnsattSecurityStrategy.isSupported(diskresjonskodeAuthorisation), false);
        assertEquals(defaultEgenAnsattSecurityStrategy.isSupported(readAuthorisation), false);
    }

    @Test
    public void authorise() throws Exception {
        userRoles.add(ROLE_READ_EGENANSATT);
        when(egenAnsattConsumer.isEgenAnsatt(anyString())).thenReturn(true);
        boolean isAuthorised = defaultEgenAnsattSecurityStrategy.isAuthorised(userRoles, FNR_ANY);
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isauthorisedReturnsFalseWhenFnrIsEgenansattAndUserDontHaveRole() throws Exception {
        when(egenAnsattConsumer.isEgenAnsatt(anyString())).thenReturn(true);
        boolean isAuthorised = defaultEgenAnsattSecurityStrategy.isAuthorised(userRoles, FNR_ANY);
        assertThat(isAuthorised, is(false));
    }

    @Test
    public void isAuthoriseReturnsTrueWhenFnrIsNotEgenansattAndUserDontHaveRole() throws Exception {
        when(egenAnsattConsumer.isEgenAnsatt(anyString())).thenReturn(false);
        boolean isAuthorised = defaultEgenAnsattSecurityStrategy.isAuthorised(userRoles, FNR_ANY);
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isAuthoriseReturnsTrueWhenFnrIsNotEgenansattAndUserHaveRole() throws Exception {
        userRoles.add(ROLE_READ_EGENANSATT);
        when(egenAnsattConsumer.isEgenAnsatt(anyString())).thenReturn(false);
        boolean isAuthorised = defaultEgenAnsattSecurityStrategy.isAuthorised(userRoles, FNR_ANY);
        assertThat(isAuthorised, is(true));
    }
}