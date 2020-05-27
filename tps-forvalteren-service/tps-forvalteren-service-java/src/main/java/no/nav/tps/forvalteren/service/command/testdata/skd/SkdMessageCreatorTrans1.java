package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;

@Service
public class SkdMessageCreatorTrans1 {

    private GetSkdMeldingByName getSkdMeldingByName;
    private GenerateSkdMelding generateSkdMelding;

    @Autowired
    public SkdMessageCreatorTrans1(GetSkdMeldingByName getSkdMeldingByName, GenerateSkdMelding generateSkdMelding) {
        this.getSkdMeldingByName = getSkdMeldingByName;
        this.generateSkdMelding = generateSkdMelding;
    }

    public SkdMeldingTrans1 execute(String skdMeldingNavn, Person person, boolean addHeader) {
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = getSkdMeldingByName.execute(skdMeldingNavn);

        if (skdRequestMeldingDefinitionOptional.isPresent()) {
            TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = skdRequestMeldingDefinitionOptional.get();
            return generateSkdMelding.execute(skdRequestMeldingDefinition, person, addHeader);
        } else {
            throw new IllegalArgumentException("SkdMeldingNavn: " + skdMeldingNavn + " does not exist.");
        }
    }

    public List<SkdMeldingTrans1> execute(String skdMeldingNavn, List<Person> persons, boolean addHeader) {

        List<SkdMeldingTrans1> skdMeldinger = new ArrayList(persons.size());
        for (Person person : persons) {
            skdMeldinger.add(execute(skdMeldingNavn, person, addHeader));
        }
        return skdMeldinger;
    }

    public List<SkdMeldingTrans1> createVergemaalSkdMelding(List<Vergemaal> vergemaalListe, boolean addHeader) {
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = getSkdMeldingByName.execute("Vergemaal");
        List<SkdMeldingTrans1> skdMeldinger = new ArrayList();
        if (skdRequestMeldingDefinitionOptional.isPresent()) {
            for (Vergemaal vergemaal : vergemaalListe) {
                SkdMeldingTrans1 skdMelding = generateSkdMelding.execute(vergemaal, addHeader);
                skdMeldinger.add(skdMelding);
            }
        }
        return skdMeldinger;
    }

}