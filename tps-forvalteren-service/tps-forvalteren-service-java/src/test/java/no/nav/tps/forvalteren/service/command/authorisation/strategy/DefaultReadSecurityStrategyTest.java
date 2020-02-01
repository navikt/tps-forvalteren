package no.nav.tps.forvalteren.service.command.authorisation.strategy;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;

@RunWith(MockitoJUnitRunner.class)
public class DefaultReadSecurityStrategyTest {

    private HashSet<UserRole> userRoles = new HashSet<>();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private DiskresjonskodeServiceRutineAuthorisation diskresjonskodeAuthorisation;

    @Mock
    private EgenAnsattServiceRutineAuthorisation egenAnsattAuthorisation;

    @Mock
    private ReadServiceRutineAuthorisation readAuthorisation;

    @Mock
    private UserContextHolder userContextHolderMock;

    @InjectMocks
    private DefaultReadSecurityStrategy defaultReadSecurityStrategy;

    @Before
    public void setup(){
        userRoles.clear();
        when(userContextHolderMock.getRoles()).thenReturn(userRoles);
    }

    @Test
    public void isSupported() throws Exception {
        assertTrue(defaultReadSecurityStrategy.isSupported(readAuthorisation));
        assertFalse(defaultReadSecurityStrategy.isSupported(diskresjonskodeAuthorisation));
        assertFalse(defaultReadSecurityStrategy.isSupported(egenAnsattAuthorisation));
    }

    @Test
    public void authoriseThrowsUnauthorisedIfMissingRole(){
        boolean isAuthorised = defaultReadSecurityStrategy.isAuthorised();
        assertThat(isAuthorised, is(false));
    }

    @Test
    public void authoriseDoesNotThrowExceptionWhenHaveRequiredRoles(){
        userRoles.add(UserRole.ROLE_ACCESS);
        boolean isAuthorised = defaultReadSecurityStrategy.isAuthorised();
        assertThat(isAuthorised, is(true));

    }

    @Test
    public void notAuthorisedIfHaveWrongRole(){
        addAllRolesToUser();
        userRoles.remove(UserRole.ROLE_ACCESS);

        boolean isAuthorised = defaultReadSecurityStrategy.isAuthorised();
        assertThat(isAuthorised, is(false));

    }

    private void addAllRolesToUser(){
        userRoles.add(UserRole.ROLE_ACCESS);
        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_7_READ);
        userRoles.add(UserRole.ROLE_DISKRESJONESKODE_6_READ);
        userRoles.add(UserRole.ROLE_EGEN_ANSATT_READ);
    }
}