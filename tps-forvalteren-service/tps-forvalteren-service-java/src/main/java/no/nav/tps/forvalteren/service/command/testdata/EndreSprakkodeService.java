package no.nav.tps.forvalteren.service.command.testdata;

import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreSpraakkode.ENDRE_SPRAKKODE;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

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
        
        if (nonNull(person.getSprakKode())) {
            environmentSet.forEach(environment ->
                navMeldinger.add(new TpsNavEndringsMelding(TpsEndreSprakkodeRequest.builder()
                        .serviceRutinenavn(ENDRE_SPRAKKODE)
                        .offentligIdent(person.getIdent())
                        .sprakKode(person.getSprakKode())
                        .datoSprak(ConvertDateToString.yyyysMMsdd(nullcheckSetDefaultValue(person.getDatoSprak(), now())))
                        .build(), environment))
            );
        }
        
        return navMeldinger;
    }
}
