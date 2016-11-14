package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisation;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisation;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.ReadAuthorisation;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.WriteAuthorisation;
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
        when(rolesService.getRolesForEnvironment(ENV, RolesService.RoleType.WRITE)).thenReturn(rolesRequired);
    }

    @Test
    public void isSupported() {
        assertEquals(defaultWriteSecurityStrategy.isSupported(writeAuthorisation), true);
        assertEquals(defaultWriteSecurityStrategy.isSupported(diskresjonskodeAuthorisation), false);
        assertEquals(defaultWriteSecurityStrategy.isSupported(egenAnsattAuthorisation), false);
        assertEquals(defaultWriteSecurityStrategy.isSupported(readAuthorisation), false);
    }

    @Test(expected = HttpUnauthorisedException.class)
    public void authoriseThrowsUnauthorsiedWriteRolesIsMissing() {
        rolesRequired.add(WRITE_ROLE);
        defaultWriteSecurityStrategy.authorise(userRoles, ENV);
    }

    @Test
    public void authoriseAcceptedWhenHaveWriteRoleForEnvironment() {
        rolesRequired.add(WRITE_ROLE);
        userRoles.add(WRITE_ROLE);
        defaultWriteSecurityStrategy.authorise(userRoles, ENV);
    }

    @Test(expected = HttpUnauthorisedException.class)
    public void authorsieThrowsUnauthorisedWhenUserDontHaveAnyRolesEnvironment(){
        rolesRequired.add(WRITE_ROLE);
        defaultWriteSecurityStrategy.authorise(userRoles, ENV);
    }
}