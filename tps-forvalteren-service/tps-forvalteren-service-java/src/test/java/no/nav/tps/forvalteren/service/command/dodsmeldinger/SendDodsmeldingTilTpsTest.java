package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.FindDoedePersoner;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SendDodsmeldingTilTpsTest {

    @InjectMocks
    private SendDodsmeldingTilTps sendDodsmeldingTilTps;

    @Mock
    private FindAllDeathRowTasks findAllDeathRowTasks;

    @Mock
    private FindDoedePersoner findDoedePersoner;

    @Mock
    private UpdateDeathRow updateDeathRow;

    @Mock
    private SkdMeldingResolver innvandring;

    @Before
    public void setup() {
        DeathRow deathrow = Mockito.mock(DeathRow.class);
        deathrow.setId(123456L);
        deathrow.setDoedsdato(LocalDateTime.now());
        deathrow.setIdent("111111222222");
        deathrow.setHandling("C");

        List<DeathRow> listOfDeathRow = new ArrayList<DeathRow>();
        List<List<DeathRow>> listOfListOfDeathRows = new ArrayList<>();
        listOfDeathRow.add(deathrow);
        listOfListOfDeathRows.add(listOfDeathRow);


        when(findAllDeathRowTasks.execute()).thenReturn(listOfListOfDeathRows);
        //when(findDoedePersoner.execute(List<Person>)).thenReturn();
    }

    @Test
    public void sendDodsmeldingerTest () {
//        ArgumentCaptor<DeathRow> capture = ArgumentCaptor.forClass(DeathRow.class);
//
//        //when(updateDeathRow.execute(capture.capture()));
//        sendDodsmeldingTilTps.execute();
//
//        verify(updateDeathRow).execute(capture.capture());
//        assertThat(capture.getValue().getHandling(), is(equalTo("C")));

    }


}
