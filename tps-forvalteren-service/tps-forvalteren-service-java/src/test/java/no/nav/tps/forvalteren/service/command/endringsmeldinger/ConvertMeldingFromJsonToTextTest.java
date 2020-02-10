package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans2;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdInputParamsToSkdMeldingInnhold;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.utils.MapBetweenRsMeldingstypeAndSkdMelding;

@RunWith(MockitoJUnitRunner.class)
public class ConvertMeldingFromJsonToTextTest {
    
    @Mock
    private ObjectMapper mapper;

    @Mock
    private MapBetweenRsMeldingstypeAndSkdMelding mapBetweenRsMeldingstypeAndSkdMelding;

    @Mock
    private SkdFelterContainerTrans2 skdFelterContainerTrans2;

    @Mock
    private SkdInputParamsToSkdMeldingInnhold skdInputParamsToSkdMeldingInnhold;

    @InjectMocks
    private ConvertMeldingFromJsonToText convertMeldingFromJsonToText;
    
    @Before
    public void setup() {
        when(mapBetweenRsMeldingstypeAndSkdMelding.mapReverse(any())).thenReturn(new SkdMeldingTrans1());
        when(skdInputParamsToSkdMeldingInnhold.execute(anyMap(), any(SkdFelterContainerTrans2.class))).thenReturn(new StringBuilder("meldingstype2"));
    }
    
    @Test
    public void verifyCorrectServiceForRsMeldingstype1Felter() {
        RsMeldingstype melding = new RsMeldingstype1Felter();
        
        convertMeldingFromJsonToText.execute(melding);
        
        verify(mapBetweenRsMeldingstypeAndSkdMelding).mapReverse(any(RsMeldingstype1Felter.class));
    }

    @Test
    public void verifyCorrectServiceForRsMeldingstype2Felter() {

        RsMeldingstype melding = new RsMeldingstype2Felter();
        when(mapper.convertValue(any(RsMeldingstype.class), any(TypeReference.class))).thenReturn(new HashMap());

        convertMeldingFromJsonToText.execute(melding);

        verify(skdInputParamsToSkdMeldingInnhold).execute(anyMap(), any(SkdFelterContainerTrans2.class));
    }
    
}