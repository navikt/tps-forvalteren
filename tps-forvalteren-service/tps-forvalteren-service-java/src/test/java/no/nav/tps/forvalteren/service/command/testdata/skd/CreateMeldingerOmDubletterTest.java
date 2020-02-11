package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Collections.singletonList;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmDubletter.MELDING_OM_DUBLETTER;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;
import no.nav.tps.forvalteren.domain.jpa.Person;

@RunWith(MockitoJUnitRunner.class)
public class CreateMeldingerOmDubletterTest {

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @InjectMocks
    private CreateMeldingerOmDubletter createMeldingerOmDubletter;

    @Test
    public void createDubletterOK() {

        createMeldingerOmDubletter.filterDubletter(singletonList(Person.builder()
                .identHistorikk(singletonList(IdentHistorikk.builder()
                        .historicIdentOrder(1)
                        .aliasPerson(Person.builder().build())
                        .build()))
                .build()), true);

        verify(skdMessageCreatorTrans1).execute(eq(MELDING_OM_DUBLETTER), anyList(), anyBoolean());
    }
}