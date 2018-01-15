package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersCreatorService;

@Service
public class GenerateSkdMelding {

    @Autowired
    private SkdParametersCreatorService skdParametersCreatorService;

    @Autowired
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;
    
    public String execute(SkdFelterContainer skdFelterContainer, TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, Person person, boolean addHeader) {
        Map<String, String> skdParametere = skdParametersCreatorService.execute(skdRequestMeldingDefinition, person);
        return skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParametere, skdFelterContainer, addHeader);
    }
}