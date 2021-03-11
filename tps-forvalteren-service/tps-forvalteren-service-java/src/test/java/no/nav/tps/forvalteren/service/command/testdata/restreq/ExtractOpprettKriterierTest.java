package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.domain.rs.RsBarnRequest;
import no.nav.tps.forvalteren.domain.rs.RsForeldreRequest;
import no.nav.tps.forvalteren.domain.rs.RsPartnerRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleRelasjoner;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.skd.KjoennType;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@RunWith(MockitoJUnitRunner.class)
public class ExtractOpprettKriterierTest {

    private static final LocalDateTime FOEDT_ETTER = LocalDateTime.of(1950, 1, 1, 0, 0);
    private static final LocalDateTime FOEDT_FOER = LocalDateTime.of(2018, 12, 18, 0, 0);
    private static final int ANTALL = 100;
    private static final KjoennType KJOENN = KjoennType.M;
    private static final String IDENTTYPE = "DNR";

    @Mock
    private MapperFacade mapperFacade;

    @Mock
    private RandomAdresseService randomAdresseService;

    @Mock
    private DummyAdresseService dummyAdresseService;

    @Mock
    private LandkodeEncoder landkodeEncoder;

    @Mock
    private MidlertidigAdresseMappingService midlertidigAdresseMappingService;

    @Mock
    private HentDatoFraIdentService hentDatoFraIdentService;

    @InjectMocks
    private ExtractOpprettKriterier extractOpprettKriterier;

    @Test
    public void extractMainPersonAllParamsSet() {
        RsPersonBestillingKriteriumRequest bestilling = new RsPersonBestillingKriteriumRequest();
        bestilling.setAntall(ANTALL);
        bestilling.setKjonn(KJOENN);
        bestilling.setFoedtEtter(FOEDT_ETTER);
        bestilling.setFoedtFoer(FOEDT_FOER);
        bestilling.setIdenttype(IDENTTYPE);

        RsPersonKriteriumRequest target = OpprettPersonUtil.extractMainPerson(bestilling);

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(1)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo(KJOENN)));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo(IDENTTYPE)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer(), is(equalTo(FOEDT_FOER)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter(), is(equalTo(FOEDT_ETTER)));
    }

    @Test
    public void extractMainPersonNoParamsSet() {

        RsPersonKriteriumRequest target = OpprettPersonUtil.extractMainPerson(new RsPersonBestillingKriteriumRequest());

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(1)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo(KjoennType.U)));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo("FNR")));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer().format(ISO_LOCAL_DATE),
                greaterThanOrEqualTo(LocalDateTime.now().minusYears(30).format(ISO_LOCAL_DATE)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter().format(ISO_LOCAL_DATE),
                greaterThanOrEqualTo(LocalDateTime.now().minusYears(60).format(ISO_LOCAL_DATE)));
    }

    @Test
    public void extractPartnerAllParamsSet() {

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setKjonn(KJOENN);
        partnerRequest.setFoedtEtter(FOEDT_ETTER);
        partnerRequest.setFoedtFoer(FOEDT_FOER);
        partnerRequest.setIdenttype(IDENTTYPE);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .partnere(singletonList(partnerRequest))
                .build());

        RsPersonKriteriumRequest target = OpprettPersonUtil.extractPartner(request.getRelasjoner().getPartnere(),
                false, false);

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(1)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo(KJOENN)));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo(IDENTTYPE)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer(), is(equalTo(FOEDT_FOER)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter(), is(equalTo(FOEDT_ETTER)));
    }

    @Test
    public void extractPartnerNoParamsSet() {

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .partnere(singletonList(new RsPartnerRequest()))
                .build());

        RsPersonKriteriumRequest target = OpprettPersonUtil.extractPartner(request.getRelasjoner().getPartnere(),
                false, false);

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(1)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo(KjoennType.U)));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo("FNR")));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer().format(ISO_LOCAL_DATE),
                greaterThanOrEqualTo(LocalDateTime.now().minusYears(30).format(ISO_LOCAL_DATE)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter().format(ISO_LOCAL_DATE),
                greaterThanOrEqualTo(LocalDateTime.now().minusYears(60).format(ISO_LOCAL_DATE)));
    }

    @Test
    public void extractBarnAllParamsSet() {

        RsBarnRequest barnRequest = new RsBarnRequest();
        barnRequest.setKjonn(KJOENN);
        barnRequest.setFoedtEtter(FOEDT_ETTER);
        barnRequest.setFoedtFoer(FOEDT_FOER);
        barnRequest.setIdenttype(IDENTTYPE);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .barn(singletonList(barnRequest))
                .build());

        RsPersonKriteriumRequest target = OpprettPersonUtil.extractBarn(request.getRelasjoner().getBarn(),
                false, false);

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(1)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo(KJOENN)));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo(IDENTTYPE)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer(), is(equalTo(FOEDT_FOER)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter(), is(equalTo(FOEDT_ETTER)));
    }

    @Test
    public void extractBarnNoParamsSet() {

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .barn(singletonList(new RsBarnRequest()))
                .build());

        RsPersonKriteriumRequest target = OpprettPersonUtil.extractBarn(request.getRelasjoner().getBarn(),
                false, false);

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(1)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo(KjoennType.U)));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo("FNR")));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer(), is(notNullValue()));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter().format(ISO_LOCAL_DATE),
                greaterThanOrEqualTo(LocalDateTime.now().minusYears(60).format(ISO_LOCAL_DATE)));
    }

    @Test
    public void extractDollyParametere() {

        RsPersonBestillingKriteriumRequest kriterier = new RsPersonBestillingKriteriumRequest();
        kriterier.getRelasjoner().getPartnere().add(new RsPartnerRequest());
        RsBarnRequest barnRequest = new RsBarnRequest();
        barnRequest.setPartnerNr(1);
        kriterier.getRelasjoner().getBarn().add(barnRequest);
        kriterier.getRelasjoner().getForeldre().add(new RsForeldreRequest());
        kriterier.setAdresseNrInfo(new AdresseNrInfo());
        Person hovedperson = Person.builder()
                .statsborgerskap(singletonList(Statsborgerskap.builder().statsborgerskap("FRA").build()))
                .build();
        List<Person> partner = singletonList(Person.builder().build());
        List<Person> barn = singletonList(Person.builder().build());
        List<Person> foreldre = singletonList(Person.builder().build());

        when(randomAdresseService.hentRandomAdresse(anyInt(), any(AdresseNrInfo.class))).thenReturn(singletonList(Gateadresse.builder().build()));

        List<Person> personer = extractOpprettKriterier.addExtendedKriterumValuesToPerson(kriterier, hovedperson, partner, barn, foreldre);
        assertThat(personer, hasSize(4));
    }
}