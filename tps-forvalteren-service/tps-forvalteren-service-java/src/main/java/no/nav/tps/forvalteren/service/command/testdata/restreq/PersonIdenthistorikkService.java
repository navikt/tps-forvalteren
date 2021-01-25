package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsAliasRequest;
import no.nav.tps.forvalteren.domain.rs.RsAliasResponse;
import no.nav.tps.forvalteren.domain.rs.RsIdenthistorikkKriterium;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerOgSjekkMiljoeService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.skd.LagreTilTpsService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdentService;

@Service
public class PersonIdenthistorikkService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private HentKjoennFraIdentService hentKjoennFraIdentService;

    @Autowired
    private PersonNameService personNameService;

    @Autowired
    private OpprettPersonerOgSjekkMiljoeService opprettPersonerOgSjekkMiljoeService;

    @Autowired
    private IdenthistorikkService identhistorikkService;

    @Autowired
    private LagreTilTpsService lagreTilTpsService;

    @Autowired
    private MapperFacade mapperFacade;

    public RsAliasResponse prepareAliases(RsAliasRequest request) {

        Person mainPerson = personRepository.findByIdent(request.getIdent());
        if (isNull(mainPerson)) {
            throw new NotFoundException(format("Person med ident '%s' ble ikke funnet", request.getIdent()));
        }

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
                Person person = opprettPersonerOgSjekkMiljoeService.createNyeIdenter(prepareRequest(request.getIdent(), alias.getIdenttype())).get(0);
                personRepository.save(person);
                addToResponse(response, person, person.getIdent());
                identHistorikk.add(person);

            } else {
                Person person = personNameService.execute(new Person(), true);
                addToResponse(response, person, request.getIdent());
            }
        });

        if (!identHistorikk.isEmpty()) {
            Person person = identhistorikkService.save(request.getIdent(), identHistorikk, null);
            lagreTilTpsService.execute(singletonList(person), request.getEnvironments());
        }

        return response;
    }

    public void prepareIdenthistorikk(Person person, List<RsIdenthistorikkKriterium> identhistorikk, Boolean navSyntetiskIdent) {

        if (!identhistorikk.isEmpty()) {
            List<Person> dubletter = opprettPersonerOgSjekkMiljoeService.createNyeIdenter(
                    prepareRequest(person, identhistorikk, navSyntetiskIdent));

            personRepository.saveAll(dubletter);
            identhistorikkService.save(person.getIdent(), dubletter, identhistorikk);
        }
    }

    private void addToResponse(RsAliasResponse response, Person aliasPerson, String ident) {
        response.getAliaser().add(RsAliasResponse.Persondata.builder()
                .ident(ident)
                .fodselsdato(hentDatoFraIdentService.extract(ident))
                .navn(mapperFacade.map(aliasPerson, RsAliasResponse.Personnavn.class))
                .build());
    }

    private RsPersonKriteriumRequest prepareRequest(Person person, List<RsIdenthistorikkKriterium> identhistorikk,
            Boolean navSyntetiskIdent) {

        List<RsPersonKriterier> personkriterier = new ArrayList();

        identhistorikk.forEach(
                historikk -> {

                    LocalDateTime foedtFoer = nullcheckSetDefaultValue(historikk.getFoedtEtter(), hentDatoFraIdentService.extract(person.getIdent()));
                    foedtFoer = nonNull(historikk.getRegdato()) ? Stream.of(historikk.getRegdato(), foedtFoer).min(LocalDateTime::compareTo).get() : foedtFoer;
                    LocalDateTime foedtEtter = nullcheckSetDefaultValue(historikk.getFoedtEtter(), hentDatoFraIdentService.extract(person.getIdent()));
                    foedtEtter = foedtEtter.isAfter(foedtFoer) ? foedtFoer : foedtEtter;

                    personkriterier.add(RsPersonKriterier.builder()
                            .antall(1)
                            .identtype(nullcheckSetDefaultValue(historikk.getIdenttype(), person.getIdenttype()))
                            .foedtEtter(foedtEtter)
                            .foedtFoer(foedtFoer)
                            .kjonn(nullcheckSetDefaultValue(historikk.getKjonn(), hentKjoennFraIdentService.execute(person.getIdent())))
                            .harMellomnavn(true)
                            .navSyntetiskIdent(navSyntetiskIdent)
                            .build());
                });

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(personkriterier)
                .build();
    }

    private RsPersonKriteriumRequest prepareRequest(String ident, RsAliasRequest.IdentType identtype) {

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(
                        singletonList(RsPersonKriterier.builder()
                                .antall(1)
                                .identtype(identtype.name())
                                .foedtFoer(hentDatoFraIdentService.extract(ident))
                                .foedtEtter(hentDatoFraIdentService.extract(ident))
                                .kjonn(hentKjoennFraIdentService.execute(ident))
                                .harMellomnavn(true)
                                .build()))
                .build();
    }
}
