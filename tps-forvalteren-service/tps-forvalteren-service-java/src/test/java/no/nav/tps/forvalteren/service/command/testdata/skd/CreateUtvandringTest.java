package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import no.nav.tps.forvalteren.domain.jpa.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreateUtvandringTest {

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @InjectMocks
    private CreateUtvandring createUtvandring;

    private Person person;
    private Person person2;
    private Person person3;
    private Person person4;

    private List<Person> listeMedPersoner;
    private SkdMeldingTrans1 skdmelding;
    private ArgumentCaptor<List> captor;

    @Before
    public void setup() {
        person = new Person();
        person2 = new Person();
        person3 = new Person();
        person4 = new Person();

        person.setUtvandretTilLand("777");
        person2.setUtvandretTilLand("888");
        person3.setUtvandretTilLand("999");

        listeMedPersoner = new ArrayList();
        listeMedPersoner.addAll(Arrays.asList(person, person2, person3, person4));

        skdmelding = new SkdMeldingTrans1();

        captor = ArgumentCaptor.forClass(List.class);

        when(skdMessageCreatorTrans1.execute(anyString(), anyListOf(Person.class), anyBoolean())).thenReturn(Arrays.asList(skdmelding));
    }

    @Test
    public void execute() {
        createUtvandring.execute(listeMedPersoner, false);

        verify(skdMessageCreatorTrans1).execute(anyString(), captor.capture(), anyBoolean());

        assertTrue(captor.getValue().containsAll(Arrays.asList(person, person2, person3)));
    }
}
