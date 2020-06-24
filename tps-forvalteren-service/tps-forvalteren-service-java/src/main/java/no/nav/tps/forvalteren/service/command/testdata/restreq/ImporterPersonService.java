package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT.PERSON_KERNINFO_SERVICE_ROUTINE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import no.nav.tps.ctg.s610.domain.RelasjonType;
import no.nav.tps.ctg.s610.domain.S610EnkeltRelasjonType;
import no.nav.tps.ctg.s610.domain.S610PersonType;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.dolly.ImporterPersonLagreRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.ImporterPersonRequest;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;

@Service
@RequiredArgsConstructor
public class ImporterPersonService {

    private static final String STATUS = "status";
    private static final String STATUS_OK = "00";
    private static final String TPSWS = "tpsws";

    private final PersonRepository personRepository;
    private final TpsServiceRoutineService tpsServiceRoutineService;
    private final GetEnvironments getEnvironments;
    private final ObjectMapper objectMapper;
    private final MapperFacade mapperFacade;

    public Map<String, Person> importFraTps(ImporterPersonRequest request) {

        Map<String, S610PersonType> miljoePerson = importFromTps(request);

        Map<String, PersonRelasjon> relasjoner = getRelasjoner(miljoePerson);

        return !miljoePerson.isEmpty() ? buildPersonWithRelasjon(relasjoner) : null;
    }

    public String importFraTpsOgLagre(ImporterPersonLagreRequest request) {

        if (nonNull(personRepository.findByIdent(request.getIdent()))) {
            throw new TpsfFunctionalException(format("Ident %s finnes allerede", request.getIdent()));
        }

        if (isBlank(request.getMiljoe())) {
            throw new TpsfTechnicalException("Miljoe må angis ved lagring");
        }

        Map<String, Person> person = importFraTps(ImporterPersonRequest.builder()
                .ident(request.getIdent())
                .miljoe(Collections.singleton(request.getMiljoe()))
                .build());

        List<Relasjon> relasjoner = person.get(0).getRelasjoner();
        person.get(0).setRelasjoner(null);
        savePerson(person.get(0));

        person.get(0).setRelasjoner(relasjoner);
        person.get(0).getRelasjoner().forEach(relasjon ->
                savePerson(relasjon.getPersonRelasjonMed()));
        savePerson(person.get(0));

        return person.get(0).getIdent();
    }

    private Map<String, Person> buildPersonWithRelasjon(Map<String, PersonRelasjon> personRelasjon) {

        Map<String, Person> personMiljoe = personRelasjon.entrySet().parallelStream()
                .map(entry -> PersonMiljoe.builder()
                        .miljoe(entry.getKey())
                        .person(mapperFacade.map(entry.getValue().getHovedperson(), Person.class))
                        .build())
                .collect(Collectors.toMap(PersonMiljoe::getMiljoe, PersonMiljoe::getPerson));

        personRelasjon.entrySet().forEach(entry -> {
            if (nonNull(entry.getValue().hovedperson.getBruker().getRelasjoner())) {

                Map<String, Person> familie = entry.getValue().getRelasjoner().entrySet().parallelStream()
                        .map(entry2 -> mapperFacade.map(entry2.getValue(), Person.class))
                        .collect(Collectors.toMap(Person::getIdent, pers -> pers));

                entry.getValue().getHovedperson().getBruker().getRelasjoner().getRelasjon().forEach(relasjon -> {
                    personMiljoe.get(entry.getKey()).getRelasjoner().add(Relasjon.builder()
                            .relasjonTypeNavn(relasjon.getTypeRelasjon().name())
                            .personRelasjonMed(familie.get(relasjon.getFnrRelasjon()))
                            .person(personMiljoe.get(entry.getKey()))
                            .build());
                    familie.get(relasjon.getFnrRelasjon()).getRelasjoner().add(
                            Relasjon.builder()
                                    .relasjonTypeNavn(entry.getValue().getRelasjoner().get(relasjon.getFnrRelasjon())
                                            .getBruker().getRelasjoner().getRelasjon().stream()
                                            .filter(relasjon2 -> personMiljoe.get(entry.getKey()).getIdent().equals(relasjon2.getFnrRelasjon()))
                                            .map(S610EnkeltRelasjonType::getTypeRelasjon)
                                            .findFirst().get().name())
                                    .personRelasjonMed(personMiljoe.get(entry.getKey()))
                                    .person(familie.get(relasjon.getFnrRelasjon()))
                                    .build());
                });
                if (!personMiljoe.get(entry.getKey()).getSivilstander().isEmpty()) {
                    personMiljoe.get(entry.getKey()).getSivilstander().get(0).setPersonRelasjonMed(
                            familie.get(
                                    entry.getValue().getHovedperson().getBruker().getRelasjoner().getRelasjon().stream()
                                            .filter(relasjon -> RelasjonType.EKTE == relasjon.getTypeRelasjon())
                                            .map(S610EnkeltRelasjonType::getFnrRelasjon)
                                            .findFirst().get()
                            ));
                }
                familie.values().forEach(person -> {
                    if (!person.getSivilstander().isEmpty()) {
                        person.getSivilstander().get(0).setPersonRelasjonMed(personMiljoe.get(entry.getKey()));
                    }
                });
            }
        });
        return personMiljoe;
    }

