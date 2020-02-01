package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FinnBarnTilForelder;

@RunWith(MockitoJUnitRunner.class)
public class PersistBarnTransRecordsToTpsTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private PersistBarnTransRecordsToTps persistBarnTransRecordsToTps;

    @Mock
    private SkdMessageCreatorTrans2 skdMessageCreatorTrans2;

    @Mock
    private FinnBarnTilForelder finnBarnTilForelder;

    @Mock
    private List<Person> barn = new ArrayList<>();

    private static final boolean ADD_HEADER = true;
    private Person forelder = aMalePerson().build();

    @Before
    public void setup() {
        when(finnBarnTilForelder.execute(forelder)).thenReturn(barn);
    }

    @Test
    public void checkThatExceptionGetsThrown() {
        when(barn.size()).thenReturn(27);

        exception.expect(IllegalArgumentException.class);

        persistBarnTransRecordsToTps.execute(forelder, ADD_HEADER);
    }

    @Test
    public void checkThatOneMessageIsSent() {
        when(barn.size()).thenReturn(13);

        persistBarnTransRecordsToTps.execute(forelder, ADD_HEADER);

        verify(finnBarnTilForelder).execute(forelder);
        verify(skdMessageCreatorTrans2).execute("Familieendring", forelder, barn, ADD_HEADER);
    }

    @Test
    public void checkThatTwoMessagesGetsSent() {
        List<Person> barnRecord1 = new ArrayList<>();
        List<Person> barnRecord2 = new ArrayList<>();

        when(barn.size()).thenReturn(26);
        when(barn.subList(0, 13)).thenReturn(barnRecord1);
        when(barn.subList(13, 26)).thenReturn(barnRecord2);

        persistBarnTransRecordsToTps.execute(forelder, ADD_HEADER);

        verify(finnBarnTilForelder).execute(forelder);
        verify(skdMessageCreatorTrans2, atLeastOnce()).execute("Familieendring", forelder, barnRecord1, ADD_HEADER);
        verify(skdMessageCreatorTrans2, atLeastOnce()).execute("Familieendring", forelder, barnRecord2, ADD_HEADER);
    }

}