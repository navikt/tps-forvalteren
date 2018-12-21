package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT.PERSON_KERNINFO_SERVICE_ROUTINE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.TpsServiceroutineFnrRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;

@Service
public class TknrOgGtFraMiljoService {

    private static final String DATA = "data1";
    private static final String KOMMUNE = "kommunenr";
    private static final String LAND = "landKode";
    private static final String BYDEL = "bydel";
    private static final String TKNR = "tknr";
    private static final String FULL_BOSTED_ADR = "fullBostedsAdresse";
    private static final String BOSTED_ADR = "bostedsAdresse";
    private static final String BRUKER = "bruker";
    private static final String GEO_TILKNYT = "geografiskTilknytning";
    private static final String REGEL_FOR_GEO_TILKNYTNING = "regelForGeografiskTilknytning";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TpsServiceRoutineService tpsServiceRoutineService;

    @Autowired
    private TpsServiceroutineFnrRequest tpsFnrRequest;

    public List<Person> hentTknrOgGtPaPerson(List<Person> personer, Set<String> environments) {

        String environment = environments.iterator().next();
        personer.forEach(person -> {
            if (isNull(person.getGtVerdi())) {
                TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_KERNINFO_SERVICE_ROUTINE,
                        tpsFnrRequest.buildRequest(person, environment), true);
                person.setTknr(getTknr(response));
                person.setGtRegel(getGtRegel(response));
                person.setGtVerdi(getGtVerdi(response));
                person.setGtType(getGtType(response));
                personRepository.save(person);
            }
        });

        return personer;
    }

    private String getGtRegel(TpsServiceRoutineResponse response) {
        return (String) getBruker(response)
                .get(REGEL_FOR_GEO_TILKNYTNING);
    }

    private Map getBruker(TpsServiceRoutineResponse response) {
        return (Map) ((Map) ((Map) response.getResponse())
                .get(DATA))
                .get(BRUKER);
    }

    private String getTknr(TpsServiceRoutineResponse response) {
        return (String) ((Map) ((Map) ((Map) ((Map) response.getResponse())
                .get(DATA))
                .get(BOSTED_ADR))
                .get(FULL_BOSTED_ADR))
                .get(TKNR);
    }

    private String getGtVerdi(TpsServiceRoutineResponse response) {
        Map<String, Object> geoTilknytning = (Map) getBruker(response).get(GEO_TILKNYT);
        for (String key : geoTilknytning.keySet()) {
            if (isNotBlank((String) geoTilknytning.get(key))) {
                return (String) geoTilknytning.get(key);
            }
        }
        return null;
    }

    private String getGtType(TpsServiceRoutineResponse response) {
        Map<String, Object> geoTilknytning = (Map) getBruker(response).get(GEO_TILKNYT);
        if (nonNull(geoTilknytning.get(KOMMUNE))) {
            return "KNR";
        } else if (nonNull(geoTilknytning.get(LAND))) {
            return "LAND";
        } else if (nonNull(geoTilknytning.get(BYDEL))) {
            return "BYDEL";
        } else {
            return null;
        }
    }
}
