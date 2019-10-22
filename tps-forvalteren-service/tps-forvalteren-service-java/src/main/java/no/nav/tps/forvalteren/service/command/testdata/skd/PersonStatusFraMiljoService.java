package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT.PERSON_KERNINFO_SERVICE_ROUTINE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class PersonStatusFraMiljoService {

    private static final String DATA = "data1";
    private static final String KOMMUNE = "kommunenr";
    private static final String LAND = "landKode";
    private static final String BYDEL = "bydel";
    private static final String TKNR = "tknr";
    private static final String TKNAVN = "tkNavn";
    private static final String FULL_BOSTED_ADR = "fullBostedsAdresse";
    private static final String BOSTED_ADR = "bostedsAdresse";
    private static final String BRUKER = "bruker";
    private static final String GEO_TILKNYT = "geografiskTilknytning";
    private static final String REGEL_FOR_GEO_TILKNYTNING = "regelForGeografiskTilknytning";
    private static final String PERSONSTATUS_DETALJ = "personstatusDetalj";
    private static final String KODE_PERSONSTATUS = "kodePersonstatus";
    private static final String SIVILSTAND_DETALJ = "sivilstandDetalj";
    private static final String SIVILSTAND_KODE = "kodeSivilstand";
    private static final String SIVILSTAND_DATO = "datoSivilstand";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TpsServiceRoutineService tpsServiceRoutineService;

    @Autowired
    private TpsServiceroutineFnrRequest tpsFnrRequest;

    public List<Person> hentStatusOgSettPaaPerson(List<Person> personer, Set<String> environments) {

        environments.forEach(environment ->
                personer.forEach(person -> {
                    if (isBlank(person.getGtVerdi())) {
                        TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_KERNINFO_SERVICE_ROUTINE,
                                tpsFnrRequest.buildRequest(person, environment), true);
                        person.setTknr(getTknr(response));
                        person.setTknavn(getTknavn(response));
                        person.setGtRegel(getGtRegel(response));
                        person.setGtVerdi(getGtVerdi(response));
                        person.setGtType(getGtType(response));
                        person.setPersonStatus(getPersonStatus(response));
                        person.setSivilstand(getSivilstand(response));
                        person.setSivilstandRegdato(getSivilstandRegdato(response));
                        personRepository.save(person);
                    }
                })
        );

        return personer;
    }

    private String getSivilstand(TpsServiceRoutineResponse response) {
        Map data = getData(response);
        Map sivilstandDetalj = nonNull(data) ? (Map) data.get(SIVILSTAND_DETALJ) : null;
        return nonNull(sivilstandDetalj) ? (String) sivilstandDetalj.get(SIVILSTAND_KODE) : null;
    }

    private LocalDateTime getSivilstandRegdato(TpsServiceRoutineResponse response) {
        Map data = getData(response);
        Map sivilstandDetalj = nonNull(data) ? (Map) data.get(SIVILSTAND_DETALJ) : null;
        String datoSivilstand = nonNull(sivilstandDetalj) ? (String) sivilstandDetalj.get(SIVILSTAND_DATO) : null;
        return isNotBlank(datoSivilstand) ? LocalDate.parse(datoSivilstand).atStartOfDay() : null;
    }

    private String getPersonStatus(TpsServiceRoutineResponse response) {
        Map data = getData(response);
        Map personstatusDetalj = nonNull(data) ? (Map) data.get(PERSONSTATUS_DETALJ) : null;
        return nonNull(personstatusDetalj) ? (String) personstatusDetalj.get(KODE_PERSONSTATUS) : null;
    }

    private String getGtRegel(TpsServiceRoutineResponse response) {
        Map bruker = getBruker(response);
        return nonNull(bruker) && bruker.get(REGEL_FOR_GEO_TILKNYTNING) instanceof String ?
                (String) bruker.get(REGEL_FOR_GEO_TILKNYTNING) : null;
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
        if (nonNull(fullbostedAdr) && fullbostedAdr.get(TKNR) instanceof String) {
            return (String) fullbostedAdr.get(TKNR);
        } else if (nonNull(fullbostedAdr) && fullbostedAdr.get(TKNR) instanceof Integer) {
            return ((Integer) fullbostedAdr.get(TKNR)).toString();
        }
        return null;
    }

    private String getTknavn(TpsServiceRoutineResponse response) {
        Map fullbostedAdr = getFullBostedAdr(response);
        return nonNull(fullbostedAdr) ? (String) fullbostedAdr.get(TKNAVN) : null;
    }

    private Map getGeoTilknytning(TpsServiceRoutineResponse response) {
        Map bruker = getBruker(response);
        return nonNull(bruker) ? (Map) bruker.get(GEO_TILKNYT) : null;
    }

    private String getGtVerdi(TpsServiceRoutineResponse response) {
        Map<String, Object> geoTilknytning = getGeoTilknytning(response);
        if (nonNull(geoTilknytning)) {
            for (Map.Entry<String, Object> entry : geoTilknytning.entrySet()) {
                if (entry.getValue() instanceof String && isNotBlank((String) entry.getValue())) {
                    return (String) entry.getValue();
                } else if (entry.getValue() instanceof Integer) {
                    return ((Integer) entry.getValue()).toString();
                }
            }
        }
        return null;
    }

    private String getGtType(TpsServiceRoutineResponse response) {
        Map geoTilknytning = getGeoTilknytning(response);
        if (nonNull(geoTilknytning)) {
            if (geoTilknytning.get(KOMMUNE) instanceof String && isNotBlank((String) geoTilknytning.get(KOMMUNE)) ||
                    geoTilknytning.get(KOMMUNE) instanceof Integer) {
                return "KNR";
            } else if (geoTilknytning.get(LAND) instanceof String && isNotBlank((String) geoTilknytning.get(LAND)) ||
                    geoTilknytning.get(LAND) instanceof Integer) {
                return "LAND";
            } else if (geoTilknytning.get(BYDEL) instanceof String && isNotBlank((String) geoTilknytning.get(BYDEL)) ||
                    geoTilknytning.get(BYDEL) instanceof Integer) {
                return "BYDEL";
            }
        }
        return null;
    }
}
