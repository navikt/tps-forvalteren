package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreSpraakkode.ENDRE_SPRAKKODE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreSprakkodeRequest;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class EndreSprakkodeService {
    
    public List<TpsNavEndringsMelding> execute(Person person, Set<String> environmentSet) {
        List<TpsNavEndringsMelding> navMeldinger = new ArrayList<>();
        
        if (person.getSprakKode() != null) {
            environmentSet.forEach(environment ->
                navMeldinger.add(new TpsNavEndringsMelding(buildRequest(person), environment))
            );
        }
        
        return navMeldinger;
    }
    
    public TpsEndreSprakkodeRequest buildRequest(Person person) {
        return TpsEndreSprakkodeRequest.builder()
                .serviceRutinenavn(ENDRE_SPRAKKODE)
                .offentligIdent(person.getIdent())
                .sprakKode(person.getSprakKode())
                .datoSprak(ConvertDateToString.yyyysMMsdd(person.getDatoSprak() != null ? person.getDatoSprak() : LocalDateTime.now()))
                .build();
    }
}
