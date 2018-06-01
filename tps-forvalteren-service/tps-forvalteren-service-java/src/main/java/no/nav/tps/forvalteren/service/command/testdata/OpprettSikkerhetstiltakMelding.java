package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreSikkerhetsTiltak.SIKKERHETSTILTAK_MLD_NAVN;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreSikkerhetstiltakRequest;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class OpprettSikkerhetstiltakMelding {
    
    public List<TpsNavEndringsMelding> execute(Person person, Set<String> environmentSet) {
        List<TpsNavEndringsMelding> navMeldinger = new ArrayList<>();
        
        if (sjekkForSikkerhetstiltak(person)) {
            environmentSet.forEach(environment ->
                navMeldinger.add(new TpsNavEndringsMelding(buildRequest(person), environment))
            );
        }
        
        return navMeldinger;
    }
    
    public TpsEndreSikkerhetstiltakRequest buildRequest(Person person) {
        TpsEndreSikkerhetstiltakRequest request = TpsEndreSikkerhetstiltakRequest.builder()
                .serviceRutinenavn(SIKKERHETSTILTAK_MLD_NAVN)
                .offentligIdent(person.getIdent())
                .typeSikkerhetsTiltak(person.getTypeSikkerhetsTiltak())
                .fom(ConvertDateToString.yyyy_MM_dd(person.getSikkerhetsTiltakDatoFom()))
                .beskrSikkerhetsTiltak(person.getBeskrSikkerhetsTiltak())
                .build();
    
        if (person.getSikkerhetsTiltakDatoTom() != null) {
            request.setTom( ConvertDateToString.yyyy_MM_dd(person.getSikkerhetsTiltakDatoTom()));
        }
        return request;
    }
    
    private boolean sjekkForSikkerhetstiltak(Person person) {
        return person.getTypeSikkerhetsTiltak() != null;
    }
}
