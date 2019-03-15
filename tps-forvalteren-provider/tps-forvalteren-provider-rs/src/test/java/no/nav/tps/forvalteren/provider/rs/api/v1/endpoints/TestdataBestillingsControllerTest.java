package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.dolly.RsIdenterMiljoer;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.dolly.ListExtractorKommaSeperated;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerByIdIn;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonerBestillingService;
import no.nav.tps.forvalteren.service.command.testdata.skd.LagreTilTpsService;

@RunWith(MockitoJUnitRunner.class)
public class TestdataBestillingsControllerTest {

    private static final String IDENT_1 = "11111111111";
    private static final String IDENT_2 = "22222222222";
    private static final String ENV_1 = "u5";
    private static final String ENV_2 = "u6";

    @Mock
    private PersonerBestillingService personerBestillingService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MapperFacade mapper;

    @Mock
    private LagreTilTpsService lagreTilTps;

    @Mock
    private ListExtractorKommaSeperated listExtractorKommaSeperated;

    @Mock
    private FindPersonerByIdIn findPersonerByIdIn;

    @InjectMocks
    private TestdataBestillingsController testdataBestillingsController;

    @Test
    public void createPersonerFraBestillingskriterierOk() {
        RsPersonBestillingKriteriumRequest bestilling = RsPersonBestillingKriteriumRequest.builder().build();
        when(personerBestillingService.createTpsfPersonFromRestRequest(bestilling))
                .thenReturn(newArrayList(
                        Person.builder().ident(IDENT_1).build(),
                        Person.builder().ident(IDENT_2).build()
                ));

        List<String> identer = testdataBestillingsController.createPersonerFraBestillingskriterier(bestilling);

        verify(personerBestillingService).createTpsfPersonFromRestRequest(bestilling);
        assertThat(identer, containsInAnyOrder(IDENT_1, IDENT_2));
    }

    @Test
    public void sendFlerePersonerTilTpsOk() {

        testdataBestillingsController.sendFlerePersonerTilTps( RsIdenterMiljoer.builder()
                .miljoer(newArrayList(ENV_1, ENV_2))
                .identer(newArrayList(IDENT_1, IDENT_2))
                .build());

        verify(findPersonerByIdIn).execute(anyList());
        verify(lagreTilTps).execute(anyList(), anySet());
    }

    @Test
    public void getPersonsOk() {

        testdataBestillingsController.getPersons(format("%s,%s", IDENT_1, IDENT_2));

        verify(listExtractorKommaSeperated).extractIdenter(anyString());
        verify(personRepository).findByIdentIn(anyList());
        verify(mapper).mapAsList(anyList(), eq(RsPerson.class));
    }

    @Test
    public void hentPersonerOk() {

        testdataBestillingsController.hentPersoner(newArrayList(IDENT_1, IDENT_2));

        verify(personRepository).findByIdentIn(anyList());
        verify(mapper).mapAsList(anyList(), eq(RsPerson.class));
    }
}