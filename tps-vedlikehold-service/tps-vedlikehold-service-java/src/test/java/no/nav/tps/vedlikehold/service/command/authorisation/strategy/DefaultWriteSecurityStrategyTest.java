package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisation;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisation;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.ReadAuthorisation;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.WriteAuthorisation;
import no.nav.tps.vedlikehold.service.command.authorisation.RolesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by F148888 on 14.11.2016.
 */


@RunWith(MockitoJUnitRunner.class)
public class DefaultWriteSecurityStrategyTest {
    private HashSet<String> userRoles = new HashSet<>();
    private HashSet<String> rolesRequired = new HashSet<>();

    private static final String WRITE_ROLE = "0000-GA-NORG_Skriv";
    private static final String ENV = "any";

    @Mock
    private DiskresjonskodeAuthorisation diskresjonskodeAuthorisation;

    @Mock
    private EgenAnsattAuthorisation egenAnsattAuthorisation;

    @Mock
    private RolesService rolesService;

    @Mock
    private ReadAuthorisation readAuthorisation;

    @Mock
    private WriteAuthorisation writeAuthorisation;

    @InjectMocks
    private DefaultWriteSecurityStrategy defaultWriteSecurityStrategy;


    @Before
    public void setup(){
        rolesRequired.clear();
        userRoles.clear();

        when(rolesService.getRolesForEnvironment(ENV, RolesService.RoleType.WRITE)).thenReturn(rolesRequired);
    }

    @Test
    public void isSupported() {
        assertEquals(defaultWriteSecurityStrategy.isSupported(writeAuthorisation), true);
        assertEquals(defaultWriteSecurityStrategy.isSupported(diskresjonskodeAuthorisation), false);
        assertEquals(defaultWriteSecurityStrategy.isSupported(egenAnsattAuthorisation), false);
        assertEquals(defaultWriteSecurityStrategy.isSupported(readAuthorisation), false);
    }

    @Test
    public void authoriseThrowsUnauthorsiedWriteRolesIsMissing() {
        rolesRequired.add(WRITE_ROLE);
        boolean isAuthorised = defaultWriteSecurityStrategy.isAuthorised(userRoles, ENV);
        assertThat(isAuthorised, is(false));
    }

    @Test
    public void authoriseAcceptedWhenHaveWriteRoleForEnvironment() {
        rolesRequired.add(WRITE_ROLE);
        userRoles.add(WRITE_ROLE);
        boolean isAuthorised = defaultWriteSecurityStrategy.isAuthorised(userRoles, ENV);
        assertThat(isAuthorised, is(true));
    }

    @Test
    public void authorsieThrowsUnauthorisedWhenUserDontHaveAnyRolesEnvironment(){
        rolesRequired.add(WRITE_ROLE);
        boolean isAuthorised = defaultWriteSecurityStrategy.isAuthorised(userRoles, ENV);
        assertThat(isAuthorised, is(false));
    }
}