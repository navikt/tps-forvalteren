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
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;

@RunWith(MockitoJUnitRunner.class)
public class CreateVergemaalTest {

    @Mock
    private VergemaalRepository vergemaalRepository;

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
        Vergemaal vergemaal = new Vergemaal();
        Person person = new Person();
        person.setIdent(ident);

        listeSkdMeldinger = new ArrayList<>();
        listeMedVergemaal = new ArrayList<>();
        listeMedPersoner = new ArrayList<>();

        listeMedVergemaal.add(vergemaal);
        listeMedPersoner.add(person);

        when(vergemaalRepository.findAllByIdent(anyString())).thenReturn(listeMedVergemaal);
        when(skdMessageCreatorTrans1.createVergemaalSkdMelding(anyList(), anyBoolean())).thenReturn(listeSkdMeldinger);
    }

    @Test
    public void execute() {
        List<SkdMeldingTrans1> result = createVergemaal.execute(listeMedPersoner, false);

        verify(vergemaalRepository).findAllByIdent(anyString());
        verify(skdMessageCreatorTrans1).createVergemaalSkdMelding(anyList(), anyBoolean());
        assertThat(result, is(listeSkdMeldinger));

    }
}
