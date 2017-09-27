package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligeIMiljo;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LagreTilTps {

    private static final String NAVN_INNVANDRINGSMELDING = "Innvandring";

    @Autowired
    private SkdCreatePersoner skdCreatePersoner;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private SkdCreateFamilierelasjoner skdCreateFamilierelasjoner;

    @Autowired
    private RelasjonRepository relasjonRepository;

    public void execute(Long gruppeId, List<String> environments) {
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        List<Person> personerIGruppen = gruppe.getPersoner();

        List<String> identer = ekstraherIdenterFraPersoner(personerIGruppen);
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(identer, new HashSet<>(environments));
        List<Person> personerSomIkkeEksitererITPSMiljoe = personerSomIkkeFinnesIMiljoe(identerSomIkkeFinnesiTPSiMiljoe, personerIGruppen);

        skdCreatePersoner.execute(NAVN_INNVANDRINGSMELDING, personerSomIkkeEksitererITPSMiljoe, environments);

        List<Person> personerMedRelasjoner = getPersonerMedRelasjoner(personerSomIkkeEksitererITPSMiljoe);

        // Relasjoner som benytter Trans1
        for (Person person : personerMedRelasjoner) {
            List<Relasjon> personRelasjoner = relasjonRepository.findByPersonId(person.getId());
            for (Relasjon relasjon : personRelasjoner) {
                String skdMeldingNavn = getSkdMeldingNavn(relasjon);
                if (skdMeldingNavn == null) {
                    continue;
                }
                skdCreatePersoner.execute(skdMeldingNavn, Arrays.asList(person), environments);
            }
        }

        // Relasjoner som benytter Trans2
        for (Person person : personerMedRelasjoner) {
            List<Relasjon> foreldreBarnRelasjoner = relasjonRepository.findByPersonAndRelasjonTypeNavn(person, "BARN");
            if (!foreldreBarnRelasjoner.isEmpty()) {
                skdCreateFamilierelasjoner.execute(person, foreldreBarnRelasjoner, environments);
            }
        }
    }

    private List<Person> personerSomIkkeFinnesIMiljoe(Set<String> identerSomIkkeFinnesiTPSiMiljoe, List<Person> personer) {
        List<Person> personerSomIkkeAlleredeFinnesIMiljoe = new ArrayList<>();
        for (Person person : personer) {
            if (identerSomIkkeFinnesiTPSiMiljoe.contains(person.getIdent())) {
                personerSomIkkeAlleredeFinnesIMiljoe.add(person);
            }
        }
        return personerSomIkkeAlleredeFinnesIMiljoe;
    }

    private String getSkdMeldingNavn(Relasjon relasjon) {
        switch (relasjon.getRelasjonTypeNavn()) {
        case "EKTEFELLE":
            return "Vigsel";
        default:
            return null;
        }
    }

    private List<Person> getPersonerMedRelasjoner(List<Person> personerTidligereLagret) {
        List<Person> personer = new ArrayList<>();
        for (Person person : personerTidligereLagret) {
            List<Relasjon> personRelasjoner = relasjonRepository.findByPersonId(person.getId());
            if (!personRelasjoner.isEmpty()) {
                personer.add(person);
            }
        }
        return personer;
    }

    private List<String> ekstraherIdenterFraPersoner(List<Person> personer) {
        List<String> identer = new ArrayList<>();
        for (Person person : personer) {
            identer.add(person.getIdent());
        }
        return identer;
    }
}
