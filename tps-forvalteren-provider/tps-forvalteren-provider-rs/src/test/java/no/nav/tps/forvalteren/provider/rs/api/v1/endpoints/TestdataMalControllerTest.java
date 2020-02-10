package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Personmal;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonmalRepository;
import no.nav.tps.forvalteren.service.command.testdatamal.CreateTestdataPerson;

@RunWith(MockitoJUnitRunner.class)
public class TestdataMalControllerTest {

    private static final Long ID = 0L;
    private static final Long GRUPPE_ID = 0L;

    @Mock
    private PersonmalRepository personmalRepository;

    @Mock
    private MapperFacade mapper;

    @InjectMocks
    private TestdataMalController testdataMalController;

    @Mock
    private CreateTestdataPerson createTestdataPerson;

    @Test
    public void createTestdataMal() {
        RsPersonMal rsPersonMal = new RsPersonMal();

        when(mapper.map(rsPersonMal, Personmal.class)).thenReturn(new Personmal());

        testdataMalController.createTestdataMal(rsPersonMal);

        verify(personmalRepository).save(any());
    }

    @Test
    public void deleteTestdataMal() {
        testdataMalController.deleteTestdataMal(ID);

        verify(personmalRepository).deleteById(ID);
    }

    @Test
    public void getTestdataMal() {
        RsPersonMal rsPersonMal = new RsPersonMal();

        when(mapper.map(personmalRepository.findById(ID), RsPersonMal.class)).thenReturn(rsPersonMal);
        assertThat(testdataMalController.getTestdataMal(ID), is(rsPersonMal));
    }

    @Test
    public void getAllTestdataMal() {
        List<Personmal> personmalTest = new ArrayList<>();
        List<RsPersonMal> rsPersonMalTest = new ArrayList<>();

        testdataMalController.getAllTestdataMal();

        when(personmalRepository.findAll()).thenReturn(personmalTest);

        assertThat(testdataMalController.getAllTestdataMal(), is(rsPersonMalTest));
    }

    @Test
    public void createNewPersonsFromMal() {

        RsPersonMalRequest rsPersonMalRequest = new RsPersonMalRequest();

        testdataMalController.createNewPersonsFromMal(GRUPPE_ID, rsPersonMalRequest);

        verify(createTestdataPerson).execute(GRUPPE_ID, rsPersonMalRequest);
    }
}
