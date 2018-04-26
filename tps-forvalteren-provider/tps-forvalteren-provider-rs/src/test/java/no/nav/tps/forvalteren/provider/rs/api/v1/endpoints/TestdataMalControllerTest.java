package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Personmal;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.repository.jpa.PersonmalRepository;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestdataMalControllerTest {

    @Mock
    private PersonmalRepository personmalRepository;

    @Mock
    private MapperFacade mapper;

    @InjectMocks
    private TestdataMalController testdataMalController;

    private Long id = 0L;

    @Before
    public void before() {

    }

    @Test
    public void createTestdataMal() {
        RsPersonMal rsPersonMal = new RsPersonMal();

        when(mapper.map(rsPersonMal, Personmal.class)).thenReturn(new Personmal());

        testdataMalController.createTestdataMal(rsPersonMal);

        verify(personmalRepository).save(any());
    }


    @Test
    public void deleteTestdataMal() {
        testdataMalController.deleteTestdataMal(id);

        verify(personmalRepository).deleteById(id);
    }

    @Test
    public void getTestdataMal() {
        RsPersonMal rsPersonMal = new RsPersonMal();

        when(mapper.map(personmalRepository.findById(id), RsPersonMal.class)).thenReturn(rsPersonMal);
        assertThat(testdataMalController.getTestdataMal(id), is(rsPersonMal));

    }

    @Test
    public void getAllTestdataMal() {
        List<Personmal> personmalTest = new ArrayList<>();
        List<RsPersonMal> rsPersonMalTest = new ArrayList<>();

        testdataMalController.getAllTestdataMal();

        when(personmalRepository.findAll()).thenReturn(personmalTest);

        assertThat(testdataMalController.getAllTestdataMal(), is(rsPersonMalTest));

    }

}
