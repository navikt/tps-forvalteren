package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static com.google.common.collect.Sets.newHashSet;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolNewIdentsRequest;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.skd.KjoennType;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligIMiljo;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpprettPersonerOgSjekkMiljoeService {

    public static final String PROD_ENV = "p";

    private final FindIdenterNotUsedInDB findIdenterNotUsedInDB;
    private final FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;
    private final PersonNameService setNameOnPersonsService;
    private final OpprettPersonerService opprettPersonerFraIdenter;
    private final IdentpoolService identpoolService;
    private final HentDatoFraIdentService hentDatoFraIdentService;
    private final MapperFacade mapperFacade;
    private final PersonRepository personRepository;

    public List<Person> createEksisterendeIdenter(RsPersonBestillingKriteriumRequest request) {

        Set<String> ledigeIdenterDB = findIdenterNotUsedInDB.filtrer(newHashSet(request.getOpprettFraIdenter()));

        Set<String> ledigeIdenterMiljo = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(ledigeIdenterDB, newHashSet(PROD_ENV));

        Set<String> ledigeIdenterKorrigert = identpoolService.whitelistAjustmentOfIdents(request.getOpprettFraIdenter(), ledigeIdenterDB, ledigeIdenterMiljo);

        List<Person> personer = opprettPersonerFraIdenter.execute(ledigeIdenterKorrigert);

        personer.forEach(person -> {
            setNameOnPersonsService.execute(person, request.getHarMellomnavn());
            person.setNyPerson(true);
        });

        return personer;
    }

    public List<Person> createNyeIdenter(RsPersonKriteriumRequest personKriterierListe) {

        List<IdentKjonnTuple> nyeIdenter = new ArrayList<>();

        AtomicReference<String> uuid = new AtomicReference<>();
        AtomicLong startTime = new AtomicLong();
        personKriterierListe.getPersonKriterierListe().forEach(kriterium -> {
            Set<String> identpoolIdents;
            Set<String> filteredDbIdents;
            int count = 5;
            try {
                do {
                    uuid.set(UUID.randomUUID().toString());
                    startTime.set(System.currentTimeMillis());
                    log.info("Identpool kalles med id {} og detaljer {}", uuid.get(), kriterium.toString());
                    identpoolIdents = identpoolService.getAvailableIdents(mapperFacade.map(kriterium, IdentpoolNewIdentsRequest.class));
                    filteredDbIdents = findIdenterNotUsedInDB.filtrer(identpoolIdents);
                } while (identpoolIdents.size() - filteredDbIdents.size() > 0 && --count > 0);
                nyeIdenter.addAll(identpoolIdents.stream()
                        .map(ident -> IdentKjonnTuple.builder()
                                .ident(ident)
                                .kjonn(kriterium.getKjonn())
                                .build())
                        .collect(Collectors.toList()));
                if (identpoolIdents.size() < kriterium.getAntall()) {
                    throw new TpsfTechnicalException("Ingen ledige identer funnet i miljø.");
                }
            } catch (ResourceAccessException e) {
                log.info("Kall til identpool med id {} feilet etter {} ms", uuid.get(), System.currentTimeMillis() - startTime.get());
                throw new TpsfTechnicalException(format("Identpool besvarte ikke forespørselen etter %d ms",
                        System.currentTimeMillis() - startTime.get()), e);
            } catch (HttpClientErrorException e) {
                throw new TpsfFunctionalException(e.getMessage(), e);
            }
        });

        List<Person> personerSomSkalPersisteres = opprettPersonerFraIdenter.opprettMedEksplisittKjoenn(nyeIdenter);

        for (int i = 0; i < personerSomSkalPersisteres.size(); i++) {

            personerSomSkalPersisteres.get(i).setNyPerson(true);
            if (personerSomSkalPersisteres.get(i).isDoedFoedt()) {

                personerSomSkalPersisteres.get(i).setDoedsdato(
                        hentDatoFraIdentService.extract(personerSomSkalPersisteres.get(i).getIdent()));
            } else {

                setNameOnPersonsService.execute(personerSomSkalPersisteres.get(i),
                        personKriterierListe.getPersonKriterierListe().size() >= i + 1 ?
                                personKriterierListe.getPersonKriterierListe().get(i).getHarMellomnavn() : null);
            }
        }

        personRepository.saveAll(personerSomSkalPersisteres);
        return personerSomSkalPersisteres;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdentKjonnTuple {

        private String ident;
        private KjoennType kjonn;
    }
}
