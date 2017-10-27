package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aGruppe;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;

@RunWith(MockitoJUnitRunner.class)
public class FindAllSkdEndringsmeldingGrupperTest {

    @InjectMocks
    private FindAllSkdEndringsmeldingGrupper findAllSkdEndringsmeldingGrupper;

    @Mock
    private SkdEndringsmeldingGruppeRepository repository;

    @Test
    public void checkThatFindAllGrupperReturnsAllGrupper() {
        List<SkdEndringsmeldingGruppe> grupper = new ArrayList<>();
        grupper.add(aGruppe().build());
        grupper.add(aGruppe().build());

        when(repository.findAllByOrderByIdAsc()).thenReturn(grupper);
        List<SkdEndringsmeldingGruppe> result = findAllSkdEndringsmeldingGrupper.execute();

        verify(repository).findAllByOrderByIdAsc();
        assertThat(result, hasSize(2));
    }

}