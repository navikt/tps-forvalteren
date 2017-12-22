package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;

@RunWith(MockitoJUnitRunner.class)
public class SaveSkdEndringsmeldingGruppeTest {

    @InjectMocks
    private SaveSkdEndringsmeldingGruppe saveSkdEndringsmeldingGruppe;

    @Mock
    private SkdEndringsmeldingGruppeRepository repository;

    @Mock
    private SkdEndringsmeldingGruppe gruppe;

    @Test
    public void checkThatGruppeGetsSaved() {
        saveSkdEndringsmeldingGruppe.execute(gruppe);
        verify(repository).save(gruppe);
    }

}