package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static org.assertj.core.util.Sets.newHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolNewIdentsRequest;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligIMiljo;

@Service
public class OpprettPersonerOgSjekkMiljoeService {

    private static final String PRODLIKE_ENV = "q0";

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

    public List<Person> createEksisterendeIdenter(List<String> eksisterendeIdenter) {

        Set<String> ledigeIdenterDB = findIdenterNotUsedInDB.filtrer(newHashSet(eksisterendeIdenter));

        Set<String> ledigeIdenterMiljo = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(ledigeIdenterDB, Sets.newHashSet(PRODLIKE_ENV));

        Set<String> ledigeIdenterKorrigert = identpoolService.whitelistAjustmentOfIdents(eksisterendeIdenter, ledigeIdenterDB, ledigeIdenterMiljo);

        List<Person> personer = opprettPersonerFraIdenter.execute(ledigeIdenterKorrigert);
        setNameOnPersonsService.execute(personer);

        return personer;
    }

    public List<Person> createNyeIdenter(RsPersonKriteriumRequest personKriterierListe) {

        List<String> nyeIdenter = new ArrayList();

        personKriterierListe.getPersonKriterierListe().forEach(kriterium -> {
            Set<String> identpoolIdents;
            Set<String> filteredDbIdents;
            int count = 5;
            do {
                identpoolIdents = identpoolService.getAvailableIdents(mapperFacade.map(kriterium, IdentpoolNewIdentsRequest.class));
                filteredDbIdents = findIdenterNotUsedInDB.filtrer(identpoolIdents);
            } while (identpoolIdents.size() - filteredDbIdents.size() > 0 && --count > 0);
            nyeIdenter.addAll(identpoolIdents);
            if (identpoolIdents.size() < kriterium.getAntall()) {
                throw new TpsfFunctionalException("Ingen ledige identer funnet i miljø.");
            }
        });

        List<Person> personerSomSkalPersisteres = opprettPersonerFraIdenter.execute(nyeIdenter);

        setNameOnPersonsService.execute(personerSomSkalPersisteres);

        return personerSomSkalPersisteres;
    }
}
