package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;

@RunWith(MockitoJUnitRunner.class)
public class CreateVergemaalTest {

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @InjectMocks
    private CreateVergemaal createVergemaal;

    private List<SkdMeldingTrans1> listeSkdMeldinger;
    private List<Person> listeMedPersoner;
    private List<Vergemaal> listeMedVergemaal;

    private String ident = "01234598765";

    @Before
    public void setup() {
        Vergemaal vergemaal = Vergemaal.builder().build();
        Person person = new Person();
        person.setIdent(ident);

        listeSkdMeldinger = new ArrayList<>();
        listeMedVergemaal = new ArrayList<>();
        listeMedPersoner = new ArrayList<>();

        listeMedVergemaal.add(vergemaal);
        listeMedPersoner.add(person);

        when(skdMessageCreatorTrans1.execute(anyString(), anyList(), anyBoolean())).thenReturn(listeSkdMeldinger);
    }

    @Test
    public void execute() {
        List<SkdMeldingTrans1> result = createVergemaal.execute(listeMedPersoner, false);

        verify(skdMessageCreatorTrans1).execute(anyString(), anyList(), anyBoolean());
        assertThat(result, is(listeSkdMeldinger));

    }
}
