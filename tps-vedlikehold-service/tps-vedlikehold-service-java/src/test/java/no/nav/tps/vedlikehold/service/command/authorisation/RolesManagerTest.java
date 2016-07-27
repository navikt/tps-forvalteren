package no.nav.tps.vedlikehold.service.command.authorisation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static java.util.Collections.singleton;
import static no.nav.tps.vedlikehold.service.command.authorisation.RolesManager.RoleType.READ;
import static no.nav.tps.vedlikehold.service.command.authorisation.RolesManager.RoleType.WRITE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class RolesManagerTest {

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

    private RolesManager rolesManager;

    @Before
    public void setUp() {
        rolesManager = new RolesManager();

        Whitebox.setInternalState(rolesManager, "readRolesU", readRolesU);
        Whitebox.setInternalState(rolesManager, "readRolesT", readRolesT);
        Whitebox.setInternalState(rolesManager, "readRolesQ", readRolesQ);
        Whitebox.setInternalState(rolesManager, "readRolesP", readRolesP);
        Whitebox.setInternalState(rolesManager, "readRolesO", readRolesO);

        Whitebox.setInternalState(rolesManager, "writeRolesU", writeRolesU);
        Whitebox.setInternalState(rolesManager, "writeRolesT", writeRolesT);
        Whitebox.setInternalState(rolesManager, "writeRolesQ", writeRolesQ);
        Whitebox.setInternalState(rolesManager, "writeRolesP", writeRolesP);
        Whitebox.setInternalState(rolesManager, "writeRolesO", writeRolesO);
    }

    @Test
    public void returnsCorrectRolesForEnvironmentAndType() {
        assertThat(rolesManager.getRolesForEnvironment("u1", READ), is(readRolesU));
        assertThat(rolesManager.getRolesForEnvironment("t3", READ), is(readRolesT));
        assertThat(rolesManager.getRolesForEnvironment("q5", READ), is(readRolesQ));
        assertThat(rolesManager.getRolesForEnvironment("p", READ), is(readRolesP));
        assertThat(rolesManager.getRolesForEnvironment("o4", READ), is(readRolesO));

        assertThat(rolesManager.getRolesForEnvironment("u1", WRITE), is(writeRolesU));
        assertThat(rolesManager.getRolesForEnvironment("t3", WRITE), is(writeRolesT));
        assertThat(rolesManager.getRolesForEnvironment("q5", WRITE), is(writeRolesQ));
        assertThat(rolesManager.getRolesForEnvironment("p", WRITE), is(writeRolesP));
        assertThat(rolesManager.getRolesForEnvironment("o4", WRITE), is(writeRolesO));
    }

    @Test
    public void returnsEmptySetForUnknownEnvironments() {
        assertThat(rolesManager.getRolesForEnvironment("!!", READ), hasSize(0));

        assertThat(rolesManager.getRolesForEnvironment("!!", WRITE), hasSize(0));
    }

    @Test
    public void returnsEmptySetForUndefinedEnvironments() {
        assertThat(rolesManager.getRolesForEnvironment("", READ), hasSize(0));
        assertThat(rolesManager.getRolesForEnvironment(null, READ), hasSize(0));

        assertThat(rolesManager.getRolesForEnvironment("", WRITE), hasSize(0));
        assertThat(rolesManager.getRolesForEnvironment(null, WRITE), hasSize(0));
    }
}
