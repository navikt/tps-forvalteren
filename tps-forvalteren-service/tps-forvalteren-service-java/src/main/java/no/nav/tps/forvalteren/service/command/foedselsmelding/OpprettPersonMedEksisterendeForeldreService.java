package no.nav.tps.forvalteren.service.command.foedselsmelding;

import static com.google.common.collect.Lists.newArrayList;
import static no.nav.tps.forvalteren.domain.rs.skd.AddressOrigin.LAGNY;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FAR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.MOR;

import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsFoedselsmeldingRequest;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetRandomAdresseOnPersons;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
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

    public Person execute(RsTpsFoedselsmeldingRequest request) {

        Person person = createPerson(request);

        person.getRelasjoner().add(createRelasjon(MOR, person, request.getIdentMor()));

        if (StringUtils.isNotBlank(request.getIdentFar())) {
            person.getRelasjoner().add(createRelasjon(FAR, person, request.getIdentFar()));
        }

        return person;
    }

    private Person createPerson(RsTpsFoedselsmeldingRequest request) {

        RsPersonKriteriumRequest kriteriumRequest = new RsPersonKriteriumRequest(
                newArrayList(RsPersonKriterier.builder()
                        .antall(1)
                        .identtype(request.getIdenttype() != null ? request.getIdenttype().name() : FNR.name())
                        .kjonn(request.getKjonn() != null ? request.getKjonn().name() : null)
                        .foedtEtter(request.getFoedselsdato())
                        .foedtFoer(request.getFoedselsdato())
                        .build()),
                null);

        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenterFlereMiljoer(kriteriumRequest, request.getMiljoer());
        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personer = opprettPersonerService.execute(identer);
        personNameService.execute(personer);

        return LAGNY == request.getAdresseFra() ? randomAdresseOnPerson.execute(personer, kriteriumRequest.getAdresseNrInfo()).get(0) : personer.get(0);
    }

    private Relasjon createRelasjon(RelasjonType relasjonType, Person hovedperson, String identRelasjon) {

        return Relasjon.builder()
                .person(hovedperson)
                .personRelasjonMed(Person.builder()
                        .ident(identRelasjon)
                        .identtype(hentIdenttypeFraIdentService.execute(identRelasjon))
                        .build()
                )
                .relasjonTypeNavn(relasjonType.name())
                .build();
    }
}
