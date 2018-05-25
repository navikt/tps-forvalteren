package no.nav.tps.forvalteren.service.command.testdata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FinnPersonerForNavEndringsmeldingTest {

    @InjectMocks
    private FinnPersonerForNavEndringsmelding finnPersonerForNavEndringsmelding;

    List<Person> listeMedTestPersoner;
    Person person1;
    Person person2;
    Person person3;
    Person person4;
    Person person5;

    @Before
    public void setup(){
        person1 = new Person();
        person2 = new Person();
        person3 = new Person();

        person1.setTypeSikkerhetsTiltak("ABCD");
        person2.setEgenAnsattDatoFom(LocalDateTime.now());
        person3.setFornavn("Tom");

    }

    @Test
    public void finnAllePersonerSomSkalHaNavMelding(){

        listeMedTestPersoner = new ArrayList<>();

        listeMedTestPersoner.add(person1);
        listeMedTestPersoner.add(person2);
        listeMedTestPersoner.add(person3);

        assertThat(finnPersonerForNavEndringsmelding.execute(listeMedTestPersoner).size(), is(2));
    }

}
