package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.domain.test.provider.GruppeProvider.aGruppe;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

@RunWith(MockitoJUnitRunner.class)
public class DeleteGruppeByIdTest {

    @Mock
    private GruppeRepository gruppeRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private DeleteRelasjonerByIdIn deleteRelasjonerByIdIn;

    @Mock
    private DoedsmeldingRepository doedsmeldingRepository;
    
    @InjectMocks
    private DeleteGruppeById deleteGruppeById;

    private static final Long GRUPPE_ID = 1337L;

    private Gruppe gruppe = aGruppe().personer(Arrays.asList(aMalePerson().build())).build();

    @Before
    public void setup() {
        when(gruppeRepository.findById(GRUPPE_ID)).thenReturn(gruppe);
    }

    @Test
    public void verifyAllServices() {
        deleteGruppeById.execute(GRUPPE_ID);

        verify(gruppeRepository).findById(GRUPPE_ID);
        verify(deleteRelasjonerByIdIn).execute((List<Long>) argThat(hasItem(gruppe.getPersoner().get(0).getId())));
        verify(personRepository).deleteByGruppeId(GRUPPE_ID);
        verify(gruppeRepository).deleteById(GRUPPE_ID);
        verify(doedsmeldingRepository).deleteByPersonIdIn((List<Long>) argThat(hasItem(gruppe.getPersoner().get(0).getId())));
    }

}