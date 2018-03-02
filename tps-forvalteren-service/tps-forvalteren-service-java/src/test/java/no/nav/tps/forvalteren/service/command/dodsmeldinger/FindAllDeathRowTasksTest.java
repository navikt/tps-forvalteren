package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FindAllDeathRowTasksTest {
    private String handlingReturn = "C";
    private String statusReturn = "";

    @Mock
    private DeathRow deathRowMock;

    @Mock
    private DeathRowRepository deathRowRepositoryMock;

    @InjectMocks
    private FindAllDeathRowTasks findAllDeathRowTasks;

    @Before
    public void setUp() {

        when(deathRowRepositoryMock.findAllByHandling("C")).thenReturn(Arrays.asList(deathRowMock));
        when(deathRowRepositoryMock.findAllByHandling("D")).thenReturn(Arrays.asList(deathRowMock));
        when(deathRowRepositoryMock.findAllByHandling("U")).thenReturn(Arrays.asList(deathRowMock));
        when(deathRowMock.getHandling()).thenReturn(handlingReturn);
        when(deathRowMock.getStatus()).thenReturn(statusReturn);
    }

    @Test
    public void findAndReturnAllTasks() {
        List<List<DeathRow>> test = findAllDeathRowTasks.execute();

        verify(deathRowRepositoryMock).findAllByHandling("C");
        verify(deathRowRepositoryMock).findAllByHandling("D");
        verify(deathRowRepositoryMock).findAllByHandling("U");

        assertThat(test, everyItem(instanceOf(ArrayList.class)));
    }

}