    private Person savePerson(Person person) {

        person.setOpprettetAv(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        person.setOpprettetDato(LocalDateTime.now());
        person.setRegdato(LocalDateTime.now());
        return personRepository.save(person);
    }

    private Map<String, S610PersonType> importFromTps(ImporterPersonRequest request) {

        Set<String> environments = nonNull(request.getMiljoe()) && !request.getMiljoe().isEmpty() ?
                request.getMiljoe() :
                getEnvironments.getEnvironmentsFromFasit(TPSWS);

        return environments.parallelStream()
                .map(env -> TpsPersonMiljoe.builder()
                        .miljoe(env)
                        .person(readFromTps(request.getIdent(), env))
                        .build())
                .filter(tpsPerson -> isNotBlank(tpsPerson.getPerson().getFodselsnummer()))
                .collect(Collectors.toMap(TpsPersonMiljoe::getMiljoe, TpsPersonMiljoe::getPerson));
    }

    private S610PersonType readFromTps(String ident, String environment) {

        TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_KERNINFO_SERVICE_ROUTINE,
                buildRequest(ident, environment), true);

        if (STATUS_OK.equals(((ResponseStatus) ((Map) response.getResponse()).get(STATUS)).getKode())) {
            return objectMapper.convertValue(((Map) response.getResponse()).get("data1"), S610PersonType.class);

        } else {
            return new S610PersonType();
        }
    }

    private Map<String, PersonRelasjon> getRelasjoner(Map<String, S610PersonType> tpsPerson) {

        return tpsPerson.entrySet().parallelStream()
                .map(entry -> PersonRelasjonMiljoe.builder()
                        .miljoe(entry.getKey())
                        .personRelasjon(getRelasjoner(entry.getValue(), entry.getKey()))
                        .build())
                .collect(Collectors.toMap(PersonRelasjonMiljoe::getMiljoe, PersonRelasjonMiljoe::getPersonRelasjon));
    }

    private PersonRelasjon getRelasjoner(S610PersonType tpsPerson, String miljoe) {

        return PersonRelasjon.builder()
                .relasjoner(nonNull(tpsPerson.getBruker().getRelasjoner()) ?
                        tpsPerson.getBruker().getRelasjoner().getRelasjon().parallelStream()
                                .map(relasjon -> readFromTps(relasjon.getFnrRelasjon(), miljoe))
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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PersonRelasjonMiljoe {

        private String miljoe;
        private PersonRelasjon personRelasjon;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PersonMiljoe {

        private String miljoe;
        private Person person;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TpsPersonMiljoe {

        private String miljoe;
        private S610PersonType person;
    }
}
