package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;

@RunWith(MockitoJUnitRunner.class)
public class SkdEndringsmeldingsgruppeServiceTest {
    
    @InjectMocks
    private SkdEndringsmeldingsgruppeService skdEndringsmeldingsgruppeService;
    
    @Mock
    private SkdEndringsmeldingGruppeRepository repository;
    
    @Mock
    private SkdEndringsmeldingGruppe gruppe;
    
    @Test
    public void checkThatGruppeGetsSaved() {
        skdEndringsmeldingsgruppeService.save(gruppe);
        verify(repository).save(gruppe);
    }
    
    @Test
    public void checkThatGruppeGetsDeleted() {
        Long gruppeId = 1337L;
        
        skdEndringsmeldingsgruppeService.deleteGruppeById(gruppeId);
        
        verify(repository).deleteById(gruppeId);
    }
    
    @Test
    public void checkThatCorrectGruppeIsFound() {
        Long gruppeId = 1337L;
        when(repository.findById(gruppeId)).thenReturn(gruppe);
        
        SkdEndringsmeldingGruppe result = skdEndringsmeldingsgruppeService.findGruppeById(gruppeId);
        
        verify(repository).findById(gruppeId);
        assertThat(result, is(gruppe));
    }
    
}