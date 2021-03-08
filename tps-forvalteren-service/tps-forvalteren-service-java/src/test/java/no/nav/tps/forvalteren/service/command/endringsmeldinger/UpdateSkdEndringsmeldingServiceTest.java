package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.message.MessageConstants.SKD_ENDRINGSMELDING_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UpdateSkdEndringsmeldingServiceTest {

    private static final Long MELDING_ID1 = 1L;
    private static final Long MELDING_ID2 = 2L;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private MessageProvider messageProvider;
    @Mock
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;
    @Mock
    private SaveSkdEndringsmeldingService saveSkdEndringsmeldingService;
    @InjectMocks
    private UpdateSkdEndringsmeldingService updateSkdEndringsmeldingService;
    private RsMeldingstype meldingType1 = Mockito.mock(RsMeldingstype1Felter.class);
    private RsMeldingstype meldingType2 = Mockito.mock(RsMeldingstype2Felter.class);
    @Mock
    private SkdEndringsmelding skdMelding1;
    @Mock
    private SkdEndringsmelding skdMelding2;
    private List<RsMeldingstype> meldinger = new ArrayList<>(Arrays.asList(meldingType1, meldingType2));

    @Before
    public void setup() {
        when(meldingType1.getId()).thenReturn(MELDING_ID1);
        when(meldingType2.getId()).thenReturn(MELDING_ID2);
        when(skdEndringsmeldingRepository.findById(MELDING_ID1)).thenReturn(Optional.of(skdMelding1));
        when(skdEndringsmeldingRepository.findById(MELDING_ID2)).thenReturn(Optional.of(skdMelding2));
        when(messageProvider.get(SKD_ENDRINGSMELDING_NOT_FOUND, MELDING_ID1)).thenReturn("not found");
    }

    @Test
    public void updateMeldingHappyPath() {
        updateSkdEndringsmeldingService.update(meldinger);

        verify(skdEndringsmeldingRepository).findById(MELDING_ID1);
        verify(skdEndringsmeldingRepository).findById(MELDING_ID2);

        verify(saveSkdEndringsmeldingService).save(meldinger.get(0), skdMelding1);
        verify(saveSkdEndringsmeldingService).save(meldinger.get(1), skdMelding2);
    }

    @Test
    public void skdEndringsmeldingThrowsExceptionWhenNotExists() {
        when(skdEndringsmeldingRepository.findById(any())).thenReturn(Optional.empty());

        expectedException.expect(SkdEndringsmeldingNotFoundException.class);
        expectedException.expectMessage("not found");

        updateSkdEndringsmeldingService.update(meldinger);

        verify(messageProvider).get(SKD_ENDRINGSMELDING_NOT_FOUND, MELDING_ID1);
        verify(messageProvider).get(SKD_ENDRINGSMELDING_NOT_FOUND, MELDING_ID2);

    }

}