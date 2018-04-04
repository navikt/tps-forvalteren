package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;

@Service
public class SkdMessageCreatorTrans1 {

    @Autowired
    private GetSkdMeldingByName getSkdMeldingByName;

    @Autowired
    private SkdFelterContainerTrans1 skdFelterContainer; //TODO Fjerne avhengigheten. flytte dentil toString i SkdMeldingTrans1

    @Autowired
    private GenerateSkdMelding generateSkdMelding;

    public List<String> execute(String skdMeldingNavn, List<Person> persons, boolean addHeader) {
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = getSkdMeldingByName.execute(skdMeldingNavn);
        List<String> skdMeldinger = new ArrayList<>();
        if (skdRequestMeldingDefinitionOptional.isPresent()) {
            TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = skdRequestMeldingDefinitionOptional.get();
            for (Person person : persons) {
                String skdMelding = generateSkdMelding.execute(skdFelterContainer, skdRequestMeldingDefinition, person, addHeader);
                skdMeldinger.add(skdMelding);
            }
        } else {
            throw new IllegalArgumentException("SkdMeldingNavn: " + skdMeldingNavn + " does not exist.");
        }
        return skdMeldinger;
    }

}