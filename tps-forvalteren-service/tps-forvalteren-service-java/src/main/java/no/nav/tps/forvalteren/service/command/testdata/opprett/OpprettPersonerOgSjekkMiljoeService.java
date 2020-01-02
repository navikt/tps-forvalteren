package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static org.assertj.core.util.Sets.newHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import com.google.common.collect.Sets;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolNewIdentsRequest;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligIMiljo;

@Service
public class OpprettPersonerOgSjekkMiljoeService {

    public static final String PROD_ENV = "p";

    @Autowired
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

    @Autowired
    private PersonNameService setNameOnPersonsService;

    @Autowired
    private OpprettPersonerService opprettPersonerFraIdenter;

    @Autowired
    private IdentpoolService identpoolService;

    @Autowired
    private MapperFacade mapperFacade;

    public List<Person> createEksisterendeIdenter(RsPersonBestillingKriteriumRequest request) {

        Set<String> ledigeIdenterDB = findIdenterNotUsedInDB.filtrer(newHashSet(request.getOpprettFraIdenter()));

        Set<String> ledigeIdenterMiljo = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(ledigeIdenterDB, Sets.newHashSet(PROD_ENV));

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

            setNameOnPersonsService.execute(personerSomSkalPersisteres.get(i),
                    personKriterierListe.getPersonKriterierListe().size() >= i+1 ?
                            personKriterierListe.getPersonKriterierListe().get(i).getHarMellomnavn() : null);
        }

        return personerSomSkalPersisteres;
    }
}
