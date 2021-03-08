package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.message.MessageConstants.SKD_ENDRINGSMELDING_JSON_PROCESSING;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingJsonProcessingException;

@RunWith(MockitoJUnitRunner.class)
public class SaveSkdEndringsmeldingServiceTest {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Mock
    private MessageProvider messageProvider;
    
    @Mock
    private ObjectMapper mapper;
    
    @Mock
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;
    
    @InjectMocks
    private SaveSkdEndringsmeldingService saveSkdEndringsmeldingService;
    
    @Mock
    private RsMeldingstype melding;
    
    @Mock
    private SkdEndringsmelding skdEndringsmelding;
    
    @Test
    public void checkThatServicesGetsCalled() throws JsonProcessingException {
        saveSkdEndringsmeldingService.save(melding, skdEndringsmelding);
        
        verify(mapper).writeValueAsString(melding);
        verify(skdEndringsmeldingRepository).save(skdEndringsmelding);
    }
    
    @Test
    public void throwsSkdEndringsmeldingJsonProcessingException() throws JsonProcessingException {
        doThrow(SkdEndringsmeldingJsonProcessingException.class).when(mapper).writeValueAsString(melding);
        
        expectedException.expect(SkdEndringsmeldingJsonProcessingException.class);
        
        saveSkdEndringsmeldingService.save(melding, skdEndringsmelding);
        
        verify(messageProvider).get(SKD_ENDRINGSMELDING_JSON_PROCESSING, melding.getId());
    }
    
}