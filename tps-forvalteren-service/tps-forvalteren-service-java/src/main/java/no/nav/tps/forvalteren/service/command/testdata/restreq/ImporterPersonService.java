package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT.PERSON_KERNINFO_SERVICE_ROUTINE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.ctg.s610.domain.S610EnkeltRelasjonType;
import no.nav.tps.ctg.s610.domain.S610PersonType;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.dolly.ImporterPersonRequest;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;

@Service
@RequiredArgsConstructor
public class ImporterPersonService {

    private final static String STATUS = "status";
    private final static String STATUS_OK = "00";

    private final PersonRepository personRepository;
    private final TpsServiceRoutineService tpsServiceRoutineService;
    private final ObjectMapper objectMapper;
    private final MapperFacade mapperFacade;

    public Person importFraTps(ImporterPersonRequest request) {

        S610PersonType tpsPerson = importFromTps(request.getIdent(), request.getMiljoe());

        PersonRelasjon relasjoner = getRelasjoner(tpsPerson, request.getMiljoe());

        return isNotBlank(tpsPerson.getIdentType()) ? buildPersonWithRelasjon(relasjoner) : null;
    }

    public String importFraTpsOgLagre(ImporterPersonRequest request) {

        if (nonNull(personRepository.findByIdent(request.getIdent()))) {
            throw new TpsfFunctionalException(format("Ident %s finnes allerede", request.getIdent()));
        }

        Person person = importFraTps(request);

        List<Relasjon> relasjoner = person.getRelasjoner();
        person.setRelasjoner(null);
        savePerson(person);

        person.setRelasjoner(relasjoner);
        person.getRelasjoner().forEach(relasjon ->
                savePerson(relasjon.getPersonRelasjonMed()));
        savePerson(person);

        return person.getIdent();
    }

    private Person buildPersonWithRelasjon(PersonRelasjon personRelasjon) {

        Person person = mapperFacade.map(personRelasjon.getHovedperson(), Person.class);

        if (nonNull(personRelasjon.getHovedperson().getBruker().getRelasjoner())) {

            Map<String, Person> familie = personRelasjon.getRelasjoner().values().parallelStream()
                    .map(tpsPerson -> mapperFacade.map(tpsPerson, Person.class))
                    .collect(Collectors.toMap(Person::getIdent, pers -> pers));

            personRelasjon.getHovedperson().getBruker().getRelasjoner().getRelasjon().forEach(relasjon -> {
                        person.getRelasjoner().add(Relasjon.builder()
                                .relasjonTypeNavn(relasjon.getTypeRelasjon().name())
                                .personRelasjonMed(familie.get(relasjon.getFnrRelasjon()))
                                .person(person)
                                .build());
                        familie.get(relasjon.getFnrRelasjon()).getRelasjoner().add(
                                Relasjon.builder()
                                        .relasjonTypeNavn(personRelasjon.getRelasjoner().get(relasjon.getFnrRelasjon())
                                                .getBruker().getRelasjoner().getRelasjon().stream()
                                                .filter(relasjon2 -> person.getIdent().equals(relasjon2.getFnrRelasjon()))
                                                .map(S610EnkeltRelasjonType::getTypeRelasjon)
                                                .findFirst().get().name())
                                        .personRelasjonMed(person)
                                        .person(familie.get(relasjon.getFnrRelasjon()))
                                        .build());
                    });
        }
        return person;
    }

    private Person savePerson(Person person) {

        person.setOpprettetAv(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        person.setOpprettetDato(LocalDateTime.now());
        person.setRegdato(LocalDateTime.now());
        return personRepository.save(person);
    }

    private S610PersonType importFromTps(String ident, String environment) {

        TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_KERNINFO_SERVICE_ROUTINE,
                buildRequest(ident, environment), true);

        if (STATUS_OK.equals(((ResponseStatus) ((Map) response.getResponse()).get(STATUS)).getKode())) {
            return objectMapper.convertValue(((Map) response.getResponse()).get("data1"), S610PersonType.class);

        } else {
            throw new TpsfFunctionalException(
                    format("Ident %s finnes ikke i miljoe %s", ident, environment));
        }
    }

    private PersonRelasjon getRelasjoner(S610PersonType tpsPerson, String miljoe) {

        return PersonRelasjon.builder()
                .relasjoner(nonNull(tpsPerson.getBruker().getRelasjoner()) ?
                        tpsPerson.getBruker().getRelasjoner().getRelasjon().parallelStream()
                                .map(relasjon -> importFromTps(relasjon.getFnrRelasjon(), miljoe))
                                .collect(Collectors.toMap(S610PersonType::getFodselsnummer, person -> person)) :
                        Collections.emptyMap())
                .hovedperson(tpsPerson)
                .build();
    }

    private Map buildRequest(String ident, String environment) {
        Map<String, Object> params = new HashMap<>();
        params.put("fnr", ident);
        params.put("aksjonsKode", "C1");
        params.put("environment", environment);
        return params;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PersonRelasjon {

        private S610PersonType hovedperson;
        private Map<String, S610PersonType> relasjoner;
    }
}
