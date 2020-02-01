package no.nav.tps.forvalteren.service.command.testdata;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;

@RunWith(MockitoJUnitRunner.class)
public class FindAlleGrupperOrderByIdAscTest {

    @Mock
    private GruppeRepository gruppeRepository;

    @InjectMocks
    private FindAlleGrupperOrderByIdAsc findAlleGrupperOrderByIdAsc;

    @Test
    public void checkThatRepositoryGetsCalled() {
        findAlleGrupperOrderByIdAsc.execute();

        verify(gruppeRepository).findAllByOrderByIdAsc();
    }

}