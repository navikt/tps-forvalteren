package no.nav.tps.forvalteren.service.command.testdatamal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Personmal;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonmalRepository;
import no.nav.tps.forvalteren.service.command.testdata.SetGruppeIdAndSavePersonBulkTx;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;

@Service
public class CreateTestdataPerson {

    @Autowired
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Autowired
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Autowired
    private OpprettPersonerService opprettPersoner;

    @Autowired
    private PersonNameService setNameOnPersonsService;

    @Autowired
    private SetGruppeIdAndSavePersonBulkTx setGruppeIdAndSavePersonBulkTx;

    @Autowired
    private SetValuesFromMalOnPersonsService setValuesFromMalOnPersonsService;

    @Autowired
    private PersonmalRepository personmalRepository;

    public void execute(Long gruppeId, RsPersonMalRequest inputPersonRequest) {

        List<Personmal> personmalListe = hentPersonmalListe(inputPersonRequest);
        RsPersonKriteriumRequest personKriteriumRequest = prepKriteriumRequest(personmalListe);

        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(personKriteriumRequest);
        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personerSomSkalPersisteres = opprettPersoner.execute(identer);

        setNameOnPersonsService.execute(personerSomSkalPersisteres);
        setValuesFromMalOnPersonsService.execute(personerSomSkalPersisteres, personmalListe);
        setGruppeIdAndSavePersonBulkTx.execute(personerSomSkalPersisteres, gruppeId);
    }

    private List hentPersonmalListe(RsPersonMalRequest inputPersonRequest) {

        List<Personmal> personmalListe = Lists.newArrayListWithExpectedSize(inputPersonRequest.getPersonmalIdListe().size());

        for (Long id : inputPersonRequest.getPersonmalIdListe()) {
            personmalListe.add(personmalRepository.findById(id));
        }
        return personmalListe;
    }

    public RsPersonKriteriumRequest prepKriteriumRequest(List<Personmal> personmalListe) {
        RsPersonKriteriumRequest rsPersonKriteriumRequest = new RsPersonKriteriumRequest();

        for (Personmal personmal : personmalListe) {
            rsPersonKriteriumRequest.getPersonKriterierListe().add(RsPersonKriterier.builder()
                    .antall(personmal.getAntallIdenter())
                    .foedtEtter(personmal.getFodtEtter())
                    .foedtFoer(personmal.getFodtFor())
                    .kjonn(personmal.getKjonn())
                    .identtype(personmal.getIdentType())
                    .build()
            );
        }

        return rsPersonKriteriumRequest;
    }
}
