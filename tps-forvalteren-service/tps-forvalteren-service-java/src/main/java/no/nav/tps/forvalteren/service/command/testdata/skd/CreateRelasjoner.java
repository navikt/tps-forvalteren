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
    private SkdMessageSenderTrans1 skdMessageSenderTrans1;

    @Autowired
    private PersistBarnTransRecordsToTps persistBarnTransRecordsToTps;

    public List<String> execute(List<Person> personerSomIkkeEksitererITpsMiljoe, boolean addHeader) {
        List<Person> personerMedRelasjoner = getPersonerMedRelasjoner(personerSomIkkeEksitererITpsMiljoe);
        List<String> skdMeldinger = new ArrayList<>();
        for (Person person : personerMedRelasjoner) {
            List<Relasjon> personRelasjoner = relasjonRepository.findByPersonId(person.getId());
            boolean hasBarn = false;
            for (Relasjon relasjon : personRelasjoner) {
                String skdMeldingNavn = getSkdMeldingNavn(relasjon);
                if ("Vigsel".equals(skdMeldingNavn)) {
                    skdMeldinger.addAll(skdMessageSenderTrans1.execute(skdMeldingNavn, Arrays.asList(person), addHeader));
                } else if ("Familieendring".equals(skdMeldingNavn)) {
                    hasBarn = true;
                }
            }
            if (hasBarn) {
                skdMeldinger.addAll(persistBarnTransRecordsToTps.execute(person, addHeader));
            }
        }
        return skdMeldinger;
    }

    private String getSkdMeldingNavn(Relasjon relasjon) {
        switch (relasjon.getRelasjonTypeNavn()) {
        case "EKTEFELLE":
            return "Vigsel";
        case "BARN":
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
