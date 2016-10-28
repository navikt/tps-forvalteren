package no.nav.tps.vedlikehold.service.command.tps.endringsmeldinger;

import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.TpsEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.resolvers.EndringsmeldingResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by f148888 on 27.10.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultGetTpsEndringsmeldingServiceTest {

    @Mock
    private TpsEndringsmelding routineMock;

    @Mock
    private EndringsmeldingResolver resolverMock;

    @Spy
    private List<EndringsmeldingResolver> resolvers = new ArrayList<>();

    @InjectMocks
    private DefaultGetTpsEndringsmeldingService command;

    @Before
    public void before() {
        when(resolverMock.resolve()).thenReturn(routineMock);
        resolvers.clear();
        resolvers.add(resolverMock);
    }

    @Test
    public void returnsRoutines() {
        List<TpsEndringsmelding> routines = command.execute();

        assertThat(routines, contains(routineMock));
    }
}
