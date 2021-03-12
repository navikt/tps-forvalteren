package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static no.nav.tps.ctg.s610.domain.RelasjonType.EKTE;
import static no.nav.tps.ctg.s610.domain.RelasjonType.ENKE;
import static no.nav.tps.ctg.s610.domain.RelasjonType.GJPA;
import static no.nav.tps.ctg.s610.domain.RelasjonType.GLAD;
import static no.nav.tps.ctg.s610.domain.RelasjonType.REPA;
import static no.nav.tps.ctg.s610.domain.RelasjonType.SEPA;
import static no.nav.tps.ctg.s610.domain.RelasjonType.SEPR;
import static no.nav.tps.ctg.s610.domain.RelasjonType.SKIL;
import static no.nav.tps.ctg.s610.domain.RelasjonType.SKPA;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FAR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.MOR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.PARTNER;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT.PERSON_KERNINFO_SERVICE_ROUTINE;
import static no.nav.tps.forvalteren.service.command.tps.transformation.response.S610PersonMappingStrategy.getSivilstand;
import static no.nav.tps.forvalteren.service.command.tps.transformation.response.S610PersonMappingStrategy.getTimestamp;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.ctg.s610.domain.RelasjonType;
import no.nav.tps.ctg.s610.domain.S610PersonType;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.jpa.Sivilstand;
import no.nav.tps.forvalteren.domain.rs.dolly.ImporterPersonLagreRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.ImporterPersonRequest;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImporterPersonService {

    private static final String MILJOE_IKKE_FUNNET = "Feilet å lese fra miljoe {}";

    private static final String STATUS = "status";
    private static final String STATUS_OK = "00";
    private static final String STATUS_WARN = "04";

    private final PersonRepository personRepository;
    private final TpsServiceRoutineService tpsServiceRoutineService;
    private final GetEnvironments getEnvironments;
    private final ObjectMapper objectMapper;
    private final MapperFacade mapperFacade;

    public Map<String, Person> importFraTps(ImporterPersonRequest request) {

        Map<String, S610PersonType> miljoePerson = importFromTps(request);

        Map<String, PersonRelasjon> relasjoner = getRelasjoner(miljoePerson);

        return !miljoePerson.isEmpty() ? buildMiljoePersonWithRelasjon(relasjoner) : emptyMap();
    }

    public Person importFraTpsOgLagre(ImporterPersonLagreRequest request) {

        if (nonNull(personRepository.findByIdent(request.getIdent()))) {
            throw new TpsfFunctionalException(format("Ident %s finnes allerede", request.getIdent()));
        }

        if (isBlank(request.getMiljoe())) {
            throw new TpsfTechnicalException("Miljoe må angis ved lagring");
        }

        Map<String, Person> personMiljoe = importFraTps(ImporterPersonRequest.builder()
                .ident(request.getIdent())
                .miljoe(Collections.singleton(request.getMiljoe()))
                .build());

        if (!personRepository.findByIdentIn(personMiljoe.values().stream()
                .map(Person::getIdent)
                .collect(Collectors.toList())).isEmpty()) {
            throw new TpsfFunctionalException(format("Ident %s har relasjon(er) som finnes allerede", request.getIdent()));
        }

        if (!personMiljoe.isEmpty()) {
            Person person = personMiljoe.values().iterator().next();

            List<PersonRelasjonDiv> personRelasjoner = detachReleasjoner(person);
            personRelasjoner.forEach(person1 -> savePerson(person1.getPerson()));

            attachReleasjoner(personRelasjoner);
            personRelasjoner.forEach(person1 -> savePerson(person1.getPerson()));

            return person;

        } else {
            throw new TpsfFunctionalException(
                    format("Person med ident %s ble ikke funnet i miljoe %s", request.getIdent(), request.getMiljoe()));
        }
    }

    private void attachReleasjoner(List<PersonRelasjonDiv> personRelasjoner) {

        personRelasjoner.forEach(personRelasjon -> {
            personRelasjon.getPerson().setSivilstander(personRelasjon.getSivilstander());
            personRelasjon.getPerson().setRelasjoner(personRelasjon.getRelasjoner());
        });
    }

    private List<PersonRelasjonDiv> detachReleasjoner(Person person) {

        List<Person> personer = new ArrayList<>();
        Stream.of(singletonList(person), person.getRelasjoner().stream()
                .map(Relasjon::getPersonRelasjonMed)
                .collect(Collectors.toList())).forEach(personer::addAll);

        List<PersonRelasjonDiv> personRelasjoner = personer.stream().map(person1 ->
                PersonRelasjonDiv.builder()
                        .person(person1)
                        .relasjoner(person1.getRelasjoner())
                        .sivilstander(person1.getSivilstander())
                        .build())
                .collect(Collectors.toList());

        personRelasjoner.forEach(personRelasjon -> {
            personRelasjon.getPerson().setSivilstander(null);
            personRelasjon.getPerson().setRelasjoner(null);
        });

        return personRelasjoner;
    }

    private Map<String, Person> buildMiljoePersonWithRelasjon(Map<String, PersonRelasjon> personRelasjon) {

        return personRelasjon.entrySet().parallelStream()
                .map(entry -> PersonMiljoe.builder()
                        .miljoe(entry.getKey())
                        .person(buildPersonWithRelasjon(entry.getValue()))
                        .build())
                .collect(Collectors.toMap(PersonMiljoe::getMiljoe, PersonMiljoe::getPerson));
    }

    private Person buildPersonWithRelasjon(PersonRelasjon personRelasjon) {

        List<S610PersonType> tpsFamilie = new ArrayList<>();
        Stream.of(singletonList(personRelasjon.getHovedperson()), personRelasjon.getRelasjoner()).forEach(tpsFamilie::addAll);

        Map<String, Person> familie = tpsFamilie.parallelStream()
                .map(person -> mapperFacade.map(person, Person.class))
                .collect(Collectors.toMap(Person::getIdent, person -> person));

        tpsFamilie.forEach(person ->
                familie.get(person.getFodselsnummer()).getRelasjoner().addAll(
                        nonNull(person.getBruker().getRelasjoner()) ?
                                person.getBruker().getRelasjoner().getRelasjon().stream()
                                        .filter(relasjon ->
                                                tpsFamilie.stream().anyMatch(person1 ->
                                                        relasjon.getFnrRelasjon().equals(person1.getFodselsnummer())))
                                        .map(relasjon -> Relasjon.builder()
                                                .relasjonTypeNavn(mapRelasjonType(relasjon.getTypeRelasjon()))
                                                .personRelasjonMed(familie.get(relasjon.getFnrRelasjon()))
                                                .person(familie.get(person.getFodselsnummer()))
                                                .build())
                                        .collect(Collectors.toList()) : emptyList()));

        mapSivilstand(tpsFamilie, familie);

        return familie.get(personRelasjon.getHovedperson().getFodselsnummer());
    }

    private void mapSivilstand(List<S610PersonType> tpsFamilie, Map<String, Person> familie) {

        tpsFamilie.forEach(person ->
                familie.get(person.getFodselsnummer()).getSivilstander().addAll(
                        nonNull(person.getBruker().getRelasjoner()) &&
                                person.getBruker().getRelasjoner().getRelasjon().stream()
                                        .anyMatch(relasjon -> isGift(relasjon.getTypeRelasjon())) ?
                                singletonList(Sivilstand.builder()
                                        .sivilstand(getSivilstand(person))
                                        .sivilstandRegdato(getTimestamp(person.getSivilstandDetalj().getDatoSivilstand()))
                                        .person(familie.get(person.getFodselsnummer()))
                                        .personRelasjonMed(familie.get(person.getBruker().getRelasjoner().getRelasjon().stream()
                                                .filter(relasjon -> isGift(relasjon.getTypeRelasjon()))
                                                .findFirst().get().getFnrRelasjon()))
                                        .build()) :
                                emptyList()));
    }

    private static boolean isGift(RelasjonType relasjonType) {

        return EKTE == relasjonType ||
                ENKE == relasjonType ||
                SKIL == relasjonType ||
                SEPR == relasjonType ||
                REPA == relasjonType ||
                SEPA == relasjonType ||
                SKPA == relasjonType ||
                GJPA == relasjonType ||
                GLAD == relasjonType;
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
                getEnvironments.getEnvironments();

        return environments.parallelStream()
                .map(env -> TpsPersonMiljoe.builder()
                        .miljoe(env)
                        .person(readFromTps(request.getIdent(), env).getPerson())
                        .build())
                .filter(tpsPerson -> nonNull(tpsPerson.getPerson()) && isNotBlank(tpsPerson.getPerson().getFodselsnummer()))
                .collect(Collectors.toMap(TpsPersonMiljoe::getMiljoe, TpsPersonMiljoe::getPerson));
    }

    private TpsPersonMiljoe readFromTps(String ident, String environment) {

        try {
            TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_KERNINFO_SERVICE_ROUTINE,
                    buildRequest(ident, environment), true);

            if (isStatusOK(((ResponseStatus) ((Map) response.getResponse()).get(STATUS)))) {
                return TpsPersonMiljoe.builder()
                        .person(objectMapper.convertValue(((Map) response.getResponse()).get("data1"), S610PersonType.class))
                        .miljoe(environment)
                        .build();
            } else {
                return TpsPersonMiljoe.builder()
                        .errorMsg(((ResponseStatus) ((Map) response.getResponse()).get(STATUS)).getUtfyllendeMelding())
                        .miljoe(environment)
                        .build();
            }

        } catch (Exception e) {
            log.error(MILJOE_IKKE_FUNNET, environment, e);
            return TpsPersonMiljoe.builder()
                    .errorMsg(e.getMessage())
                    .miljoe(environment)
                    .build();
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
                                .map(relasjon -> readFromTps(relasjon.getFnrRelasjon(), miljoe).getPerson())
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList()) :
                        emptyList())
                .hovedperson(tpsPerson)
                .build();
    }

    private static Map buildRequest(String ident, String environment) {
        Map<String, Object> params = new HashMap<>();
        params.put("fnr", ident);
        params.put("aksjonsKode", "D1");
        params.put("environment", environment);
        return params;
    }

    private static String mapRelasjonType(RelasjonType relasjonType) {

        switch (relasjonType) {
        case MORA:
            return MOR.name();
        case FARA:
            return FAR.name();
        case EKTE:
        case ENKE:
        case SKIL:
        case SEPR:
        case REPA:
        case SEPA:
        case SKPA:
        case GJPA:
        case GLAD:
            return PARTNER.name();
        default:
            return relasjonType.name();
        }
    }

    private static boolean isStatusOK(ResponseStatus status) {
        return STATUS_OK.equals(status.getKode()) || STATUS_WARN.equals(status.getKode());
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PersonRelasjon {

        private S610PersonType hovedperson;
        private List<S610PersonType> relasjoner;
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
        private String errorMsg;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PersonRelasjonDiv {

        private Person person;
        private List<Sivilstand> sivilstander;
        private List<Relasjon> relasjoner;
    }
}