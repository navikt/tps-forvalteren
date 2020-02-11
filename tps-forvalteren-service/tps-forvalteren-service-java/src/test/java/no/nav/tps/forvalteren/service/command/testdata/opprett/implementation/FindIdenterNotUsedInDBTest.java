package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.opprett.FindIdenterNotUsedInDB;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonService;

@RunWith(MockitoJUnitRunner.class)
public class FindIdenterNotUsedInDBTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;
    
    private static final String IDENT_AVAILABLE = "42";
    private static final String IDENT_IN_DB = "1337";
    private Set<String> identer = new HashSet<>(Arrays.asList(IDENT_AVAILABLE, IDENT_IN_DB));
    private List<Person> persistedPersons = Arrays.asList(aMalePerson().ident(IDENT_IN_DB).build());
    
    @Before
    public void setup() {
        when(personService.getPersonerByIdenter(anyList())).thenReturn(persistedPersons);
    }
    
    @Test
    public void asd() {
        Set<String> result = findIdenterNotUsedInDB.filtrer(identer);
        
        verify(personService).getPersonerByIdenter(anyList());
        
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(IDENT_AVAILABLE));
    }
    
}