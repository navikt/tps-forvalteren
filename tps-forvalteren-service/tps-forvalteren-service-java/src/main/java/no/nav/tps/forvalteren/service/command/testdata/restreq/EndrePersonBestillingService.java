package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.rs.RsPostadresse;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;

@Service
@Transactional
@RequiredArgsConstructor
public class EndrePersonBestillingService {

    private final PersonRepository personRepository;
    private final RandomAdresseService randomAdresseService;
    private final MapperFacade mapperFacade;

    public Person execute(String ident, RsPersonBestillingKriteriumRequest request) {

        Person person = personRepository.findByIdent(ident);

        if (isNull(person)) {
            throw new TpsfFunctionalException(format("Person med ident %s ble ikke funnet", ident));
        }
        updateAdresse(request, person);
        updateStatsborgerskap(request, person);

        return personRepository.save(person);
    }

    private void updateStatsborgerskap(RsPersonBestillingKriteriumRequest request, Person person) {

        if (isNotBlank(request.getStatsborgerskap())) {

            person.getStatsborgerskap().add(Statsborgerskap.builder()
                    .statsborgerskap(request.getStatsborgerskap())
                    .statsborgerskapRegdato(nullcheckSetDefaultValue(request.getStatsborgerskapRegdato(), now()))
                    .person(person)
                    .build());
        }
    }

    private void updateAdresse(RsPersonBestillingKriteriumRequest request, Person person) {

        if (nonNull(request.getAdresseNrInfo()) || nonNull(request.getBoadresse())) {

            Adresse adresse = (nonNull(request.getAdresseNrInfo()) ?
                    randomAdresseService.hentRandomAdresse(1, request.getAdresseNrInfo()).get(0) : mapperFacade.map(request.getBoadresse(), Adresse.class)).toUppercase();

            AtomicBoolean found = new AtomicBoolean(false);
            person.getBoadresse().forEach(boadresse -> {
                if (adresse.equals(boadresse)) {
                    found.set(true);
                }
            });
            if (!found.get()) {
                adresse.setPerson(person);
                if (isNull(adresse.getFlyttedato())) {
                    adresse.setFlyttedato(now().minusYears(1));
                }
                person.getBoadresse().add(adresse);
                person.setGtVerdi(null); // Triggers reload of TKNR
            }
        }

        if (!request.getPostadresse().isEmpty()) {

            for (RsPostadresse postadresseRequest : request.getPostadresse()) {

                boolean found = false;
                Postadresse postadresse = mapperFacade.map(postadresseRequest, Postadresse.class).toUppercase();

                for (Postadresse postadressePerson : person.getPostadresse()) {
                    if (postadresse.equals(postadressePerson)) {
                        found = true;
                    }
                }

                if (!found) {
                    postadresse.setPerson(person);
                    person.getPostadresse().add(postadresse);
                }
            }
        }
    }
}
