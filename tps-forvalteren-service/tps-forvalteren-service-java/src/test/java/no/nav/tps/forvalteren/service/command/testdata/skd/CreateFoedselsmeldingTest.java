package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;

@RunWith(MockitoJUnitRunner.class)
public class CreateFoedselsmeldingTest {

    private static final Boolean ADD_HEADER = false;

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @InjectMocks
    private CreateFoedselsmeldinger createFoedselsmeldinger;

    private List<SkdMeldingTrans1> skdMeldinger;
    private List<Person> listeMedPersoner;

    @Before
    public void setup() {
        Person person = new Person();
        listeMedPersoner = new ArrayList<>();
        skdMeldinger = new ArrayList<>();

        listeMedPersoner.add(person);

        when(skdMessageCreatorTrans1.execute(anyString(), anyListOf(Person.class), anyBoolean())).thenReturn(skdMeldinger);
    }

    @Test
    public void execute() {
        List<SkdMeldingTrans1> result = createFoedselsmeldinger.executeFromPersons(listeMedPersoner, ADD_HEADER);

        verify(skdMessageCreatorTrans1).execute(anyString(), anyListOf(Person.class), anyBoolean());

        assertThat(result, is(skdMeldinger));
    }
}
