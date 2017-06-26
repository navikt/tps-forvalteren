package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdInputParamsToSkdMeldingInnhold;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdOpprettSkdMeldingMedHeaderOginnhold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultSkdOpprettSkdMeldingMedHeaderOgInnhold implements SkdOpprettSkdMeldingMedHeaderOginnhold {

    @Autowired
    private SkdInputParamsToSkdMeldingInnhold skdInputParamsToSkdMeldingInnhold;

    @Autowired
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;

    @Override
    public String execute(Map<String, String> skdInputMap) {
        StringBuilder skdMelding = skdInputParamsToSkdMeldingInnhold.execute(skdInputMap);
        skdAddHeaderToSkdMelding.execute(skdMelding);
        return skdMelding.toString();
    }
}
