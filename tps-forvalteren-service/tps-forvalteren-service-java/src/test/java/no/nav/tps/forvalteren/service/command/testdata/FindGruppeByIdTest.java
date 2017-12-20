package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.GRUPPE_NOT_FOUND_KEY;
import static no.nav.tps.forvalteren.domain.test.provider.GruppeProvider.aGruppe;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.service.command.exceptions.GruppeNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class FindGruppeByIdTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private MessageProvider messageProvider;

    @Mock
    private GruppeRepository gruppeRepository;

    @InjectMocks
    private FindGruppeById findGruppeById;

    private static final Long GRUPPE_ID = 1337L;
    private static final Long INVALID_GRUPPE_ID = 0L;

    @Before
    public void setup() {
        when(gruppeRepository.findById(GRUPPE_ID)).thenReturn(aGruppe().build());
        when(gruppeRepository.findById(INVALID_GRUPPE_ID)).thenReturn(null);
    }

    @Test
    public void checkThatRepositoryGetsCalled() {
        findGruppeById.execute(GRUPPE_ID);

        verify(gruppeRepository).findById(GRUPPE_ID);
    }

    @Test
    public void checkThatExceptionGetsThrowWhenGruppeDoesNotExist() {
        when(messageProvider.get(GRUPPE_NOT_FOUND_KEY, INVALID_GRUPPE_ID)).thenReturn("exception");

        expectedException.expect(GruppeNotFoundException.class);
        expectedException.expectMessage("exception");

        findGruppeById.execute(INVALID_GRUPPE_ID);

        verify(gruppeRepository).findById(INVALID_GRUPPE_ID);
        verify(messageProvider).get(GRUPPE_NOT_FOUND_KEY, INVALID_GRUPPE_ID);
    }

}