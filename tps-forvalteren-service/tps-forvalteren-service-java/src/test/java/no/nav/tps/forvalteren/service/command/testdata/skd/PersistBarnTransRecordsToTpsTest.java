package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aFemalePerson;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.stream.IntStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

@RunWith(MockitoJUnitRunner.class)
public class PersistBarnTransRecordsToTpsTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private PersistBarnTransRecordsToTps persistBarnTransRecordsToTps;

    @Mock
    private SkdMessageCreatorTrans2 skdMessageCreatorTrans2;

    private static final boolean ADD_HEADER = true;
    private Person forelder = aMalePerson().build();
    private Person barn = aFemalePerson().build();

    @Test
    public void checkThatExceptionGetsThrown() {
        IntStream.range(0, 28).forEach(i -> forelder.getRelasjoner().add(
                Relasjon.builder()
                        .person(forelder)
                        .personRelasjonMed(barn)
                        .relasjonTypeNavn("BARN")
                        .build()));

        exception.expect(IllegalArgumentException.class);

        persistBarnTransRecordsToTps.execute(forelder, ADD_HEADER);
    }

    @Test
    public void checkThatOneMessageIsSent() {

        IntStream.range(0, 13).forEach(i -> forelder.getRelasjoner().add(
                Relasjon.builder()
                        .person(forelder)
                        .personRelasjonMed(barn)
                        .relasjonTypeNavn("BARN")
                        .build()));

        persistBarnTransRecordsToTps.execute(forelder, ADD_HEADER);

        verify(skdMessageCreatorTrans2).execute(eq("Familieendring"), eq(forelder), anyList(), eq(ADD_HEADER));
    }
}