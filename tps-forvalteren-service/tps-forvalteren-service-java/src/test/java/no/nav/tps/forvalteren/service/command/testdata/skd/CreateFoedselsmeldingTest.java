package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerSomSkalHaFoedselsmelding;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreateFoedselsmeldingTest {

    private static final Boolean ADD_HEADER = false;

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Mock
    private FindPersonerSomSkalHaFoedselsmelding findPersonerSomSkalHaFoedselsmelding;

    @InjectMocks
    private CreateFoedselsmeldinger createFoedselsmeldinger;

    private List<SkdMeldingTrans1> skdMeldinger;
    private List<Person> listeMedPersoner;


    @Before
    public void setup(){
        Person person = new Person();
        listeMedPersoner = new ArrayList<>();
        skdMeldinger = new ArrayList<>();

        listeMedPersoner.add(person);

        when(findPersonerSomSkalHaFoedselsmelding.execute(anyListOf(Person.class))).thenReturn(listeMedPersoner);
        when(skdMessageCreatorTrans1.execute(anyString(), anyListOf(Person.class),anyBoolean())).thenReturn(skdMeldinger);
    }

    @Test
    public void execute(){
        List<SkdMeldingTrans1> result = createFoedselsmeldinger.execute(listeMedPersoner, ADD_HEADER);

        verify(findPersonerSomSkalHaFoedselsmelding).execute(anyListOf(Person.class));
        verify(skdMessageCreatorTrans1).execute(anyString(), anyListOf(Person.class), anyBoolean());

        assertThat(result, is(skdMeldinger));



    }
}
