package no.nav.tps.forvalteren.service.command.testdata.skd;

import static com.google.common.collect.Lists.newArrayList;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.BarnForeldrerelasjonEndringAarsakskode98.BARN_FORELDRE_RELASJON_ENDRING;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.Familieendring.FAMILIEENDRING_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.VigselAarsakskode11.VIGSEL_MLD_NAVN;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

@Service
@RequiredArgsConstructor
public class CreateRelasjoner {

    private final RelasjonRepository relasjonRepository;
    private final SkdMessageCreatorTrans1 skdMessageCreatorTrans1;
    private final PersistBarnTransRecordsToTps persistBarnTransRecordsToTps;

    public List<SkdMelding> execute(List<Person> personerMedRelasjoner, boolean addHeader) {

        List<SkdMelding> skdMeldinger = new ArrayList<>();
        for (Person person : personerMedRelasjoner) {
            List<Relasjon> personRelasjoner = relasjonRepository.findByPersonId(person.getId());
            boolean hasBarn = false;
            boolean hasForelder = false;
            for (Relasjon relasjon : personRelasjoner) {
                String skdMeldingNavn = getSkdMeldingNavn(relasjon);
                if (VIGSEL_MLD_NAVN.equals(skdMeldingNavn) && relasjon.getPersonRelasjonMed().getSivilstander().isEmpty()) {
                    skdMeldinger.addAll(skdMessageCreatorTrans1.execute(skdMeldingNavn, newArrayList(person), addHeader));
                } else if (FAMILIEENDRING_MLD_NAVN.equals(skdMeldingNavn)) {
                    hasBarn = true;
                } else if (BARN_FORELDRE_RELASJON_ENDRING.equals(skdMeldingNavn)) {
                    hasForelder = true;
                }
            }
            if (hasBarn) {
                skdMeldinger.addAll(persistBarnTransRecordsToTps.execute(person, addHeader));
            }
            if (hasForelder) {
                skdMeldinger.addAll(skdMessageCreatorTrans1.execute(BARN_FORELDRE_RELASJON_ENDRING, newArrayList(person), addHeader));
            }
        }
        return skdMeldinger;
    }

    private String getSkdMeldingNavn(Relasjon relasjon) {
        switch (relasjon.getRelasjonTypeNavn()) {
        case "EKTEFELLE":
            return VIGSEL_MLD_NAVN;
        case "BARN":
        case "FOEDSEL":
            return FAMILIEENDRING_MLD_NAVN;
        case "MOR":
        case "FAR":
            return BARN_FORELDRE_RELASJON_ENDRING;
        default:
            return "";
        }
    }
}
