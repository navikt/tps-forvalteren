package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkdMessageCreatorTrans1 {

    private GetSkdMeldingByName getSkdMeldingByName;
    private GenerateSkdMelding generateSkdMelding;

    @Autowired
    public SkdMessageCreatorTrans1(GetSkdMeldingByName getSkdMeldingByName, GenerateSkdMelding generateSkdMelding) {
        this.getSkdMeldingByName = getSkdMeldingByName;
        this.generateSkdMelding = generateSkdMelding;
    }

    public List<SkdMeldingTrans1> execute(String skdMeldingNavn, List<Person> persons, boolean addHeader) {
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = getSkdMeldingByName.execute(skdMeldingNavn);
        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();
        if (skdRequestMeldingDefinitionOptional.isPresent()) {
            TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = skdRequestMeldingDefinitionOptional.get();
            for (Person person : persons) {
                SkdMeldingTrans1 skdMelding = generateSkdMelding.execute(skdRequestMeldingDefinition, person, addHeader);
                skdMeldinger.add(skdMelding);
            }
        } else {
            throw new IllegalArgumentException("SkdMeldingNavn: " + skdMeldingNavn + " does not exist.");
        }
        return skdMeldinger;
    }

    public List<SkdMeldingTrans1> createVergemaalSkdMelding(List<Vergemaal> vergemaalListe, boolean addHeader) {
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = getSkdMeldingByName.execute("Vergemaal");
        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();
        if (skdRequestMeldingDefinitionOptional.isPresent()) {
            for (Vergemaal vergemaal : vergemaalListe) {
                SkdMeldingTrans1 skdMelding = generateSkdMelding.execute(vergemaal, addHeader);
                skdMeldinger.add(skdMelding);
            }
        }
        return skdMeldinger;
    }

}