package no.nav.tps.forvalteren.service.command;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
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

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.dodsmeldinger.UpdateDeathRow;
import no.nav.tps.forvalteren.service.command.exceptions.DeathRowNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UpdateDeathRowTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private DeathRowRepository repositoryMock;

    @Mock
    private DeathRow deathRowMock;

    @InjectMocks
    private UpdateDeathRow command;

    @Before
    public void setUp() {
        when(deathRowMock.getId()).thenReturn(1L);
        when(repositoryMock.findById(1L)).thenReturn(deathRowMock);
        when(repositoryMock.save(deathRowMock)).thenReturn(deathRowMock);
    }

    @Test
    public void updateReturnsUpdatedRow() {
        DeathRow result = command.execute(deathRowMock);
        assertThat(result, is(sameInstance(deathRowMock)));
        verify(repositoryMock).findById(1L);
        verify(repositoryMock).save(deathRowMock);
    }

    @Test
    public void throwsExceptionIfDeathRowDoesNotExist() {
        when(repositoryMock.findById(1L)).thenReturn(null);

        expectedException.expect(DeathRowNotFoundException.class);

        command.execute(deathRowMock);
    }
}
