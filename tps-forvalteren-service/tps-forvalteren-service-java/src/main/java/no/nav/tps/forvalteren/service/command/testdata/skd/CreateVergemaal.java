package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.VergemaalAarsakskode37.VERGEMAAL_MLD_NAVN;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
@RequiredArgsConstructor
public class CreateVergemaal {

    private final SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    public List<SkdMeldingTrans1> execute(List<Person> personerIGruppen, boolean addHeader) {

        return skdMessageCreatorTrans1.execute(VERGEMAAL_MLD_NAVN, personerIGruppen.stream()
                .filter(person -> !person.getVergemaal().isEmpty())
                .collect(Collectors.toList()), addHeader);
    }
}
