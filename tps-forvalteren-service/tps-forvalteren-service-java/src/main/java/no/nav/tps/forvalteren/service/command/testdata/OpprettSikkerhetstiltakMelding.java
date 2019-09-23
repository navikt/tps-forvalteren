package no.nav.tps.forvalteren.service.command.testdata;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreSikkerhetstiltak.SIKKERHETSTILTAK_MLD_NAVN;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
                navMeldinger.add(new TpsNavEndringsMelding(TpsEndreSikkerhetstiltakRequest.builder()
                        .serviceRutinenavn(SIKKERHETSTILTAK_MLD_NAVN)
                        .offentligIdent(person.getIdent())
                        .typeSikkerhetsTiltak(person.getTypeSikkerhetsTiltak())
                        .fom(ConvertDateToString.yyyysMMsdd(person.getSikkerhetsTiltakDatoFom()))
                        .tom(ConvertDateToString.yyyysMMsdd(person.getSikkerhetsTiltakDatoTom()))
                        .beskrSikkerhetsTiltak(person.getBeskrSikkerhetsTiltak())
                        .build(), environment))
            );
        }
        
        return navMeldinger;
    }

    private boolean sjekkForSikkerhetstiltak(Person person) {
        return isNotBlank(person.getTypeSikkerhetsTiltak()) &&
                isNotBlank(person.getBeskrSikkerhetsTiltak()) &&
                nonNull(person.getSikkerhetsTiltakDatoFom());
    }
}
