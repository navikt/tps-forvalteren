package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.ExtractOpprettKriterier.extractMainPerson;

import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerOgSjekkMiljoeService;

@Service
@RequiredArgsConstructor
public class VergemaalService {

    private final OpprettPersonerOgSjekkMiljoeService opprettPersonerOgSjekkMiljoeService;
    private final MapperFacade mapperFacade;

    public void opprettVerge(RsPersonBestillingKriteriumRequest request, List<Person> hovedPersoner) {

        if (nonNull(request.getVergemaal())) {

            RsPersonBestillingKriteriumRequest vergeRequest = new RsPersonBestillingKriteriumRequest();
            vergeRequest.setIdenttype(request.getVergemaal().getIdentTypeVerge());
            vergeRequest.setAntall(hovedPersoner.size());
            Iterator<Person> vergeIterator =
                    opprettPersonerOgSjekkMiljoeService.createNyeIdenter(extractMainPerson(vergeRequest)).iterator();

            hovedPersoner.forEach(person -> {
                Vergemaal vergemaal = mapperFacade.map(request.getVergemaal(), Vergemaal.class);
                vergemaal.setVerge(vergeIterator.next());
                mapperFacade.map(request, vergemaal.getVerge());
                vergemaal.setPerson(person);
                person.getVergemaal().add(vergemaal);
            });
        }
    }
}
