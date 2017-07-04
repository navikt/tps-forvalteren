package no.nav.tps.forvalteren.service.command.authorisation.strategy;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.WriteServiceRutineAuthorisation;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
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

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultWriteSecurityStrategyTest {


    private HashSet<UserRole> userRoles = new HashSet<>();

    @Mock
    private UserContextHolder userContextHolderMock;

    @Mock
    private DiskresjonskodeServiceRutineAuthorisation diskresjonskodeAuthorisation;

    @Mock
    private EgenAnsattServiceRutineAuthorisation egenAnsattAuthorisation;

    @Mock
    private ReadServiceRutineAuthorisation readAuthorisation;

    @Mock
    private WriteServiceRutineAuthorisation writeAuthorisation;

    @Mock
    private MessageProvider messageProviderMock;

    @InjectMocks
    private DefaultWriteSecurityStrategy defaultWriteSecurityStrategy;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        when(userContextHolderMock.getRoles()).thenReturn(userRoles);
    }

    @Test
    public void isSupported() {
        assertTrue(defaultWriteSecurityStrategy.isSupported(writeAuthorisation));
        assertFalse(defaultWriteSecurityStrategy.isSupported(diskresjonskodeAuthorisation));
        assertFalse(defaultWriteSecurityStrategy.isSupported(egenAnsattAuthorisation));
        assertFalse(defaultWriteSecurityStrategy.isSupported(readAuthorisation));
    }

    @Test
    public void authoriseThrowsUnauthorsiedWriteRolesIsMissing() {
        boolean isAuthorised = defaultWriteSecurityStrategy.isAuthorised();
        assertThat(isAuthorised, is(false));
    }

    @Test
    public void authoriseAcceptedWhenHaveCorrectRole() {
        userRoles.add(UserRole.ROLE_ACCESS);
        boolean isAuthorised = defaultWriteSecurityStrategy.isAuthorised();
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void authoriseReturnsFalseWhenUserHaveWrongRole(){
        addAllRolesToUser();
        userRoles.remove(UserRole.ROLE_ACCESS);

        boolean isAuthorised = defaultWriteSecurityStrategy.isAuthorised();
        assertThat(isAuthorised, is(false));
    }

    @Test
    public void handleUnauthorisedThrowsUnautorisedWhenUserDontHaveRequiredRoles() {
        exception.expect(HttpForbiddenException.class);
        defaultWriteSecurityStrategy.handleUnauthorised();
        verify(messageProviderMock).get(anyString());
    }

    private void addAllRolesToUser(){
        userRoles.add(UserRole.ROLE_ACCESS);
        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_7_READ);
        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_6_READ);
        userRoles.add(UserRole.ROLE_EGEN_ANSATT_READ);
    }
}