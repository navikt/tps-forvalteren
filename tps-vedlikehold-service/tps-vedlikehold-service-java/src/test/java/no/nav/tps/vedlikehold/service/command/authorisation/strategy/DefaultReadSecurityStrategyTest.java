package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisation;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisation;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.ReadAuthorisation;
import no.nav.tps.vedlikehold.service.command.authorisation.RolesService;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by F148888 on 14.11.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultReadSecurityStrategyTest {

    private HashSet<String> userRoles = new HashSet<>();
    private HashSet<String> requiredRoles = new HashSet<>();
    private static final String ENV = "env";
    private static final String ROLE_READ ="0000-GA-NORG_Skriv";

    @Mock
    private DiskresjonskodeAuthorisation diskresjonskodeAuthorisation;

    @Mock
    private EgenAnsattAuthorisation egenAnsattAuthorisation;

    @Mock
    private ReadAuthorisation readAuthorisation;

    @Mock
    private RolesService rolesService;

    @InjectMocks
    private DefaultReadSecurityStrategy defaultReadSecurityStrategy;

    @Before
    public void setup(){
        when(rolesService.getRolesForEnvironment(ENV, RolesService.RoleType.READ)).thenReturn(requiredRoles);
    }

    @Test
    public void isSupported() throws Exception {
        assertEquals(defaultReadSecurityStrategy.isSupported(readAuthorisation), true);
        assertEquals(defaultReadSecurityStrategy.isSupported(diskresjonskodeAuthorisation), false);
        assertEquals(defaultReadSecurityStrategy.isSupported(egenAnsattAuthorisation), false);
    }

    @Test(expected = HttpUnauthorisedException.class)
    public void authoriseThrowsUnauthorisedIfMissingRole(){
        requiredRoles.add(ROLE_READ);
        defaultReadSecurityStrategy.authorise(userRoles, ENV);
    }

    @Test
    public void authoriseDoesNotThrowExceptionWhenHaveRequiredRoles(){
        requiredRoles.add(ROLE_READ);
        userRoles.add(ROLE_READ);
        defaultReadSecurityStrategy.authorise(userRoles, ENV);
    }

    @Test(expected = HttpUnauthorisedException.class)
    public void authoriseThrowsAuthorisedIfHaveWrongRole(){
        requiredRoles.add(ROLE_READ);
        userRoles.add("role");
        defaultReadSecurityStrategy.authorise(userRoles, ENV);
    }

    @Test
    public void authoriseSuccessWhenNoRequiredRoles(){
        defaultReadSecurityStrategy.authorise(userRoles,ENV);
    }
}