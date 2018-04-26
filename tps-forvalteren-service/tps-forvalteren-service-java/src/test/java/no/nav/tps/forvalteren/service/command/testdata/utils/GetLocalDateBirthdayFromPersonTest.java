package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.time.LocalDate;

import no.nav.tps.forvalteren.domain.jpa.Person;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetLocalDateBirthdayFromPersonTest {

    @InjectMocks
    private GetLocalDateBirthdayFromPerson getLocalDateBirthdayFromPerson;

    @Test
    public void execute() {
        Person person = new Person();
        person.setIdent("05066012345");

        getLocalDateBirthdayFromPerson.execute(person);

        assertThat(getLocalDateBirthdayFromPerson.execute(person), instanceOf(LocalDate.class));
        assertThat(getLocalDateBirthdayFromPerson.execute(person).getYear(), is(2060));
        assertThat(getLocalDateBirthdayFromPerson.execute(person).getMonthValue(), is(6));
        assertThat(getLocalDateBirthdayFromPerson.execute(person).getDayOfMonth(), is(5));
    }
}
