package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FinnBarnTilForeldre;

@RunWith(MockitoJUnitRunner.class)
public class DivideBarnIntoTransRecordsTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private DivideBarnIntoTransRecords divideBarnIntoTransRecords;

    @Mock
    private SkdMessageSenderTrans2 skdMessageSenderTrans2;

    @Mock
    private FinnBarnTilForeldre finnBarnTilForeldre;

    @Mock
    private List<Person> barn = new ArrayList<>();

    private Person foreldre = aMalePerson().build();
    private List<String> environments = new ArrayList<>(Arrays.asList("u5", "u6"));

    @Before
    public void setup() {
        when(finnBarnTilForeldre.execute(foreldre)).thenReturn(barn);
    }

    @Test
    public void checkThatExceptionGetsThrown() {
        when(barn.size()).thenReturn(27);

        exception.expect(IllegalArgumentException.class);

        divideBarnIntoTransRecords.execute(foreldre, environments);
    }

    @Test
    public void checkThatOneMessageIsSent() {
        when(barn.size()).thenReturn(13);

        divideBarnIntoTransRecords.execute(foreldre, environments);

        verify(finnBarnTilForeldre).execute(foreldre);
        verify(skdMessageSenderTrans2).execute("Familieendring", foreldre, barn, environments);
    }

    @Test
    public void checkThatTwoMessagesGetsSent() {
        List<Person> barnRecord1 = new ArrayList<>();
        List<Person> barnRecord2 = new ArrayList<>();

        when(barn.size()).thenReturn(26);
        when(barn.subList(0, 13)).thenReturn(barnRecord1);
        when(barn.subList(13, 26)).thenReturn(barnRecord2);

        divideBarnIntoTransRecords.execute(foreldre, environments);

        verify(finnBarnTilForeldre).execute(foreldre);
        verify(skdMessageSenderTrans2, atLeastOnce()).execute("Familieendring", foreldre, barnRecord1, environments);
        verify(skdMessageSenderTrans2, atLeastOnce()).execute("Familieendring", foreldre, barnRecord2, environments);
    }

}