package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@RunWith(MockitoJUnitRunner.class)
public class SkdEndringsmeldingServiceTest {

    @InjectMocks
    private SkdEndringsmeldingService skdEndringsmeldingService;

    @Mock
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Mock
    private SkdEndringsmeldingGruppeRepository gruppeRepository;

    @Mock
    private ObjectMapper objectMapperMock;

    private static final Long MELDINGS_ID_1 = 1234L;
    private static final Long MELDINGS_ID_2 = 2468L;

    @Test
    public void verifyRepositoryCall() {
        ArrayList<Long> skdendringsmeldingIds = new ArrayList<>();
        skdEndringsmeldingService.deleteById(skdendringsmeldingIds);
        verify(skdEndringsmeldingRepository).deleteByIdIn(skdendringsmeldingIds);
    }

    @Test
    public void shouldGetCorrectNumberOfPagesGivenAntallMeldingerIGruppe() {
        assertThat(skdEndringsmeldingService.getAntallSiderIGruppe(0), is(0));
        assertThat(skdEndringsmeldingService.getAntallSiderIGruppe(10), is(1));
        assertThat(skdEndringsmeldingService.getAntallSiderIGruppe(11), is(2));
        assertThat(skdEndringsmeldingService.getAntallSiderIGruppe(29), is(3));
    }

    @Test
    public void shouldFindSkdEndringsmeldingOnPage() {
        long gruppeId = 123L;

        List<SkdEndringsmelding> skdEndringsmeldinger = Arrays.asList(
                SkdEndringsmelding.builder().id(MELDINGS_ID_1).build(),
                SkdEndringsmelding.builder().id(MELDINGS_ID_2).build());

        PageImpl<SkdEndringsmelding> page = new PageImpl<>(skdEndringsmeldinger);
        when(skdEndringsmeldingRepository.findAllByGruppeIdOrderByIdAsc(eq(gruppeId), any())).thenReturn(page);

        List<SkdEndringsmelding> endringsmeldinger = skdEndringsmeldingService.findSkdEndringsmeldingerOnPage(gruppeId, 0);

        verify(skdEndringsmeldingRepository).findAllByGruppeIdOrderByIdAsc(eq(gruppeId), ArgumentMatchers.any());
        assertThat(endringsmeldinger.size(), is(equalTo(2)));
        assertThat(endringsmeldinger.get(0).getId(), is(equalTo(MELDINGS_ID_1)));
        assertThat(endringsmeldinger.get(1).getId(), is(equalTo(MELDINGS_ID_2)));
    }

    @Test
    public void shouldConvertSkdEndringsmeldingerToRsMeldingstyper() throws IOException {
        List<SkdEndringsmelding> skdEndringsmeldinger = createSkdMeldinger();

        RsMeldingstype1Felter rsMeldingstype1Felter1 = RsMeldingstype1Felter.builder().build();
        rsMeldingstype1Felter1.setId(MELDINGS_ID_1);
        RsMeldingstype1Felter rsMeldingstype1Felter2 = RsMeldingstype1Felter.builder().build();
        rsMeldingstype1Felter2.setId(MELDINGS_ID_2);

        when(objectMapperMock.readValue(eq(skdEndringsmeldinger.get(0).getEndringsmelding()), eq(RsMeldingstype.class))).thenReturn(rsMeldingstype1Felter1);
        when(objectMapperMock.readValue(eq(skdEndringsmeldinger.get(1).getEndringsmelding()), eq(RsMeldingstype.class))).thenReturn(rsMeldingstype1Felter2);

        List<RsMeldingstype> meldinger = skdEndringsmeldingService.convertSkdEndringsmeldingerToRsMeldingstyper(skdEndringsmeldinger);

        assertThat(meldinger.get(0).getId(), is(equalTo(MELDINGS_ID_1)));
        assertThat(meldinger.get(1).getId(), is(equalTo(MELDINGS_ID_2)));
    }

    @Test
    public void shouldReturnereskdmeldingsIdenterMedAngittAarsakskodeOgTransaksjonskode() {
        long gruppeId = 123L;
        List<String> aarsakskoder = Arrays.asList("01", "02");
        String transaksjonstype = "1";

        final String expectedFoedselsnummer = "21341341234";

        SkdEndringsmeldingGruppe gruppe = SkdEndringsmeldingGruppe.builder().id(gruppeId).build();
        when(gruppeRepository.findById(gruppeId)).thenReturn(gruppe);
        when(skdEndringsmeldingRepository
                .findFoedselsnummerBy(eq(aarsakskoder), eq(transaksjonstype), eq(gruppe))).thenReturn(Arrays.asList(expectedFoedselsnummer));

        final Set<String> actualFoedselsnumre = skdEndringsmeldingService.filtrerIdenterPaaAarsakskodeOgTransaksjonstype(gruppeId, aarsakskoder, transaksjonstype);

        verify(gruppeRepository).findById(gruppeId);
        assertThat(actualFoedselsnumre.size(), is(equalTo(1)));
        assertTrue(actualFoedselsnumre.contains(expectedFoedselsnummer));
    }

    @Test
    public void hentMeldingerFraGruppeOk() {
        long gruppeId = 123L;
        List<SkdEndringsmelding> skdEndringsmeldinger = createSkdMeldinger();

        when(skdEndringsmeldingRepository.findAllIdsBy(any())).thenReturn(Lists.newArrayList(skdEndringsmeldinger.get(0).getId(), skdEndringsmeldinger.get(1).getId()));

        List<Long> result = skdEndringsmeldingService.findAllMeldingIdsInGruppe(gruppeId);

        verify(skdEndringsmeldingRepository).findAllIdsBy(any());
        assertThat(result, containsInAnyOrder(MELDINGS_ID_1, MELDINGS_ID_2));
    }

    @Test
    public void hentMeldingIderMedFnrOk() {
        long gruppeId = 123L;
        String foedselsnummer1 = "11111111111";
        String foedselsnummer2 = "22222222222";
        List<SkdEndringsmelding> skdEndringsmeldinger = createSkdMeldinger();
        skdEndringsmeldinger.get(0).setFoedselsnummer(foedselsnummer1);
        skdEndringsmeldinger.get(1).setFoedselsnummer(foedselsnummer2);
        List<String> identer = Lists.newArrayList(foedselsnummer1);

        when(skdEndringsmeldingRepository.findAllIdsBy(any(), any())).thenReturn(Lists.newArrayList(skdEndringsmeldinger.get(0).getId()));

        List<Long> result = skdEndringsmeldingService.finnAlleMeldingIderMedFoedselsnummer(gruppeId, identer);

        verify(skdEndringsmeldingRepository).findAllIdsBy(any(), any());
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(MELDINGS_ID_1));
    }

    private List<SkdEndringsmelding> createSkdMeldinger() {
        return Arrays.asList(
                SkdEndringsmelding.builder().id(MELDINGS_ID_1).endringsmelding("{\"meldingstype\": \"t1\",\"id\": " + MELDINGS_ID_1 + "}").build(),
                SkdEndringsmelding.builder().id(MELDINGS_ID_2).endringsmelding("{\"meldingstype\": \"t1\",\"id\": " + MELDINGS_ID_2 + "}").build());
    }
}