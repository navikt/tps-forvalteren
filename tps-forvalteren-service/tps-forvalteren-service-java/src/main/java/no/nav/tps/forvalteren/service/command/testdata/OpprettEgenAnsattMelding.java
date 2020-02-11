package no.nav.tps.forvalteren.service.command.testdata;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreEgenAnsatt.EGEN_ANSATT_MLD_NAVN;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreEgenansattRequest;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class OpprettEgenAnsattMelding {

    public List<TpsNavEndringsMelding> execute(Person person, Set<String> environmentSet) {
        List<TpsNavEndringsMelding> navMeldinger = new ArrayList<>();

        if (nonNull(person.getEgenAnsattDatoFom())) {
            environmentSet.forEach(environment ->
                    navMeldinger.add(new TpsNavEndringsMelding(TpsEndreEgenansattRequest.builder()
                            .serviceRutinenavn(EGEN_ANSATT_MLD_NAVN)
                            .offentligIdent(person.getIdent())
                            .fom(ConvertDateToString.yyyysMMsdd(person.getEgenAnsattDatoFom()))
                            .tom(ConvertDateToString.yyyysMMsdd(person.getEgenAnsattDatoTom()))
                            .build(), environment))
            );
        }
        return navMeldinger;
    }
}
