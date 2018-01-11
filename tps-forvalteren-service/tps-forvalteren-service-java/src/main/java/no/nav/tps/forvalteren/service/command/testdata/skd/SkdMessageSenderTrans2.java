package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies.BarnetranseSkdParameterStrategy;

@Service
public class SkdMessageSenderTrans2 {

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Autowired
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Autowired
    private GetSkdMeldingByName getSkdMeldingByName;

    @Autowired
    private SkdFelterContainerTrans2 skdFelterContainer;

    @Autowired
    private BarnetranseSkdParameterStrategy barnetranseSkdParameterStrategy;

    public List<String> execute(String skdMeldingNavn, Person forelder, List<Person> barn, boolean addHeader) {
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = getSkdMeldingByName.execute(skdMeldingNavn);
        List<String> skdMeldinger = new ArrayList<>();
        if (skdRequestMeldingDefinitionOptional.isPresent()) {
            Map<String, String> skdParametere = barnetranseSkdParameterStrategy.execute(forelder, barn);
            String skdMelding = skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParametere, skdFelterContainer, addHeader);
            skdMeldinger.add(skdMelding);
        } else {
            throw new IllegalArgumentException("SkdMeldingNavn: " + skdMeldingNavn + " does not exist.");
        }
        return skdMeldinger;
    }

}
