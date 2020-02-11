package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans2;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdInputParamsToSkdMeldingInnhold;
import no.nav.tps.forvalteren.service.command.testdata.utils.MapBetweenRsMeldingstypeAndSkdMelding;

/**
 * @deprecated (Mai 2018, Refaktoreringsarbeid: Bytt ut ConvertMeldingFromJsonToText med MapBetweenRsMeldingstypeAndSkdMelding når SkdFelterContainerTrans2 er byttet ut med SkdMeldingTrans2 slik SkdmeldingTrans1 ble byttet ut.
 * Da kan MapBetweenRsMeldingstypeAndSkdMelding utføre map av vilkårlig RsMeldingstype1Felter og RsMeldingstype2Felter, hvilket gjør denne klassen overflødig. )
 */
@Service
@Deprecated
public class ConvertMeldingFromJsonToText {
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private MapBetweenRsMeldingstypeAndSkdMelding mapBetweenRsMeldingstypeAndSkdMelding;
    
    @Autowired
    private SkdFelterContainerTrans2 skdFelterContainerTrans2;
    
    @Autowired
    private SkdInputParamsToSkdMeldingInnhold skdInputParamsToSkdMeldingInnhold;
    
    public String execute(RsMeldingstype melding) {
        Map valuesMap = mapper.convertValue(melding, new TypeReference<HashMap<String, String>>() {
        });
        if (melding instanceof RsMeldingstype1Felter) {
            return mapBetweenRsMeldingstypeAndSkdMelding.mapReverse(((RsMeldingstype1Felter) melding)).toString();
        } else {
            return skdInputParamsToSkdMeldingInnhold.execute(valuesMap, skdFelterContainerTrans2).toString(); //TODO gjenta samme endring for SkdFelterContainerTrans2 som har blitt gjort for SkdFelterContainer1
        }
    }
    
}
