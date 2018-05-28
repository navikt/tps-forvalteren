package no.nav.tps.forvalteren.service.command.testdata;

import java.util.ArrayList;
import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FIndPersonerSomSkalHaFoedselsmeldingTest {

    @Mock
    private RelasjonRepository relasjonRepository;

    @InjectMocks
    private FindPersonerSomSkalHaFoedselsmelding findPersonerSomSkalHaFoedselsmelding;

    private Person testPerson;
    private List<Relasjon> relasjonListResult;
    private List<Person> relasjonPersonList;
    private List<Person> resultRelasjonPersonList;

    @Before
    public void setup() {
        Person testPerson = new Person();
        Person testRelasjonPerson1 = new Person();
        Person testRelasjonPerson2 = new Person();
        Person testRelasjonPerson3 = new Person();
        Person testRelasjonPerson4 = new Person();

        Relasjon relasjon1 = new Relasjon(0004L, testRelasjonPerson1, testRelasjonPerson2, "FOEDSEL");
        Relasjon relasjon2 = new Relasjon(0005L, testRelasjonPerson3, testRelasjonPerson4, "FOEDSEL");

        relasjonListResult = new ArrayList<>();
        relasjonListResult.add(relasjon1);
        relasjonListResult.add(relasjon2);

        relasjonPersonList = new ArrayList<>();
        relasjonPersonList.add(testRelasjonPerson1);
        relasjonPersonList.add(testRelasjonPerson2);
        relasjonPersonList.add(testRelasjonPerson3);
        relasjonPersonList.add(testRelasjonPerson4);

        resultRelasjonPersonList = new ArrayList<>();
        resultRelasjonPersonList.add(testRelasjonPerson2);
        resultRelasjonPersonList.add(testRelasjonPerson4);
    }

    @Test
    public void execute() {

        System.out.println(relasjonListResult);

        when(relasjonRepository.findByPersonAndRelasjonTypeNavn(any(Person.class), eq("FOEDSEL"))).thenReturn(relasjonListResult);

        List<Person> resultat = findPersonerSomSkalHaFoedselsmelding.execute(relasjonPersonList);

        assertTrue(resultat.containsAll(resultRelasjonPersonList));
    }
}
