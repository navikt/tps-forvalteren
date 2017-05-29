package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DeletePersonsByIdServiceTest {

    @Mock
    private PersonRepository repositoryMock;

    @InjectMocks
    private DeletePersonsByIdService command;

    private static final Long[] ids = {1L, 2L, 3L};

    @Test
    public void callsPersonRepository() {
        command.execute(ids);
        verify(repositoryMock).deleteByIdIn(ids);
    }

}