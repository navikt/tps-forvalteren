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
public class OpprettEgenAnsattMelding {

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;

    private static final String EGEN_ANSATT_MLD_NAVN = "endre_egen_ansatt";

    public List<TpsNavEndringsMelding> execute(Person person, Set<String> environmentSet) {
        Map<String, Object> tpsRequestParameter = new LinkedHashMap<>();
        List<TpsNavEndringsMelding> navMeldinger = new ArrayList<>();

        if (sjekkForEgenAnsatt(person)) {
            environmentSet.forEach(environment -> {

                TpsNavEndringsMelding melding = new TpsNavEndringsMelding();

                tpsRequestParameter.put("offentligIdent", person.getIdent());
                tpsRequestParameter.put("fom", ConvertDateToString.yyyy_MM_dd(person.getEgenAnsattDatoFom()));
                tpsRequestParameter.put("environment", environment);
                tpsRequestParameter.put("serviceRutinenavn", EGEN_ANSATT_MLD_NAVN);
                if (person.getEgenAnsattDatoTom() != null) {
                    tpsRequestParameter.put("tom", ConvertDateToString.yyyy_MM_dd(person.getEgenAnsattDatoTom()));
                }

                melding.setMelding(mappingUtils.convertToTpsServiceRoutineRequest(EGEN_ANSATT_MLD_NAVN, tpsRequestParameter));
                melding.setMiljo(environment);
                navMeldinger.add(melding);
            });
        }
        return navMeldinger;
    }

    private boolean sjekkForEgenAnsatt(Person person) {
        return person.getEgenAnsattDatoFom() != null;
    }
}
