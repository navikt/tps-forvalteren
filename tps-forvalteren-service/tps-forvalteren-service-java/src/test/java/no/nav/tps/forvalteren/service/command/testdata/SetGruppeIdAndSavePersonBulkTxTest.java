package no.nav.tps.forvalteren.service.command.testdata;

import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetGruppeIdOnPersons;

@RunWith(MockitoJUnitRunner.class)
public class SetGruppeIdAndSavePersonBulkTxTest {

    @Mock
    private SetGruppeIdOnPersons setGruppeIdOnPersons;

    @Mock
    private SavePersonBulk savePersonBulk;

    @InjectMocks
    private SetGruppeIdAndSavePersonBulkTx setGruppeIdAndSavePersonBulkTx;

    @Test
    public void verifyServices() {
        Long gruppeId = 1337L;
        List<Person> persons = Collections.emptyList();

        setGruppeIdAndSavePersonBulkTx.execute(persons, gruppeId);

        verify(setGruppeIdOnPersons).setGruppeId(persons, gruppeId);
        verify(savePersonBulk).execute(persons);
    }

}