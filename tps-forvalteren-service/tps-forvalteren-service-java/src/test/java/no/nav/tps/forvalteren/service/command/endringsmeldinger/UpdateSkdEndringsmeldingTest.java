package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_JSON_PROCESSING;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_NOT_FOUND;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingJsonProcessingException;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UpdateSkdEndringsmeldingTest {

    @InjectMocks
    private UpdateSkdEndringsmelding updateSkdEndringsmelding;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private MessageProvider messageProvider;

    private RsMeldingstype meldingType1 = Mockito.mock(RsMeldingstype1Felter.class);

    private RsMeldingstype meldingType2 = Mockito.mock(RsMeldingstype2Felter.class);

    @Mock
    private SkdEndringsmelding skdMelding1;

    @Mock
    private SkdEndringsmelding skdMelding2;

    private static final Long MELDING_ID1 = 1L;
    private static final Long MELDING_ID2 = 2L;
    private List<RsMeldingstype> meldinger = new ArrayList<>(Arrays.asList(meldingType1, meldingType2));

    @Before
    public void setup() throws JsonProcessingException {
        when(meldingType1.getId()).thenReturn(MELDING_ID1);
        when(meldingType2.getId()).thenReturn(MELDING_ID2);
        when(skdEndringsmeldingRepository.findById(MELDING_ID1)).thenReturn(skdMelding1);
        when(skdEndringsmeldingRepository.findById(MELDING_ID2)).thenReturn(skdMelding2);
        when(messageProvider.get(SKD_ENDRINGSMELDING_JSON_PROCESSING, MELDING_ID1)).thenReturn("json");
        when(messageProvider.get(SKD_ENDRINGSMELDING_JSON_PROCESSING, MELDING_ID2)).thenReturn("json");
        when(messageProvider.get(SKD_ENDRINGSMELDING_NOT_FOUND, MELDING_ID1)).thenReturn("not found");
        when(messageProvider.get(SKD_ENDRINGSMELDING_NOT_FOUND, MELDING_ID2)).thenReturn("not found");
    }

    @Test
    public void updateMeldingHappyPath() throws JsonProcessingException {
        updateSkdEndringsmelding.execute(meldinger);

        verify(skdEndringsmeldingRepository).findById(MELDING_ID1);
        verify(skdEndringsmeldingRepository).findById(MELDING_ID2);

        verify(mapper).writeValueAsString(meldingType1);
        verify(mapper).writeValueAsString(meldingType2);

        verify(skdEndringsmeldingRepository).save(skdMelding1);
        verify(skdEndringsmeldingRepository).save(skdMelding2);
    }

    @Test
    public void checkThatJsonExceptionGetsThrown() throws JsonProcessingException {
        doThrow(JsonProcessingException.class).when(mapper).writeValueAsString(meldingType1);

        expectedException.expect(SkdEndringsmeldingJsonProcessingException.class);
        expectedException.expectMessage("json");

        updateSkdEndringsmelding.execute(meldinger);

        verify(messageProvider).get(SKD_ENDRINGSMELDING_JSON_PROCESSING, MELDING_ID1);
        verify(messageProvider).get(SKD_ENDRINGSMELDING_JSON_PROCESSING, MELDING_ID2);
    }

    @Test
    public void skdEndringsmeldingThrowsExceptionWhenNotExists() {
        when(skdEndringsmeldingRepository.findById(any())).thenReturn(null);

        expectedException.expect(SkdEndringsmeldingNotFoundException.class);
        expectedException.expectMessage("not found");

        updateSkdEndringsmelding.execute(meldinger);

        verify(messageProvider).get(SKD_ENDRINGSMELDING_NOT_FOUND, MELDING_ID1);
        verify(messageProvider).get(SKD_ENDRINGSMELDING_NOT_FOUND, MELDING_ID2);

    }

}