package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertTrue;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.UTVANDRET;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret;
import no.nav.tps.forvalteren.domain.jpa.Person;

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

        person.setInnvandretUtvandret(singletonList(buildUtvandretLand("777")));
        person2.setInnvandretUtvandret(singletonList(buildUtvandretLand("888")));
        person3.setInnvandretUtvandret(singletonList(buildUtvandretLand("999")));

        listeMedPersoner = new ArrayList();
        listeMedPersoner.addAll(Arrays.asList(person, person2, person3, person4));

        skdmelding = new SkdMeldingTrans1();

        captor = ArgumentCaptor.forClass(List.class);

        when(skdMessageCreatorTrans1.execute(anyString(), anyList(), anyBoolean())).thenReturn(Arrays.asList(skdmelding));
    }

    @Test
    public void execute() {
        createUtvandring.execute(listeMedPersoner, false);

        verify(skdMessageCreatorTrans1).execute(anyString(), captor.capture(), anyBoolean());

        assertTrue(captor.getValue().containsAll(Arrays.asList(person, person2, person3)));
    }

    private static InnvandretUtvandret buildUtvandretLand(String landkode) {
        return InnvandretUtvandret.builder()
                .innutvandret(UTVANDRET)
                .landkode(landkode)
                .build();
    }
}
