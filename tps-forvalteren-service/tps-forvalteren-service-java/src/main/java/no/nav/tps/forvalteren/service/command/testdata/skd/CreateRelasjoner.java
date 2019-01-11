package no.nav.tps.forvalteren.service.command.testdata.skd;

import static com.google.common.collect.Lists.newArrayList;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.Familieendring.FAMILIEENDRING_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.VigselAarsakskode11.VIGSEL_MLD_NAVN;

import java.util.ArrayList;
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
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Autowired
    private PersistBarnTransRecordsToTps persistBarnTransRecordsToTps;

    public List<SkdMelding> execute(List<Person> personerSomIkkeEksitererITpsMiljoe, boolean addHeader) {
        List<Person> personerMedRelasjoner = getPersonerMedRelasjoner(personerSomIkkeEksitererITpsMiljoe);
        List<SkdMelding> skdMeldinger = new ArrayList<>();
        for (Person person : personerMedRelasjoner) {
            List<Relasjon> personRelasjoner = relasjonRepository.findByPersonId(person.getId());
            boolean hasBarn = false;
            for (Relasjon relasjon : personRelasjoner) {
                String skdMeldingNavn = getSkdMeldingNavn(relasjon);
                if (VIGSEL_MLD_NAVN.equals(skdMeldingNavn)) {
                    skdMeldinger.addAll(skdMessageCreatorTrans1.execute(skdMeldingNavn, newArrayList(person), addHeader));
                } else if (FAMILIEENDRING_MLD_NAVN.equals(skdMeldingNavn)) {
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
            return VIGSEL_MLD_NAVN;
        case "BARN":
            return FAMILIEENDRING_MLD_NAVN;
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
