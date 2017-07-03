package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdInputParamsToSkdMeldingInnhold;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdOpprettSkdMeldingMedHeaderOgInnhold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultSkdOpprettSkdMeldingMedHeaderOgInnhold implements SkdOpprettSkdMeldingMedHeaderOgInnhold {

    @Autowired
    private SkdInputParamsToSkdMeldingInnhold skdInputParamsToSkdMeldingInnhold;

    @Autowired
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;

    @Override
    public String execute(Map<String, String> skdParameters) {
        StringBuilder skdMelding = skdInputParamsToSkdMeldingInnhold.execute(skdParameters);
        StringBuilder skdMeldingMedHeader = skdAddHeaderToSkdMelding.execute(skdMelding);
        return skdMeldingMedHeader.toString();
    }
}
