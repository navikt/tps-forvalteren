package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Objects.nonNull;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.rs.RsPostadresse;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
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

        updateAdresse(person, request);

        return personRepository.save(person);
    }

    private void updateAdresse(Person person, RsPersonBestillingKriteriumRequest request) {

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
                person.getBoadresse().add(adresse);
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
                    person.getPostadresse().add(postadresse);
                }
            }
        }
    }
}
