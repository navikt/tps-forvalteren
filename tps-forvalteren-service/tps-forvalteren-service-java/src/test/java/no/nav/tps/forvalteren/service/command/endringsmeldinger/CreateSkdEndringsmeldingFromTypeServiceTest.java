package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.message.MessageConstants.SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsNewSkdEndringsmelding;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingGruppeNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CreateSkdEndringsmeldingFromTypeServiceTest {
    
    private static final Long GRUPPE_ID = 1337L;
    private static final Long MELDING_ID = 42L;
    private static final String MELDINGSTYPE = "t1";
    private static final String MELDING_NAVN = "navn";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private MessageProvider messageProvider;
    @Mock
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;
    @Mock
    private GetRsMeldingstypeFromTypeText GetRsMeldingstypeFromTypeText;
    @Mock
    private SaveSkdEndringsmeldingService saveSkdEndringsmeldingService;
    @InjectMocks
    private CreateSkdEndringsmeldingFromTypeService createSkdEndringsmeldingFromTypeService;
    @Mock
    private RsNewSkdEndringsmelding rsNewSkdEndringsmelding;
    @Mock
    private SkdEndringsmeldingGruppe gruppe;
    @Mock
    private RsMeldingstype rsMeldingstype;
    
    @Before
    public void setup() {
        when(skdEndringsmeldingGruppeRepository.findById(GRUPPE_ID)).thenReturn(gruppe);
        when(rsNewSkdEndringsmelding.getMeldingstype()).thenReturn(MELDINGSTYPE);
        when(rsNewSkdEndringsmelding.getNavn()).thenReturn(MELDING_NAVN);
        when(GetRsMeldingstypeFromTypeText.execute(rsNewSkdEndringsmelding.getMeldingstype())).thenReturn(rsMeldingstype);
    }
    
    @Test
    public void checkThatMeldingGetsSaved() {
        createSkdEndringsmeldingFromTypeService.execute(GRUPPE_ID, rsNewSkdEndringsmelding);
        
        verify(GetRsMeldingstypeFromTypeText).execute(MELDINGSTYPE);
        verify(saveSkdEndringsmeldingService).save(any(RsMeldingstype.class), any(SkdEndringsmelding.class));
    }
    
    @Test
    public void throwsSkdEndringsmeldingGruppeNotFoundException() {
        when(skdEndringsmeldingGruppeRepository.findById(anyLong())).thenReturn(null);
        
        expectedException.expect(SkdEndringsmeldingGruppeNotFoundException.class);
        
        createSkdEndringsmeldingFromTypeService.execute(GRUPPE_ID, rsNewSkdEndringsmelding);
        
        verify(messageProvider).get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, GRUPPE_ID);
    }
    
}