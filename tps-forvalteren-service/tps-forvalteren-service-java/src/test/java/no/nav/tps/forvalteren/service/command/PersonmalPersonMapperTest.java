package no.nav.tps.forvalteren.service.command;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetDefinedFieldValues;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetRandomFieldValues;
import no.nav.tps.forvalteren.service.command.testdatamal.PersonmalPersonMapper;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PersonmalPersonMapperTest {

    @InjectMocks
    private PersonmalPersonMapper personmalPersonMapper;

    @Mock
    private SetDefinedFieldValues setDefinedFieldValues;

    @Mock
    private SetRandomFieldValues setRandomFieldValues;

    @Mock
    private RsPersonMal inputPersonMal;

    @Before
    public void setup() {

    }

    @Test
    public void execute() {
        Person person = Mockito.mock(Person.class);

        inputPersonMal = new RsPersonMal();
        assertThat(personmalPersonMapper.execute(inputPersonMal, person), instanceOf(Person.class));
    }

    @Test
    public void checkThatSetRandomIsCalled() {
        Person person = new Person();
        Person person2 = new Person();
        RsPersonMal inputPersonMal = new RsPersonMal();
        String fieldName = "sivilstand";

        inputPersonMal.setSivilstand("*");
        person2.setSivilstand("1");

        when(setRandomFieldValues.execute(fieldName, person)).thenReturn(person2);
        Person p = personmalPersonMapper.execute(inputPersonMal, person);
        assertTrue(p.getSivilstand().matches("\\d{1}"));
        verify(setRandomFieldValues).execute(fieldName, person);
    }

    @Test
    public void checkThatSetDefinedValueIsCalled() {
        Person person = new Person();
        Person person2 = new Person();
        RsPersonMal inputPersonMal = new RsPersonMal();
        Object stringObject = new String("4");

        inputPersonMal.setSivilstand("4");
        person2.setSivilstand("4");

        when(setDefinedFieldValues.execute("sivilstand", stringObject, person)).thenReturn(person2);
        Person result = personmalPersonMapper.execute(inputPersonMal, person);

        assertTrue(result.getSivilstand().matches("\\d{1}"));
        verify(setDefinedFieldValues).execute("sivilstand", stringObject, person);

    }

}
