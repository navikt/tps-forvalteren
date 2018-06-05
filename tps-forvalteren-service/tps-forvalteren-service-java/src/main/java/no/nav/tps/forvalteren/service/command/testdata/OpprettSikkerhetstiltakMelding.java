package no.nav.tps.forvalteren.service.command.testdata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpprettSikkerhetstiltakMelding {

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;

    private static final String SIKKERHETSTILTAK_MLD_NAVN = "endre_sikkerhetstiltak";

    public List<TpsNavEndringsMelding> execute(Person person, Set<String> environmentSet){
        Map<String, Object> tpsRequestParameter = new LinkedHashMap<>();
        List<TpsNavEndringsMelding> navMeldinger = new ArrayList<>();

        if(sjekkForSikkerhetstiltak(person)){
            environmentSet.forEach(environment -> {

                TpsNavEndringsMelding melding = new TpsNavEndringsMelding();

                tpsRequestParameter.put("offentligIdent", person.getIdent());
                tpsRequestParameter.put("typeSikkerhetsTiltak", person.getTypeSikkerhetsTiltak());
                tpsRequestParameter.put("fom", ConvertDateToString.yyyy_MM_dd(person.getSikkerhetsTiltakDatoFom()));
                tpsRequestParameter.put("beskrSikkerhetsTiltak", person.getBeskrSikkerhetsTiltak());
                tpsRequestParameter.put("serviceRutinenavn", SIKKERHETSTILTAK_MLD_NAVN);

                if(person.getSikkerhetsTiltakDatoTom() != null){
                    tpsRequestParameter.put("tom", ConvertDateToString.yyyy_MM_dd(person.getSikkerhetsTiltakDatoTom()));
                }

                melding.setMelding(mappingUtils.convertToTpsServiceRoutineRequest(SIKKERHETSTILTAK_MLD_NAVN, tpsRequestParameter));
                melding.setMiljo(environment);
                navMeldinger.add(melding);

            });
        }

        return navMeldinger;
    }

    private boolean sjekkForSikkerhetstiltak(Person person){
        if(person.getTypeSikkerhetsTiltak()!= null){
            return true;
        } else {
            return false;
        }
    }
}
