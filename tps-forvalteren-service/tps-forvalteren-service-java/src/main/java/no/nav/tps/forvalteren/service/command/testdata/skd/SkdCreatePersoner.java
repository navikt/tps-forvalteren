package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SkdCreatePersoner {

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private SkdParametersCreatorService skdParametersCreatorService;

    @Autowired
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Autowired
    private GetSkdMeldingByName getSkdMeldingByName;

    @Autowired
    private SkdFelterContainerTrans1 skdFelterContainerTrans1;

    public void execute(String skdMeldingNavn, List<Person> personer, List<String> environments) {
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = getSkdMeldingByName.execute(skdMeldingNavn);

        if (skdRequestMeldingDefinitionOptional.isPresent()) {
            TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = skdRequestMeldingDefinitionOptional.get();
            for (Person person : personer) {
                Map<String, String> skdParametere = skdParametersCreatorService.execute(skdRequestMeldingDefinition, person);
                String skdMelding = skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParametere, skdFelterContainerTrans1);
                sendSkdMeldingTilGitteMiljoer.execute(skdMelding, skdRequestMeldingDefinition, new HashSet<>(environments));
            }
        } else {
            throw new IllegalArgumentException("SkdMeldingNavn: " + skdMeldingNavn + " does not exist.");
        }
    }

}