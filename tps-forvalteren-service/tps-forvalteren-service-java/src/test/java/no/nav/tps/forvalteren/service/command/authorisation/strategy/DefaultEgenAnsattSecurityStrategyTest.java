package no.nav.tps.forvalteren.service.command.authorisation.strategy;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.service.command.exceptions.HttpUnauthorisedException;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultEgenAnsattSecurityStrategyTest {

    private static final String FNR_ANY = "any";
    private HashSet<UserRole> userRoles = new HashSet<>();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private UserContextHolder userContextHolder;

    @Mock
    private MessageProvider messageProvider;

    @Mock
    private DiskresjonskodeServiceRutineAuthorisation diskresjonskodeAuthorisation;

    @Mock
    private EgenAnsattServiceRutineAuthorisation egenAnsattAuthorisation;

    @Mock
    private ReadServiceRutineAuthorisation readAuthorisation;

    @Mock
    private EgenAnsattConsumer egenAnsattConsumer;

    @InjectMocks
    private DefaultEgenAnsattSecurityStrategy defaultEgenAnsattSecurityStrategy;

    @Before
    public void before() {
        userRoles.clear();
        when(userContextHolder.getRoles()).thenReturn(userRoles);
    }

    @Test
    public void isSupported() throws Exception {
        assertTrue(defaultEgenAnsattSecurityStrategy.isSupported(egenAnsattAuthorisation));
        assertFalse(defaultEgenAnsattSecurityStrategy.isSupported(diskresjonskodeAuthorisation));
        assertFalse(defaultEgenAnsattSecurityStrategy.isSupported(readAuthorisation));
    }

    @Test
    public void isAuthoriseReturnsTrueWhenUserHasRequiredRole() throws Exception {
        userRoles.add(UserRole.ROLE_EGEN_ANSATT_READ);
        when(egenAnsattConsumer.isEgenAnsatt(anyString())).thenReturn(true);

        boolean isAuthorised = defaultEgenAnsattSecurityStrategy.isAuthorised(FNR_ANY);
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isAuthorisedReturnsFalseWhenFnrIsEgenansattAndUserDontHaveRole() throws Exception {
        when(egenAnsattConsumer.isEgenAnsatt(anyString())).thenReturn(true);

        boolean isAuthorised = defaultEgenAnsattSecurityStrategy.isAuthorised( FNR_ANY);
        assertThat(isAuthorised, is(false));
    }

    @Test
    public void isAuthorisedReturnsTrueWhenFnrIsNotEgenansattAndUserDontHaveRole() throws Exception {
        when(egenAnsattConsumer.isEgenAnsatt(anyString())).thenReturn(false);

        boolean isAuthorised = defaultEgenAnsattSecurityStrategy.isAuthorised( FNR_ANY);
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void isAuthoriseReturnsTrueWhenFnrIsNotEgenansattAndUserHaveRole() throws Exception {
        userRoles.add(UserRole.ROLE_EGEN_ANSATT_READ);

        when(egenAnsattConsumer.isEgenAnsatt(anyString())).thenReturn(false);

        boolean isAuthorised = defaultEgenAnsattSecurityStrategy.isAuthorised( FNR_ANY);
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void handleUnauthorisedThrowsHttpUnauthorisedException() throws Exception{
        expectedException.expect(HttpUnauthorisedException.class);

        defaultEgenAnsattSecurityStrategy.handleUnauthorised();
        verify(messageProvider).get(anyString());
    }
}