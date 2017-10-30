package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@RunWith(MockitoJUnitRunner.class)
public class DeleteSkdEndringsmeldingByIdInTest {

    @InjectMocks
    private DeleteSkdEndringsmeldingByIdIn deleteSkdEndringsmeldingByIdIn;

    @Mock
    private SkdEndringsmeldingRepository repository;

    @Mock
    private List<Long> skdEndringsmeldingIds;

    @Test
    public void verifyRepositoryCall() {
        deleteSkdEndringsmeldingByIdIn.execute(skdEndringsmeldingIds);
        verify(repository).deleteByIdIn(skdEndringsmeldingIds);
    }

}