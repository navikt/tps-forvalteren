package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.domain.service.command.User.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceRoutineControllerTest {

    @Mock
    private GetTpsServiceRutinerService serviceMock;

    @Mock
    private UserContextHolder userContextHolderMock;


    @InjectMocks
    private ServiceRoutineController controller;

    @Test
    public void returnsResultFromService() {
        TpsServiceRoutineDefinition routine = mock(TpsServiceRoutineDefinition.class);

        List<TpsServiceRoutineDefinition> routines = Collections.singletonList(routine);
        when(serviceMock.execute()).thenReturn(routines);

        User user = new User("name", "username", Sets.newSet("rolle"));
        when(userContextHolderMock.getUser()).thenReturn(user);

        List<TpsServiceRoutineDefinition> result = controller.getTpsServiceRutiner();

        assertThat(result, hasItem(routine));
    }
}