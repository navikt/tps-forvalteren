package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static com.google.common.collect.Sets.newHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolNewIdentsRequest;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligIMiljo;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

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

    public List<Person> createEksisterendeIdenter(RsPersonBestillingKriteriumRequest request) {

        Set<String> ledigeIdenterDB = findIdenterNotUsedInDB.filtrer(newHashSet(request.getOpprettFraIdenter()));

        Set<String> ledigeIdenterMiljo = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(ledigeIdenterDB, newHashSet(PROD_ENV));

        Set<String> ledigeIdenterKorrigert = identpoolService.whitelistAjustmentOfIdents(request.getOpprettFraIdenter(), ledigeIdenterDB, ledigeIdenterMiljo);

        List<Person> personer = opprettPersonerFraIdenter.execute(ledigeIdenterKorrigert);

        personer.forEach(person ->
                setNameOnPersonsService.execute(person, request.getHarMellomnavn())
        );

        return personer;
    }

    public List<Person> createNyeIdenter(RsPersonKriteriumRequest personKriterierListe) {

        List<String> nyeIdenter = new ArrayList();

        personKriterierListe.getPersonKriterierListe().forEach(kriterium -> {
            Set<String> identpoolIdents;
            Set<String> filteredDbIdents;
            int count = 5;
            try {
                do {
                    identpoolIdents = identpoolService.getAvailableIdents(mapperFacade.map(kriterium, IdentpoolNewIdentsRequest.class));
                    filteredDbIdents = findIdenterNotUsedInDB.filtrer(identpoolIdents);
                } while (identpoolIdents.size() - filteredDbIdents.size() > 0 && --count > 0);
                nyeIdenter.addAll(identpoolIdents);
                if (identpoolIdents.size() < kriterium.getAntall()) {
                    throw new TpsfTechnicalException("Ingen ledige identer funnet i miljø.");
                }
            } catch (ResourceAccessException e) {
                throw new TpsfTechnicalException("Identpool besvarte ikke forespørselen i tide", e);
            }
        });

        List<Person> personerSomSkalPersisteres = opprettPersonerFraIdenter.execute(nyeIdenter);

        for (int i = 0; i < personerSomSkalPersisteres.size(); i++) {

            if (personerSomSkalPersisteres.get(i).isDoedFoedt()) {

                personerSomSkalPersisteres.get(i).setDoedsdato(
                        hentDatoFraIdentService.extract(personerSomSkalPersisteres.get(i).getIdent()));
            } else {

                setNameOnPersonsService.execute(personerSomSkalPersisteres.get(i),
                        personKriterierListe.getPersonKriterierListe().size() >= i + 1 ?
                                personKriterierListe.getPersonKriterierListe().get(i).getHarMellomnavn() : null);
            }
        }

        return personerSomSkalPersisteres;
    }
}
