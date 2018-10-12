package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
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

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsNewSkdEndringsmelding;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingGruppeNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CreateSkdEndringsmeldingFromTypeTest {
    
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
    private SaveSkdEndringsmelding saveSkdEndringsmelding;
    @InjectMocks
    private CreateSkdEndringsmeldingFromType createSkdEndringsmeldingFromType;
    @Mock
    private RsNewSkdEndringsmelding rsNewSkdEndringsmelding;
    @Mock
    private SkdEndringsmeldingGruppe gruppe;
    @Mock
    private RsMeldingstype rsMeldingstype;
    
    @Before
    public void setup() {
        when(rsMeldingstype.getId()).thenReturn(MELDING_ID);
        when(skdEndringsmeldingGruppeRepository.findById(GRUPPE_ID)).thenReturn(gruppe);
        when(rsNewSkdEndringsmelding.getMeldingstype()).thenReturn(MELDINGSTYPE);
        when(rsNewSkdEndringsmelding.getNavn()).thenReturn(MELDING_NAVN);
        when(GetRsMeldingstypeFromTypeText.execute(rsNewSkdEndringsmelding.getMeldingstype())).thenReturn(rsMeldingstype);
    }
    
    @Test
    public void checkThatMeldingGetsSaved() {
        createSkdEndringsmeldingFromType.execute(GRUPPE_ID, rsNewSkdEndringsmelding);
        
        verify(GetRsMeldingstypeFromTypeText).execute(MELDINGSTYPE);
        verify(saveSkdEndringsmelding).save(any(RsMeldingstype.class), any(SkdEndringsmelding.class));
    }
    
    @Test
    public void throwsSkdEndringsmeldingGruppeNotFoundException() {
        when(skdEndringsmeldingGruppeRepository.findById(anyLong())).thenReturn(null);
        
        expectedException.expect(SkdEndringsmeldingGruppeNotFoundException.class);
        
        createSkdEndringsmeldingFromType.execute(GRUPPE_ID, rsNewSkdEndringsmelding);
        
        verify(messageProvider).get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, GRUPPE_ID);
    }
    
}