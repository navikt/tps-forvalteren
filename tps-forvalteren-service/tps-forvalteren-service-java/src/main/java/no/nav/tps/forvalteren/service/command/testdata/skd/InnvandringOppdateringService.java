package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;

@Service
@RequiredArgsConstructor
public class InnvandringOppdateringService {

    private final GetSkdMeldingByName getSkdMeldingByName;
    private final GenerateSkdMelding generateSkdMelding;

    public List<SkdMeldingTrans1> execute(String skdMeldingNavn, List<Person> persons, boolean addHeader) {

        return persons.stream()
                .filter(person -> !person.isDoedFoedt())
                .map(person -> {
                    Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional =
                            getSkdMeldingByName.execute(skdMeldingNavn);
                    if (skdRequestMeldingDefinitionOptional.isPresent()) {
                        return generateSkdMelding.execute(skdRequestMeldingDefinitionOptional.get(), person, addHeader);
                    } else {
                        throw new IllegalArgumentException("SkdMeldingNavn: " + skdMeldingNavn + " does not exist.");
                    }
                })
                .collect(Collectors.toList());
    }
}
