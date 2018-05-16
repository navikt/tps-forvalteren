package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans2;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdInputParamsToSkdMeldingInnhold;

@Service
public class ConvertMeldingFromJsonToText {
    
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SkdFelterContainerTrans1 skdFelterContainerTrans1;

    @Autowired
    private SkdFelterContainerTrans2 skdFelterContainerTrans2;

    @Autowired
    private SkdInputParamsToSkdMeldingInnhold skdInputParamsToSkdMeldingInnhold;

    public String execute(RsMeldingstype melding) {
        Map valuesMap = mapper.convertValue(melding, new TypeReference<HashMap<String, String>>() {});
        if (melding instanceof RsMeldingstype1Felter) {
            return skdInputParamsToSkdMeldingInnhold.execute(valuesMap, skdFelterContainerTrans1).toString(); //TODO Map from RSMeldingstype to SkdMeldingstype, then toString().
        } else {
            return skdInputParamsToSkdMeldingInnhold.execute(valuesMap, skdFelterContainerTrans2).toString();
        }
    }

}
