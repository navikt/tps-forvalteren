package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aFemalePerson;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapContaining.hasKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;

@RunWith(MockitoJUnitRunner.class)
public class BarnetranseSkdParameterStrategyTest {

    @InjectMocks
    private BarnetranseSkdParameterStrategy barnetranseSkdParameterStrategy;

    private Person forelder = aMalePerson().build();
    private List<Person> barn = new ArrayList<>();
    private Map<String, String> skdParams;

    @Before
    public void setup() {
        barn.add(aFemalePerson().build());
        barn.add(aMalePerson().build());
        skdParams = barnetranseSkdParameterStrategy.execute(forelder, barn);
    }

    @Test
    public void checkThatDefaultFieldsAreAdded() {
        assertThat(skdParams, hasKey("maskindato"));
        assertThat(skdParams, hasKey("maskintid"));
        assertThat(skdParams, hasEntry("transtype", "2"));
        assertThat(skdParams, hasEntry("aarsakskode", "98"));
    }

    @Test
    public void checkThatForelderIsAdded() {
        assertThat(skdParams, hasEntry("fodselsnr", forelder.getIdent()));
    }

    @Test
    public void checkThatBarnAreAdded() {
        for (int counter = 0; counter < barn.size(); counter++) {
            String fodsdatoKey = "barnFodsdato" + counter;
            String fodsdatoValue = barn.get(counter).getIdent().substring(0, 6);

            String persnrKey = "barnPersnr" + counter;
            String persnrValue = barn.get(counter).getIdent().substring(6);

            assertThat(skdParams, hasEntry(fodsdatoKey, fodsdatoValue));
            assertThat(skdParams, hasEntry(persnrKey, persnrValue));
        }
    }

}