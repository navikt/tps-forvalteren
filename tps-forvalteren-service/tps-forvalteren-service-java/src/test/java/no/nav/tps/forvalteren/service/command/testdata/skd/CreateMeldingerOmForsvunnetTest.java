package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.time.LocalDateTime.now;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmForsvunnetAarsakskode82.MELDING_OM_FORSVUNNET;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;

@RunWith(MockitoJUnitRunner.class)
public class CreateMeldingerOmForsvunnetTest {

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @InjectMocks
    private CreateMeldingerOmForsvunnet createMeldingerOmForsvunnet;

    @Test
    public void filterForsvunnet() {

        createMeldingerOmForsvunnet.filterForsvunnet(Collections.singletonList(Person.builder().forsvunnetDato(now()).build()), true);

        verify(skdMessageCreatorTrans1).execute(eq(MELDING_OM_FORSVUNNET), anyList(), anyBoolean());
    }
}