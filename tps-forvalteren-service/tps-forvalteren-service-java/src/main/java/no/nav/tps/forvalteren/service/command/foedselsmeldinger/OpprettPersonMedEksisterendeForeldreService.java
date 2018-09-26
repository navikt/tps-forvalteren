package no.nav.tps.forvalteren.service.command.foedselsmeldinger;

import static no.nav.tps.forvalteren.domain.rs.skd.AddressOrigin.LAGNY;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FAR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.MOR;

import java.util.Arrays;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsFoedselsmelding;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.implementation.SetRandomAdresseOnPersons;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentIdenttypeFraIdentService;

@Service
public class OpprettPersonMedEksisterendeForeldreService {

    @Autowired
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Autowired
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Autowired
    private OpprettPersonerService opprettPersonerService;

    @Autowired
    private PersonNameService personNameService;

    @Autowired
    private SetRandomAdresseOnPersons randomAdresseOnPerson;

    @Autowired
    private HentIdenttypeFraIdentService hentIdenttypeFraIdentService;

    public Person execute(RsTpsFoedselsmelding request) {

        Person person = createPerson(request);

        person.getRelasjoner().add(createRelasjon(MOR, person, request.getIdentMor(), request.getMiljoe()));

        if (StringUtils.isNotBlank(request.getIdentFar())) {
            person.getRelasjoner().add(createRelasjon(FAR, person, request.getIdentFar(), request.getMiljoe()));
        }

        return person;
    }

    private Person createPerson(RsTpsFoedselsmelding request) {

        RsPersonKriteriumRequest kriteriumRequest = new RsPersonKriteriumRequest(
                Arrays.asList(RsPersonKriterier.builder()
                        .antall(1)
                        .identtype(request.getIdenttype())
                        .kjonn(request.getKjonn())
                        .foedtEtter(request.getFoedselsdato().toLocalDate())
                        .foedtFoer(request.getFoedselsdato().toLocalDate())
                        .build()),
                null);

        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(kriteriumRequest);
        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personer = opprettPersonerService.execute(identer);
        personNameService.execute(personer);

        return LAGNY == request.getAdresseFra() ? randomAdresseOnPerson.execute(personer, kriteriumRequest.getAdresseNrInfo()).get(0) : personer.get(0);
    }

    private Relasjon createRelasjon(RelasjonType relasjonType, Person hovedperson, String identRelasjon) {

        return Relasjon.builder()
                .person(Person.builder()
                        .ident(identRelasjon)
                        .identtype(hentIdenttypeFraIdentService.execute(identRelasjon))
                        .build())
                .personRelasjonMed(hovedperson)
                .relasjonTypeNavn(relasjonType.name())
                .build();
    }
}
