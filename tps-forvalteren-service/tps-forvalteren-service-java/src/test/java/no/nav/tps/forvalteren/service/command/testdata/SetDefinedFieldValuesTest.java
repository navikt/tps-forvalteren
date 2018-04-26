package no.nav.tps.forvalteren.service.command.testdata;

import java.time.LocalDateTime;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetDefinedFieldValues;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SetDefinedFieldValuesTest {

    @InjectMocks
    private SetDefinedFieldValues setDefinedFieldValues;

    @Mock
    private Person person;

    private Object fieldValue;

    @Before
    public void setup() {
        fieldValue = new String();
    }

    @Test
    public void setStatsborgerskapFraPersonMal() {
        person = new Person();

        setDefinedFieldValues.execute("statsborgerskap", "111", person);
        assertThat(person.getStatsborgerskap(), is("111"));

        setDefinedFieldValues.execute("statsborgerskap", fieldValue, person);
        assertThat(person.getStatsborgerskap(), is(nullValue()));

        setDefinedFieldValues.execute("statsborgerskap", "NOR", person);
        assertThat(person.getStatsborgerskap(), is(nullValue()));

        setDefinedFieldValues.execute("statsborgerskap", "1922", person);
        assertThat(person.getStatsborgerskap(), is(nullValue()));
    }

    @Test
    public void setSpesregFraPersonMal() {
        person = new Person();

        setDefinedFieldValues.execute("spesreg", fieldValue, person);
        assertThat(person.getSpesreg(), is(nullValue()));

        setDefinedFieldValues.execute("spesreg", "3", person);
        assertThat(person.getSpesreg(), is("3"));

        setDefinedFieldValues.execute("spesreg", "X", person);
        assertThat(person.getSpesreg(), is(nullValue()));

        setDefinedFieldValues.execute("spesreg", "1WE", person);
        assertThat(person.getSpesreg(), is(nullValue()));

        setDefinedFieldValues.execute("spesreg", "11", person);
        assertThat(person.getSpesreg(), is(nullValue()));
    }

    @Test
    public void setSpesregDatoFraPersonMal() {
        person = new Person();

        setDefinedFieldValues.execute("spesregDato", fieldValue, person);
        assertThat(person.getSpesregDato(), is(nullValue()));

        setDefinedFieldValues.execute("spesregDato", "20100101", person);
        assertThat(person.getSpesregDato(), instanceOf(LocalDateTime.class));

    }

    @Test
    public void setDoedsdatoFraPersonMal() {
        person = new Person();

        setDefinedFieldValues.execute("doedsdato", fieldValue, person);
        assertThat(person.getDoedsdato(), is(nullValue()));

        setDefinedFieldValues.execute("doedsdato", "19501010", person);
        assertThat(person.getDoedsdato(), instanceOf(LocalDateTime.class));

    }

    @Test
    public void setSivilstandFraPersonMal() {
        person = new Person();

        setDefinedFieldValues.execute("sivilstand", fieldValue, person);
        assertThat(person.getSivilstand(), is(nullValue()));

        setDefinedFieldValues.execute("sivilstand", "3", person);
        assertThat(person.getSivilstand(), is("3"));

        setDefinedFieldValues.execute("sivilstand", "X", person);
        assertThat(person.getSivilstand(), is(nullValue()));

        setDefinedFieldValues.execute("sivilstand", "1WE", person);
        assertThat(person.getSivilstand(), is(nullValue()));

        setDefinedFieldValues.execute("sivilstand", "11", person);
        assertThat(person.getSivilstand(), is(nullValue()));
    }

}
