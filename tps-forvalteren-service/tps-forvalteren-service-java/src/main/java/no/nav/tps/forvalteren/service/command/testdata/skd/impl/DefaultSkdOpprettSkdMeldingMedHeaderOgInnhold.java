package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdInputParamsToSkdMeldingInnhold;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdOpprettSkdMeldingMedHeaderOgInnhold;

@Service
public class DefaultSkdOpprettSkdMeldingMedHeaderOgInnhold implements SkdOpprettSkdMeldingMedHeaderOgInnhold {

    @Autowired
    private SkdInputParamsToSkdMeldingInnhold skdInputParamsToSkdMeldingInnhold;

    @Autowired
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;

    @Override
    public String execute(Map<String, String> skdParameters, SkdFelterContainer skdFelterContainer, boolean addHeader) {
        StringBuilder skdMelding = skdInputParamsToSkdMeldingInnhold.execute(skdParameters, skdFelterContainer);
        if (addHeader) {
            StringBuilder skdMeldingMedHeader = skdAddHeaderToSkdMelding.execute(skdMelding);
            return skdMeldingMedHeader.toString();
        }
        return skdMelding.toString();
    }
}
