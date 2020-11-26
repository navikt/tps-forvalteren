package no.nav.tps.forvalteren.service.command.testdata.restreq;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Fullmakt;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerOgSjekkMiljoeService;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.ExtractOpprettKriterier.extractMainPerson;

@Service
@RequiredArgsConstructor
public class FullmaktService {

    private final OpprettPersonerOgSjekkMiljoeService opprettPersonerOgSjekkMiljoeService;
    private final MapperFacade mapperFacade;

    public void opprettFullmakt(RsPersonBestillingKriteriumRequest request, List<Person> hovedPersoner) {

        if (nonNull(request.getFullmakt())) {

            RsPersonBestillingKriteriumRequest fullmaktRequest = new RsPersonBestillingKriteriumRequest();
            fullmaktRequest.setIdenttype(request.getFullmakt().getIdentType());
            fullmaktRequest.setHarMellomnavn(request.getFullmakt().getHarMellomnavn());
            fullmaktRequest.setAntall(hovedPersoner.size());
            Iterator<Person> fullmektigIterator =
                    opprettPersonerOgSjekkMiljoeService.createNyeIdenter(extractMainPerson(fullmaktRequest)).iterator();

            hovedPersoner.forEach(person -> {
                Fullmakt fullmakt = mapperFacade.map(request.getFullmakt(), Fullmakt.class);
                fullmakt.setFullmektig(fullmektigIterator.next());
                mapperFacade.map(request, fullmakt.getFullmektig());
                fullmakt.setPerson(person);
                person.getFullmakt().add(fullmakt);
            });
        }
    }
}
