package no.nav.tps.forvalteren.service.command.testdata;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

@RunWith(MockitoJUnitRunner.class)
public class FindPersonerByIdInTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private FindPersonerByIdIn findPersonerByIdIn;

    @Test
    public void checkThatRepostioryGetsCalled() {
        List<String> identer = new ArrayList<>();

        findPersonerByIdIn.execute(identer);

        verify(personRepository).findByIdentIn(identer);
    }

}