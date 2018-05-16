package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.nav.tps.forvalteren.service.command.testdata.opprett.FindIdenterNotUsedInDB;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerByIdIn;

@RunWith(MockitoJUnitRunner.class)
public class FindIdenterNotUsedInDBTest {

    @Mock
    private FindPersonerByIdIn findPersonerByIdIn;

    @InjectMocks
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;
    
    private static final String IDENT_AVAILABLE = "42";
    private static final String IDENT_IN_DB = "1337";
    private Set<String> identer = new HashSet<>(Arrays.asList(IDENT_AVAILABLE, IDENT_IN_DB));
    private List<Person> persistedPersons = Arrays.asList(aMalePerson().ident(IDENT_IN_DB).build());
    
    @Before
    public void setup() {
        when(findPersonerByIdIn.execute(anyListOf(String.class))).thenReturn(persistedPersons);
    }
    
    @Test
    public void asd() {
        Set<String> result = findIdenterNotUsedInDB.filtrer(identer);
        
        verify(findPersonerByIdIn).execute(anyListOf(String.class));
        
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(IDENT_AVAILABLE));
    }
    
}