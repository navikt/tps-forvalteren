package no.nav.tps.forvalteren.service.command.testdata;

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

        if (sjekkForEgenAnsatt(person)) {
            environmentSet.forEach(environment ->
                navMeldinger.add(new TpsNavEndringsMelding(buildRequest(person), environment))
            );
        }
        return navMeldinger;
    }
    
    public TpsEndreEgenansattRequest buildRequest(Person person) {
        TpsEndreEgenansattRequest request = TpsEndreEgenansattRequest.builder()
                .serviceRutinenavn(EGEN_ANSATT_MLD_NAVN)
                .offentligIdent( person.getIdent())
                .fom( ConvertDateToString.yyyysMMsdd(person.getEgenAnsattDatoFom()))
                .build();
        if (person.getEgenAnsattDatoTom() != null) {
            request.setTom( ConvertDateToString.yyyysMMsdd(person.getEgenAnsattDatoTom()));
        }
        return request;
    }
    
    private boolean sjekkForEgenAnsatt(Person person) {
        return person.getEgenAnsattDatoFom() != null;
    }
}
