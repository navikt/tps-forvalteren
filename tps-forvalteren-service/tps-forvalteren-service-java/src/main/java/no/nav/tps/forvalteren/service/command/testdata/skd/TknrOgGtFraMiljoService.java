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
        Map bruker = getBruker(response);
        return nonNull(bruker) ? (String) bruker.get(REGEL_FOR_GEO_TILKNYTNING) : null;
    }

    private Map getData(TpsServiceRoutineResponse response) {
        return nonNull(response) && nonNull(response.getResponse()) ?
                (Map) ((Map) response.getResponse()).get(DATA) : null;
    }

    private Map getBruker(TpsServiceRoutineResponse response) {
        Map data = getData(response);
        return nonNull(data) ? (Map) data.get(BRUKER) : null;
    }

    private Map getBostedAdr(TpsServiceRoutineResponse response) {
        Map data = getData(response);
        return nonNull(data) ? (Map) data.get(BOSTED_ADR) : null;
    }

    private Map getFullBostedAdr(TpsServiceRoutineResponse response) {
        Map bostedAdr = getBostedAdr(response);
        return nonNull(bostedAdr) ? (Map) bostedAdr.get(FULL_BOSTED_ADR) : null;
    }

    private String getTknr(TpsServiceRoutineResponse response) {
        Map fullbostedAdr = getFullBostedAdr(response);
        return nonNull(fullbostedAdr) && fullbostedAdr.get(TKNR) instanceof String ? (String) fullbostedAdr.get(TKNR) : null;
    }

    private Map getGeoTilknytning(TpsServiceRoutineResponse response) {
        Map bruker = getBruker(response);
        return nonNull(bruker) ? (Map) bruker.get(GEO_TILKNYT) : null;
    }

    private String getGtVerdi(TpsServiceRoutineResponse response) {
        Map<String, Object> geoTilknytning =  getGeoTilknytning(response);
        if (nonNull(geoTilknytning)) {
            for (Map.Entry<String, Object> entry : geoTilknytning.entrySet()) {
                if (entry.getValue() instanceof String && isNotBlank((String) entry.getValue())) {
                    return (String) entry.getValue();
                }
            }
        }
        return null;
    }

    private String getGtType(TpsServiceRoutineResponse response) {
        Map geoTilknytning = getGeoTilknytning(response);
        if (nonNull(geoTilknytning)) {
            if (isNotBlank((String) geoTilknytning.get(KOMMUNE))) {
                return "KNR";
            } else if (isNotBlank((String) geoTilknytning.get(LAND))) {
                return "LAND";
            } else if (isNotBlank((String) geoTilknytning.get(BYDEL))) {
                return "BYDEL";
            }
        }
        return null;
    }
}
