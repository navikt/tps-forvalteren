package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;

@RunWith(MockitoJUnitRunner.class)
public class DeleteSkdEndringsmeldingGruppeByIdTest {

    @InjectMocks
    private DeleteSkdEndringsmeldingGruppeById deleteSkdEndringsmeldingGruppeById;

    @Mock
    private SkdEndringsmeldingGruppeRepository repository;

    @Test
    public void checkThatGruppeGetsDeleted() {
        Long gruppeId = 1337L;

        deleteSkdEndringsmeldingGruppeById.execute(gruppeId);

        verify(repository).deleteById(gruppeId);
    }
}