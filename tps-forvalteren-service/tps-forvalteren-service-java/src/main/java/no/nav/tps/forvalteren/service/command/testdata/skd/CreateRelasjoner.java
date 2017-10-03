package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

@Service
public class CreateRelasjoner {

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private SkdCreatePersoner skdCreatePersoner;

    @Autowired
    private SkdFelterContainerTrans1 skdFelterContainerTrans1;

    @Autowired
    private SkdFelterContainerTrans2 skdFelterContainerTrans2;

    public void execute(List<Person> personerSomIkkeEksitererITpsMiljoe, List<String> environments) {
        List<Person> personerMedRelasjoner = getPersonerMedRelasjoner(personerSomIkkeEksitererITpsMiljoe);

        for (Person person : personerMedRelasjoner) {
            List<Relasjon> personRelasjoner = relasjonRepository.findByPersonId(person.getId());
            for (Relasjon relasjon : personRelasjoner) {
                String skdMeldingNavn = getSkdMeldingNavn(relasjon);
                if (skdMeldingNavn.equals("Vigsel")) {
                    skdCreatePersoner.execute(skdMeldingNavn, Arrays.asList(person), environments, skdFelterContainerTrans1);
                } else if (skdMeldingNavn.equals("Familieendring")) {
                    skdCreatePersoner.execute(skdMeldingNavn, Arrays.asList(person), environments, skdFelterContainerTrans2);
                }
            }
        }

    }

    private String getSkdMeldingNavn(Relasjon relasjon) {
        switch (relasjon.getRelasjonTypeNavn()) {
        case "EKTEFELLE":
            return "Vigsel";
        case "MOR":
            return "Familieendring";
        case "FAR":
            return "Familieendring";
        default:
            return "";
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

}
