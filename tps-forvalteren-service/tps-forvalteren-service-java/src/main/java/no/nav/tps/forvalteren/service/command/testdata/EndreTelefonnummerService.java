package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreNorskGironummer.ENDRE_KONTONUMMER;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreTelefonnummerRequest;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;

@Service
public class EndreTelefonnummerService {

    public List<TpsNavEndringsMelding> execute(Person person, Set<String> environmentSet) {
        List<TpsNavEndringsMelding> navMeldinger = new ArrayList<>();

        if (isNotBlank(person.getTelefonnummer_1())) {
            environmentSet.forEach(environment ->

                    navMeldinger.add(new TpsNavEndringsMelding(TpsEndreTelefonnummerRequest.builder()
                            .serviceRutinenavn(ENDRE_KONTONUMMER)
                            .offentligIdent(person.getIdent())
                            .typeTelefon("MOBI")
                            .telefonLandkode(person.getTelefonLandskode_1())
                            .telefonNr(person.getTelefonnummer_1())
                            .build(), environment))
            );
        }

        if (isNotBlank(person.getTelefonnummer_2())) {
            environmentSet.forEach(environment ->

                    navMeldinger.add(new TpsNavEndringsMelding(TpsEndreTelefonnummerRequest.builder()
                            .serviceRutinenavn(ENDRE_KONTONUMMER)
                            .offentligIdent(person.getIdent())
                            .typeTelefon("HJET")
                            .telefonLandkode(person.getTelefonLandskode_2())
                            .telefonNr(person.getTelefonnummer_2())
                            .build(), environment))
            );
        }

        return navMeldinger;
    }
}
