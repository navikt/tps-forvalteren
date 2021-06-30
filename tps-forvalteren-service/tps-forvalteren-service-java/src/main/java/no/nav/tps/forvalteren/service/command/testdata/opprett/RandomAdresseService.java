package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.rs.AdresseNrInfo.AdresseNr.KOMMUNENR;
import static no.nav.tps.forvalteren.domain.rs.AdresseNrInfo.AdresseNr.POSTNR;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.consumer.rs.adresser.AdresseServiceConsumer;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RandomAdresseService {

    private final AdresseServiceConsumer adresseServiceConsumer;
    private final HentDatoFraIdentService hentDatoFraIdentService;
    private final DummyAdresseService dummyAdresseService;
    private final MapperFacade mapperFacade;

    public List<Person> execute(List<Person> persons, AdresseNrInfo adresseNrInfo) {

        var adresser = hentRandomAdresse(persons.size(), adresseNrInfo);

        for (var i = 0; i < persons.size(); i++) {
            adresser.get(i).setFlyttedato(hentDatoFraIdentService.extract(persons.get(i).getIdent()));
            adresser.get(i).setPerson(persons.get(i));
            adresser.get(i).setFlyttedato(LocalDateTime.now());
            persons.get(i).getBoadresse().add(adresser.get(i));
        }

        return persons;
    }

    public List<Adresse> hentRandomAdresse(int total, AdresseNrInfo adresseNrInfo) {

        try {
            var addrResponse = adresseServiceConsumer.getAdresser(

                    new StringBuilder()
                    .append(KOMMUNENR.equals(adresseNrInfo.getNummertype()) ?  "kommunenummer=" : "")
                    .append(KOMMUNENR.equals(adresseNrInfo.getNummertype()) ?
                            adresseNrInfo.getNummer() : "")
                    .append(POSTNR.equals(adresseNrInfo.getNummertype()) ? "postnummer=" : "")
                    .append(POSTNR.equals(adresseNrInfo.getNummertype()) ?
                            adresseNrInfo.getNummer() : "")
                    .toString(), total);

            return mapperFacade.mapAsList(addrResponse, Adresse.class);

        } catch (RuntimeException e) {
            log.error("Adresseoppslag feilet for søk med {}", nonNull(adresseNrInfo) ? adresseNrInfo.toString() : null, e.getMessage());
            return singletonList(dummyAdresseService.createDummyBoAdresse(null));
        }
    }

    public List<Person> execute(List<Person> persons) {

        return execute(persons, null);
    }
}
