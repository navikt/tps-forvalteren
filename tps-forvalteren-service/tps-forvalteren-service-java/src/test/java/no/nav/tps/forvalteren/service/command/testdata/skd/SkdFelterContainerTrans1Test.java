package no.nav.tps.forvalteren.service.command.testdata.skd;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SkdFelterContainerTrans1Test {

    @InjectMocks
    private SkdFelterContainerTrans1 skdFelterContainerTrans1;

    private List<SkdFeltDefinisjon> skdFeltDefinisjoner;

    @Before
    public void setup() {
        skdFeltDefinisjoner = skdFelterContainerTrans1.hentSkdFelter();
    }

    @Test
    public void callsMethodsThatAddSkdFelter() {
        final int SKD_MELDING_LENGTH = 1500;
        int bytesReserved = 0;
        for(SkdFeltDefinisjon felt : skdFeltDefinisjoner) {
            bytesReserved += felt.getAntallBytesAvsatt();
        }
        assertThat(bytesReserved, is(SKD_MELDING_LENGTH));
    }

    @Test
    public void listContainsAllSkdFeltDefinisjoner() {
        final int SKD_FELT_DEFINISJONER_SIZE = 135;
        assertThat(skdFeltDefinisjoner, hasSize(SKD_FELT_DEFINISJONER_SIZE));
    }

    @Test
    public void checkThatFieldLengthAndDefaultValueLengthIsEqual() {
        for(SkdFeltDefinisjon felt : skdFeltDefinisjoner) {
            assertThat(felt.getDefaultVerdi().length(), is(felt.getAntallBytesAvsatt()));
        }
    }

    @Test
    public void checkThatFieldsAreInAscOrder() {
        int currentOrder = 1;
        for(SkdFeltDefinisjon felt : skdFeltDefinisjoner) {
            assertThat(felt.getIdRekkefolge(), is(greaterThanOrEqualTo(currentOrder)));
            currentOrder++;
        }
    }

}