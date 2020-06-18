package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT.PERSON_KERNINFO_SERVICE_ROUTINE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Map;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.ctg.s610.domain.S610PersonType;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.dolly.ImporterPersonRequest;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.TpsServiceroutineFnrRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;

@Service
@RequiredArgsConstructor
public class ImporterPersonService {

    private final static String STATUS = "status";
    private final static String STATUS_OK = "00";

    private final PersonRepository personRepository;
    private final TpsServiceRoutineService tpsServiceRoutineService;
    private final TpsServiceroutineFnrRequest tpsFnrRequest;
    private final ObjectMapper objectMapper;

    public Person importFraTps(ImporterPersonRequest request) {

        TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_KERNINFO_SERVICE_ROUTINE,
                tpsFnrRequest.buildRequest(request.getIdent(), request.getMiljoe()), true);

        if (STATUS_OK.equals(((ResponseStatus) ((Map) response.getResponse()).get(STATUS)).getKode())) {

            S610PersonType tpsPerson = objectMapper.convertValue(((Map) response.getResponse()).get("data1"), S610PersonType.class);

            return isNotBlank(tpsPerson.getIdentType()) ? mapperFacade.map(tpsPerson, Person.class) : null;
        } else {
            return null;
        }
    }

    private final MapperFacade mapperFacade;

    public String importFraTpsOgLagre(ImporterPersonRequest request) {

        if (nonNull(personRepository.findByIdent(request.getIdent()))) {
            throw new TpsfFunctionalException(format("Ident %s finnes allerede", request.getIdent()));
        }

        Person person = importFraTps(request);

        return person.getIdent();
    }
}
