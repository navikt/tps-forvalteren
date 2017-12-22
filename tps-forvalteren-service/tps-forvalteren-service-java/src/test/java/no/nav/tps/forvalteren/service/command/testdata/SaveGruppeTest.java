package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.domain.test.provider.GruppeProvider.aGruppe;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;

@RunWith(MockitoJUnitRunner.class)
public class SaveGruppeTest {

    @Mock
    private GruppeRepository gruppeRepository;

    @InjectMocks
    private SaveGruppe saveGruppe;

    private Gruppe gruppe = aGruppe().build();

    @Test
    public void checkThatRepostioryGetsCalled() {
        saveGruppe.execute(gruppe);

        verify(gruppeRepository).save(gruppe);
    }

}