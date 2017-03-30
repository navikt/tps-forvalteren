package no.nav.tps.vedlikehold.service.command.authorisation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static java.util.Collections.singleton;
import static no.nav.tps.vedlikehold.service.command.authorisation.RolesService.RoleType.READ;
import static no.nav.tps.vedlikehold.service.command.authorisation.RolesService.RoleType.WRITE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@RunWith(MockitoJUnitRunner.class)
public class RolesServiceTest {

    private static final Set<String> readRolesU = singleton("readRoleU");
    private static final Set<String> readRolesT = singleton("readRoleT");
    private static final Set<String> readRolesQ = singleton("readRoleQ");
    private static final Set<String> readRolesP = singleton("readRoleP");
    private static final Set<String> readRolesO = singleton("readRoleO");

    private static final Set<String> writeRolesU = singleton("writeRoleU");
    private static final Set<String> writeRolesT = singleton("writeRoleT");
    private static final Set<String> writeRolesQ = singleton("writeRoleQ");
    private static final Set<String> writeRolesP = singleton("writeRoleP");
    private static final Set<String> writeRolesO = singleton("writeRoleO");

    private RolesService rolesService;

    @Before
    public void setUp() {
        rolesService = new RolesService();

        Whitebox.setInternalState(rolesService, "readRolesU", readRolesU);
        Whitebox.setInternalState(rolesService, "readRolesT", readRolesT);
        Whitebox.setInternalState(rolesService, "readRolesQ", readRolesQ);
        Whitebox.setInternalState(rolesService, "readRolesP", readRolesP);
        Whitebox.setInternalState(rolesService, "readRolesO", readRolesO);

        Whitebox.setInternalState(rolesService, "writeRolesU", writeRolesU);
        Whitebox.setInternalState(rolesService, "writeRolesT", writeRolesT);
        Whitebox.setInternalState(rolesService, "writeRolesQ", writeRolesQ);
        Whitebox.setInternalState(rolesService, "writeRolesP", writeRolesP);
        Whitebox.setInternalState(rolesService, "writeRolesO", writeRolesO);
    }

    @Test
    public void returnsCorrectRolesForEnvironmentAndType() {
        assertThat(rolesService.getRolesForEnvironment("u1", READ), is(readRolesU));
        assertThat(rolesService.getRolesForEnvironment("t3", READ), is(readRolesT));
        assertThat(rolesService.getRolesForEnvironment("q5", READ), is(readRolesQ));
        assertThat(rolesService.getRolesForEnvironment("p", READ), is(readRolesP));
        assertThat(rolesService.getRolesForEnvironment("o4", READ), is(readRolesO));

        assertThat(rolesService.getRolesForEnvironment("u1", WRITE), is(writeRolesU));
        assertThat(rolesService.getRolesForEnvironment("t3", WRITE), is(writeRolesT));
        assertThat(rolesService.getRolesForEnvironment("q5", WRITE), is(writeRolesQ));
        assertThat(rolesService.getRolesForEnvironment("p", WRITE), is(writeRolesP));
        assertThat(rolesService.getRolesForEnvironment("o4", WRITE), is(writeRolesO));
    }

    @Test
    public void returnsEmptySetForUnknownEnvironments() {
        assertThat(rolesService.getRolesForEnvironment("!!", READ), hasSize(0));

        assertThat(rolesService.getRolesForEnvironment("!!", WRITE), hasSize(0));
    }

    @Test
    public void returnsEmptySetForUndefinedEnvironments() {
        assertThat(rolesService.getRolesForEnvironment("", READ), hasSize(0));
        assertThat(rolesService.getRolesForEnvironment(null, READ), hasSize(0));

        assertThat(rolesService.getRolesForEnvironment("", WRITE), hasSize(0));
        assertThat(rolesService.getRolesForEnvironment(null, WRITE), hasSize(0));
    }
}
