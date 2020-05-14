package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT.PERSON_KERNINFO_SERVICE_ROUTINE;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAarsakskode43.DOEDSMELDING_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAnnulleringAarsakskode45.DOEDSMELDINGANNULLERING_MLD_NAVN;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.service.command.testdata.TpsServiceroutineFnrRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;

@Service
@RequiredArgsConstructor
public class CreateDoedsmeldinger {

    private final SkdMessageCreatorTrans1 skdMessageCreatorTrans1;
    private final TpsServiceRoutineService tpsServiceRoutineService;
    private final DoedsmeldingRepository doedsmeldingRepository;
    private final TpsServiceroutineFnrRequest tpsFnrRequest;

    public List<SkdMeldingTrans1> execute(List<Person> personerIGruppen, String environment, boolean addHeader) {

        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();
        personerIGruppen.forEach(person -> {

            Doedsmelding doedsmelding = doedsmeldingRepository.findByPersonId(person.getId());
            if (isNotBlank(environment)) {
                skdMeldinger.addAll(buildDoedsmeldinger(person, doedsmelding, environment, addHeader));

            } else if (nonNull(person.getDoedsdato()) && isNull(doedsmelding)) { // Enables backwards compatibility
                skdMeldinger.addAll(skdMessageCreatorTrans1.execute(DOEDSMELDING_MLD_NAVN, singletonList(person), addHeader));
            }
        });

        return skdMeldinger;
    }

    public void updateDoedsmeldingRepository(List<Person> personer) {

        personer.forEach(person -> {
            Doedsmelding doedsmelding = doedsmeldingRepository.findByPersonId(person.getId());
            if (nonNull(person.getDoedsdato()) && isNull(doedsmelding)) {
                doedsmeldingRepository.save(Doedsmelding.builder()
                        .person(person)
                        .isMeldingSent(true)
                        .build());

            } else if (isNull(person.getDoedsdato()) && nonNull(doedsmelding)) {
                doedsmeldingRepository.deleteByPersonIdIn(singletonList(person.getId()));
            }
        });
    }

    private List<SkdMeldingTrans1> buildDoedsmeldinger(Person person, Doedsmelding doedsmelding, String environment, boolean addHeader) {

        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();
        if (nonNull(person.getDoedsdato()) || nonNull(doedsmelding)) {

            LocalDate tpsDoedsdato = getTpsDoedsdato(person, environment);

            if (isSendAnnuleringsmelding(person, tpsDoedsdato)) {
                skdMeldinger.addAll(skdMessageCreatorTrans1.execute(DOEDSMELDINGANNULLERING_MLD_NAVN, singletonList(person), addHeader));

            }
            if (isSendDoedsmelding(person, tpsDoedsdato)) {
                skdMeldinger.addAll(skdMessageCreatorTrans1.execute(DOEDSMELDING_MLD_NAVN, singletonList(person), addHeader));
            }
        }
        return skdMeldinger;
    }

    private static boolean isSendAnnuleringsmelding(Person person, LocalDate tpsDoedsdato) {

        return nonNull(tpsDoedsdato) && (isNull(person.getDoedsdato()) ||
                !tpsDoedsdato.equals(person.getDoedsdato().toLocalDate()));
    }

    private static boolean isSendDoedsmelding(Person person, LocalDate tpsDoedsdato) {

        return nonNull(person.getDoedsdato()) && !person.getDoedsdato().toLocalDate().equals(tpsDoedsdato);
    }

    private LocalDate getTpsDoedsdato(Person person, String environment) {

        TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_KERNINFO_SERVICE_ROUTINE,
                tpsFnrRequest.buildRequest(person, environment), true);
        String tpsDoedsdatoString =
                (String) ((Map) ((Map) response.getResponse()).get("data1")).get("datoDo");

        return isNotBlank(tpsDoedsdatoString) ? LocalDate.parse(tpsDoedsdatoString) : null;
    }
}
