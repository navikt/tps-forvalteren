package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
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
        Long meldingsId1 = 1234L;
        Long meldingsId2 = 2468L;

        SkdEndringsmeldingGruppe gruppe = SkdEndringsmeldingGruppe.builder().id(gruppeId).build();

        List<SkdEndringsmelding> skdEndringsmeldinger = Arrays.asList(
                SkdEndringsmelding.builder().id(meldingsId1).build(),
                SkdEndringsmelding.builder().id(meldingsId2).build());

        PageImpl<SkdEndringsmelding> page = new PageImpl<>(skdEndringsmeldinger);

        when(gruppeRepository.findById(gruppeId)).thenReturn(gruppe);
        when(skdEndringsmeldingRepository
                .findAllByGruppe(eq(gruppe), any())).thenReturn(page);

        List<SkdEndringsmelding> endringsmeldinger = skdEndringsmeldingService.findSkdEndringsmeldingerOnPage(gruppeId, 0);

        verify(gruppeRepository).findById(gruppeId);
        verify(skdEndringsmeldingRepository).findAllByGruppe(eq(gruppe), any());
        assertEquals(2, endringsmeldinger.size());
        assertEquals(meldingsId1, endringsmeldinger.get(0).getId());
        assertEquals(meldingsId2, endringsmeldinger.get(1).getId());
    }

    @Test
    public void shouldConvertSkdEndringsmeldingerToRsMeldingstyper() throws IOException {
        Long meldingsId1 = 1234L;
        Long meldingsId2 = 2468L;
        List<SkdEndringsmelding> skdEndringsmeldinger = createSkdMeldinger(meldingsId1, meldingsId2);

        List<RsMeldingstype> meldinger = skdEndringsmeldingService.convertSkdEndringsmeldingerToRsMeldingstyper(skdEndringsmeldinger);

        assertEquals(meldingsId1, meldinger.get(0).getId());
        assertEquals(meldingsId2, meldinger.get(1).getId());
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
        assertEquals(1, actualFoedselsnumre.size());
        assertTrue(actualFoedselsnumre.contains(expectedFoedselsnummer));
    }

    private List<SkdEndringsmelding> createSkdMeldinger(Long meldingsId1, Long meldingsId2) {
        return Arrays.asList(
                SkdEndringsmelding.builder().id(meldingsId1).endringsmelding("{\"meldingstype\": \"t1\",\"id\": " + meldingsId1 + "}").build(),
                SkdEndringsmelding.builder().id(meldingsId2).endringsmelding("{\"meldingstype\": \"t1\",\"id\": " + meldingsId2 + "}").build());
    }
}