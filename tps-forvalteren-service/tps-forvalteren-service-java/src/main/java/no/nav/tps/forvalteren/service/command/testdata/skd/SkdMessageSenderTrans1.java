package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersCreatorService;

@Service
public class SkdMessageSenderTrans1 {

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
    private SkdFelterContainerTrans1 skdFelterContainer;

    public void execute(String skdMeldingNavn, List<Person> persons, List<String> environments) {
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = getSkdMeldingByName.execute(skdMeldingNavn);

        if (skdRequestMeldingDefinitionOptional.isPresent()) {
            TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = skdRequestMeldingDefinitionOptional.get();
            for (Person person : persons) {
                Map<String, String> skdParametere = skdParametersCreatorService.execute(skdRequestMeldingDefinition, person);
                String skdMelding = skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParametere, skdFelterContainer);
                sendSkdMeldingTilGitteMiljoer.execute(skdMelding, skdRequestMeldingDefinition, new HashSet<>(environments));
            }
        } else {
            throw new IllegalArgumentException("SkdMeldingNavn: " + skdMeldingNavn + " does not exist.");
        }
    }

}