package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimplePersonRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;

@RunWith(MockitoJUnitRunner.class)
public class ExtractOpprettKriterierTest {

    private static final LocalDateTime FOEDT_ETTER = LocalDateTime.of(1950, 1, 1, 0, 0);
    private static final LocalDateTime FOEDT_FOER = LocalDateTime.of(2018, 12, 18, 0, 0);
    private static final int ANTALL = 100;
    private static final String KJOENN = "M";
    private static final String IDENTTYPE = "DNR";

    @Mock
    private MapperFacade mapperFacade;

    @InjectMocks
    private ExtractOpprettKriterier extractOpprettKriterier;

    @Test
    public void extractMainPersonAllParamsSet() {

        RsPersonKriteriumRequest target = extractOpprettKriterier.extractMainPerson(RsPersonBestillingKriteriumRequest.builder()
                .antall(ANTALL)
                .kjonn(KJOENN)
                .foedtEtter(FOEDT_ETTER)
                .foedtFoer(FOEDT_FOER)
                .identtype(IDENTTYPE)
                .build());

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(ANTALL)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo(KJOENN)));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo(IDENTTYPE)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer(), is(equalTo(FOEDT_FOER)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter(), is(equalTo(FOEDT_ETTER)));
    }

    @Test
    public void extractMainPersonNoParamsSet() {

        RsPersonKriteriumRequest target = extractOpprettKriterier.extractMainPerson(RsPersonBestillingKriteriumRequest.builder()
                .build());

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(1)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo("U")));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo("FNR")));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer(), is(nullValue()));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter(), is(nullValue()));
    }

    @Test
    public void extractPartnerAllParamsSet() {

        RsPersonKriteriumRequest target = extractOpprettKriterier.extractPartner(RsSimplePersonRequest.builder()
                .kjonn(KJOENN)
                .foedtEtter(FOEDT_ETTER)
                .foedtFoer(FOEDT_FOER)
                .identtype(IDENTTYPE)
                .build());

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(1)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo(KJOENN)));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo(IDENTTYPE)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer(), is(equalTo(FOEDT_FOER)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter(), is(equalTo(FOEDT_ETTER)));
    }

    @Test
    public void extractPartnerNoParamsSet() {

        RsPersonKriteriumRequest target = extractOpprettKriterier.extractPartner(RsSimplePersonRequest.builder()
                .build());

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(1)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo("U")));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo("FNR")));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer(), is(nullValue()));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter(), is(nullValue()));
    }

    @Test
    public void extractBarnAllParamsSet() {

        RsPersonKriteriumRequest target = extractOpprettKriterier.extractBarn(singletonList(RsSimplePersonRequest.builder()
                .kjonn(KJOENN)
                .foedtEtter(FOEDT_ETTER)
                .foedtFoer(FOEDT_FOER)
                .identtype(IDENTTYPE)
                .build()));

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(1)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo(KJOENN)));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo(IDENTTYPE)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer(), is(equalTo(FOEDT_FOER)));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter(), is(equalTo(FOEDT_ETTER)));
    }

    @Test
    public void extractBarnNoParamsSet() {

        RsPersonKriteriumRequest target = extractOpprettKriterier.extractBarn(singletonList(RsSimplePersonRequest.builder()
                .build()));

        assertThat(target.getPersonKriterierListe().get(0).getAntall(), is(equalTo(1)));
        assertThat(target.getPersonKriterierListe().get(0).getKjonn(), is(equalTo("U")));
        assertThat(target.getPersonKriterierListe().get(0).getIdenttype(), is(equalTo("FNR")));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtFoer(), is(nullValue()));
        assertThat(target.getPersonKriterierListe().get(0).getFoedtEtter(), is(nullValue()));
    }

    @Test
    public void extractDollyParametere() {

        RsPersonBestillingKriteriumRequest kriterier = RsPersonBestillingKriteriumRequest.builder().build();
        List<Person> hovedperson = singletonList(Person.builder().build());
        List<Person> partner = singletonList(Person.builder().build());
        List<Person> barn = singletonList(Person.builder().build());

        List<Person> personer = extractOpprettKriterier.addExtendedKriterumValuesToPerson(kriterier, hovedperson, partner, barn);
        assertThat(personer, hasSize(3));
    }
}