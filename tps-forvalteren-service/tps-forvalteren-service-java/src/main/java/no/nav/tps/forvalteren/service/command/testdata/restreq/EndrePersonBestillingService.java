package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Objects.nonNull;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonListService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;

@Service
@RequiredArgsConstructor
public class EndrePersonBestillingService {

    private final PersonRepository personRepository;
    private final SavePersonListService savePersonListService;
    private final RandomAdresseService randomAdresseService;
    private final MapperFacade mapperFacade;

    public Person execute(String ident, RsPersonBestillingKriteriumRequest request) {

        Person person = personRepository.findByIdent(ident);

        updateAdresse(person, request);
        return person;
    }

    private void updateAdresse(Person person, RsPersonBestillingKriteriumRequest request) {


        if (nonNull(request.getAdresseNrInfo()) || nonNull(request.getBoadresse())) {

            Adresse adresse = nonNull(request.getAdresseNrInfo()) ?
                    randomAdresseService.hentRandomAdresse(1, request.getAdresseNrInfo()).get(0) : mapperFacade.map(request.getBoadresse(), Adresse.class);

            person.getBoadresse().add(adresse);
        }
    }
}
