package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S610HentGT.PERSON_KERNINFO_SERVICE_ROUTINE;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAarsakskode43.DOEDSMELDING_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAnnulleringAarsakskode45.DOEDSMELDINGANNULLERING_MLD_NAVN;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.service.command.foedselsmelding.AdresserResponse;
import no.nav.tps.forvalteren.service.command.testdata.SaveDoedsmeldingToDB;
import no.nav.tps.forvalteren.service.command.testdata.TpsServiceroutineFnrRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonAdresseService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;

@Service
public class CreateDoedsmeldinger {

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Autowired
    private TpsServiceRoutineService tpsServiceRoutineService;

    @Autowired
    private DoedsmeldingRepository doedsmeldingRepository;

    @Autowired
    private TpsServiceroutineFnrRequest tpsFnrRequest;

    @Autowired
    private PersonAdresseService personAdresseService;

    @Autowired
    private SaveDoedsmeldingToDB saveDoedsmeldingToDB;

    public List<SkdMeldingTrans1> execute(List<Person> personerIGruppen, Set<String> environments, boolean addHeader) {

        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();

        personerIGruppen.forEach(person -> {
            Doedsmelding doedsmelding = doedsmeldingRepository.findByPersonId(person.getId());
            if (nonNull(person.getDoedsdato()) || nonNull(doedsmelding)) {

                environments.forEach(environment -> {

                    LocalDate tpsDoedsdato = getTpsDoedsdato(person, environment);

                    if (isSendAnnuleringsmelding(person, tpsDoedsdato)) {

                        findLastAddress(person, tpsDoedsdato, environment);
                        skdMeldinger.addAll(skdMessageCreatorTrans1.execute(DOEDSMELDINGANNULLERING_MLD_NAVN, singletonList(person), addHeader));

                    }
                    if (isSendDoedsmelding(person, tpsDoedsdato)) {

                        skdMeldinger.addAll(skdMessageCreatorTrans1.execute(DOEDSMELDING_MLD_NAVN, singletonList(person), addHeader));
                    }
                });

                saveDoedsmeldingToDB.execute(singletonList(person));
            }
        });

        return skdMeldinger;
    }

    private static boolean isSendAnnuleringsmelding(Person person, LocalDate tpsDoedsdato) {

        return nonNull(tpsDoedsdato) && !tpsDoedsdato.equals(person.getDoedsdato());
    }

    private static boolean isSendDoedsmelding(Person person, LocalDate tpsDoedsdato) {

        return nonNull(person.getDoedsdato()) && !person.getDoedsdato().equals(tpsDoedsdato);
    }

    private LocalDate getTpsDoedsdato(Person person, String environment) {

        TpsServiceRoutineResponse response = tpsServiceRoutineService.execute(PERSON_KERNINFO_SERVICE_ROUTINE,
                tpsFnrRequest.buildRequest(person, environment), true);
        String tpsDoedsdatoString =
                (String) ((Map) ((Map) response.getResponse()).get("data1")).get("datoDo");

        return nonNull(tpsDoedsdatoString) ? LocalDate.parse(tpsDoedsdatoString) : null;
    }

    private void findLastAddress(Person person, LocalDate doedsdato, String miljoe) {

        AdresserResponse adresser = personAdresseService.hentAdresserForDato(person.getIdent(), doedsdato.atStartOfDay().minusDays(1), miljoe);

        if (nonNull(adresser)) {
            if (nonNull(adresser.getBoadresse())) {
                adresser.getBoadresse().setPerson(person);
                person.setBoadresse(asList(adresser.getBoadresse()));
            }

            if (nonNull(adresser.getPostadresse())) {
                adresser.getPostadresse().setPerson(person);
                person.setPostadresse(asList(adresser.getPostadresse()));
            }
        }
    }
}
