package no.nav.tps.forvalteren.consumer.ws.kodeverk.mapping;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static no.nav.tps.forvalteren.consumer.ws.kodeverk.mapping.KodeverkTestUtils.createGyldighetsperiode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.EnkeltKodeverk;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.Kode;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.Periode;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.Term;

@RunWith(MockitoJUnitRunner.class)
public class HentKodeverkMappingStrategyTest {

    @Mock
    private EnkeltKodeverk kodeverkMock;

    private MapperFacade mapper;

    @Before
    public void before() {
        DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new HentKodeverkMappingStrategy().register(mapperFactory);
        mapper = mapperFactory.getMapperFacade();

        when(kodeverkMock.getNavn()).thenReturn("navn");
        when(kodeverkMock.getUri()).thenReturn("uri");
        when(kodeverkMock.getVersjonsnummer()).thenReturn("9");

        List<Periode> gyldighet = createGyldighetsperiode(LocalDate.now(), LocalDate.now().plusDays(2));
        when(kodeverkMock.getGyldighetsperiode()).thenReturn(gyldighet);
        when(kodeverkMock.getKode()).thenReturn(Collections.<Kode>emptyList());
    }

    @Test
    public void mapsKodeverk() {
        no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk result = mapper.map(kodeverkMock, no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk.class);

        assertThat(result, is(notNullValue()));
        assertThat(result.getNavn(), is(kodeverkMock.getNavn()));
        assertThat(result.getUri(), is(kodeverkMock.getUri()));
        assertThat(result.getVersjon(), is(9));
        assertThat(result.getFom().toString(), is(LocalDate.now().toString()));
        assertThat(result.getTom().toString(), is(LocalDate.now().plusDays(2).toString()));
        assertThat(result.getKoder(), hasSize(0));
    }

    @Test
    public void mapsKodeWithTerm() {
        Kode kodeMock = mock(Kode.class);
        when(kodeMock.getNavn()).thenReturn("k1Navn");

        Term term1 = createTerm("t1", LocalDate.now(), LocalDate.now().plusDays(2));
        Term term2 = createTerm("t2", LocalDate.now().plusDays(3), LocalDate.now().plusDays(100));
        when(kodeMock.getTerm()).thenReturn(asList(term1, term2));

        when(kodeverkMock.getKode()).thenReturn(singletonList(kodeMock));

        no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk result = mapper.map(kodeverkMock, no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk.class);

        assertThat(result.getKoder(), hasSize(2));
        assertKode(kodeMock, result.getKoder().get(0), term1);
        assertKode(kodeMock, result.getKoder().get(1), term2);
    }

    private void assertKode(Kode kode, no.nav.tps.forvalteren.domain.ws.kodeverk.Kode mappedKode, Term term) {
        assertThat(mappedKode.getNavn(), is(kode.getNavn()));
        assertThat(mappedKode.getUri(), is(kode.getUri()));
        assertThat(mappedKode.getTerm(), is(term.getNavn()));
        assertThat(mappedKode.getFom().toString(), is(new LocalDate(term.getGyldighetsperiode().get(0).getFom().toGregorianCalendar().getTimeInMillis()).toString()));
        assertThat(mappedKode.getTom().toString(), is(new LocalDate(term.getGyldighetsperiode().get(0).getTom().toGregorianCalendar().getTimeInMillis()).toString()));
    }

    private Term createTerm(String term, LocalDate fom, LocalDate tom) {
        Term t = mock(Term.class);
        List<Periode> periode = createGyldighetsperiode(fom, tom);
        when(t.getGyldighetsperiode()).thenReturn(periode);
        when(t.getNavn()).thenReturn(term);
        return t;
    }
}