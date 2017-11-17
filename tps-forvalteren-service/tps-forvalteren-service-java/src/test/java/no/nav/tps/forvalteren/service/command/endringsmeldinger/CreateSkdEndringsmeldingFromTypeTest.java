package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_JSON_PROCESSING;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsNewSkdEndringsmelding;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingGruppeNotFoundException;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingJsonProcessingException;

@RunWith(MockitoJUnitRunner.class)
public class CreateSkdEndringsmeldingFromTypeTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private MessageProvider messageProvider;

    @Mock
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Mock
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private GetRsMeldingstypeFromTypeText GetRsMeldingstypeFromTypeText;

    @InjectMocks
    private CreateSkdEndringsmeldingFromType createSkdEndringsmeldingFromType;

    @Mock
    private RsNewSkdEndringsmelding rsNewSkdEndringsmelding;

    @Mock
    private SkdEndringsmeldingGruppe gruppe;

    @Mock
    private RsMeldingstype rsMeldingstype;

    private static final Long GRUPPE_ID = 1337L;
    private static final Long MELDING_ID = 42L;
    private static final String MELDINGSTYPE = "t1";
    private static final String MELDING_NAVN = "navn";

    @Before
    public void setup() throws JsonProcessingException {
        when(rsMeldingstype.getId()).thenReturn(MELDING_ID);
        when(skdEndringsmeldingGruppeRepository.findById(GRUPPE_ID)).thenReturn(gruppe);
        when(rsNewSkdEndringsmelding.getMeldingstype()).thenReturn(MELDINGSTYPE);
        when(rsNewSkdEndringsmelding.getNavn()).thenReturn(MELDING_NAVN);
        when(GetRsMeldingstypeFromTypeText.execute(rsNewSkdEndringsmelding.getMeldingstype())).thenReturn(rsMeldingstype);
    }

    @Test
    public void checkThatMeldingGetsSaved() throws JsonProcessingException {
        createSkdEndringsmeldingFromType.execute(GRUPPE_ID, rsNewSkdEndringsmelding);

        verify(GetRsMeldingstypeFromTypeText).execute(MELDINGSTYPE);
        verify(mapper).writeValueAsString(rsMeldingstype);
        verify(skdEndringsmeldingRepository).save(any(SkdEndringsmelding.class));
    }

    @Test
    public void throwsSkdEndringsmeldingGruppeNotFoundException() {
        when(skdEndringsmeldingGruppeRepository.findById(anyLong())).thenReturn(null);

        expectedException.expect(SkdEndringsmeldingGruppeNotFoundException.class);

        createSkdEndringsmeldingFromType.execute(GRUPPE_ID, rsNewSkdEndringsmelding);

        verify(messageProvider).get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, GRUPPE_ID);
    }

    @Test
    public void throwsSkdEndringsmeldingJsonProcessingException() throws JsonProcessingException {
        doThrow(SkdEndringsmeldingJsonProcessingException.class).when(mapper).writeValueAsString(rsMeldingstype);

        expectedException.expect(SkdEndringsmeldingJsonProcessingException.class);

        createSkdEndringsmeldingFromType.execute(GRUPPE_ID, rsNewSkdEndringsmelding);

        verify(messageProvider).get(SKD_ENDRINGSMELDING_JSON_PROCESSING, MELDING_ID);
    }

}