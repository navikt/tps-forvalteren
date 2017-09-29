package no.nav.tps.forvalteren.service.command.testdata.skd;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@RunWith(MockitoJUnitRunner.class)
public class SkdFelterContainerTrans2Test {

    @InjectMocks
    private SkdFelterContainerTrans2 skdFelterContainerTrans2;

    private List<SkdFeltDefinisjon> skdFeltDefinisjoner;

    @Before
    public void setup() {
        skdFeltDefinisjoner = skdFelterContainerTrans2.hentSkdFelter();
    }

    @Test
    public void listContainsAllSkdFeltDefinisjoner() {
        final int SKD_FELT_DEFINISJONER_SIZE = 61;
        assertThat(skdFeltDefinisjoner, hasSize(SKD_FELT_DEFINISJONER_SIZE));
    }

    @Test
    public void firstFieldIsAtCorrectPosition() {
        assertThat(skdFeltDefinisjoner.get(0).getFraByte(), is(1));
        assertThat(skdFeltDefinisjoner.get(0).getTilByte(), is(11));
    }

    @Test
    public void lastBarnKjoennIsAtCorrectPosition() {
        final int LAST_FIXED_POSITION = 867;
        assertThat(skdFeltDefinisjoner.get(57).getFraByte(), is(LAST_FIXED_POSITION));
        assertThat(skdFeltDefinisjoner.get(57).getTilByte(), is(LAST_FIXED_POSITION));
    }

    @Test
    public void lastFieldIsAtCorrectPosition() {
        assertThat(skdFeltDefinisjoner.get(60).getFraByte(), is(918));
        assertThat(skdFeltDefinisjoner.get(60).getTilByte(), is(1500));
    }

    @Test
    public void checkThatFieldLengthAndDefaultValueLengthIsEqual() {
        for(SkdFeltDefinisjon felt : skdFeltDefinisjoner) {
            assertThat(felt.getDefaultVerdi().length(), is(felt.getAntallBytesAvsatt()));
        }
    }

    @Test
    public void checkThatFieldsAreInAscOrder() {
        int order = 1;
        for(SkdFeltDefinisjon felt : skdFeltDefinisjoner) {
            assertThat(felt.getIdRekkefolge(), is(order));
            order++;
        }
    }

}