package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsAliasRequest;
import no.nav.tps.forvalteren.domain.rs.RsAliasResponse;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.skd.LagreTilTpsService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdentService;

@Service
public class PersonAliasService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private HentKjoennFraIdentService hentKjoennFraIdentService;

    @Autowired
    private PersonNameService personNameService;

    @Autowired
    private PersonerBestillingService personerBestillingService;

    @Autowired
    private IdenthistorikkService identhistorikkService;

    @Autowired
    private LagreTilTpsService lagreTilTpsService;

    @Autowired
    private MapperFacade mapperFacade;

    public RsAliasResponse prepareAliases(RsAliasRequest request) {

        Person mainPerson = personRepository.findByIdent(request.getIdent());
        RsAliasResponse response = RsAliasResponse.builder()
                .hovedperson(RsAliasResponse.Persondata.builder()
                        .ident(mainPerson.getIdent())
                        .fodselsdato(hentDatoFraIdentService.extract(mainPerson.getIdent()))
                        .navn(mapperFacade.map(mainPerson, RsAliasResponse.Personnavn.class))
                        .build())
                .build();

        List<Person> identHistorikk = new ArrayList();

        request.getAliaser().forEach(alias -> {

            if (TRUE.equals(alias.getNyIdent())) {
                Person person = (personerBestillingService.createTpsfPersonFromRestRequest(prepareRequest(alias.getIdenttype(), request.getIdent()))).get(0);
                addToResponse(response, person, person.getIdent());
                identHistorikk.add(person);

            } else {
                Person person = personNameService.execute(new Person(), true);
                addToResponse(response, person, request.getIdent());
            }
        });

        if (!identHistorikk.isEmpty()) {
            Person person = identhistorikkService.save(request.getIdent(), identHistorikk);
            lagreTilTpsService.execute(singletonList(person), request.getEnvironments());
        }

        return response;
    }

    private void addToResponse(RsAliasResponse response, Person aliasPerson, String ident) {
        response.getAliaser().add(RsAliasResponse.Persondata.builder()
                .ident(ident)
                .fodselsdato(hentDatoFraIdentService.extract(ident))
                .navn(mapperFacade.map(aliasPerson, RsAliasResponse.Personnavn.class))
                .build());
    }

    private RsPersonBestillingKriteriumRequest prepareRequest(RsAliasRequest.IdentType identtype, String ident) {

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();

        request.setAntall(1);
        request.setIdenttype(identtype.name());
        request.setFoedtFoer(hentDatoFraIdentService.extract(ident));
        request.setFoedtEtter(hentDatoFraIdentService.extract(ident));
        request.setKjonn(hentKjoennFraIdentService.execute(ident));
        request.setRegdato(now());

        return request;
    }
}
