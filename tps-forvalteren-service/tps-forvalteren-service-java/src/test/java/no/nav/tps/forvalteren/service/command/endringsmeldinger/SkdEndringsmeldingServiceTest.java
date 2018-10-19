package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
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
    public void shouldReturnereskdmeldingsIdenterMedAngittAarsakskodeOgTransaksjonskode() {
        long gruppeId = 123L;
        List<String> aarsakskoder = Arrays.asList("01", "02");
        String transaksjonstype = "1";
        
        List<SkdEndringsmelding> meldinger = new ArrayList<>();
        final String expectedFoedselsnummer = "21341341234";
        meldinger.add(SkdEndringsmelding.builder().foedselsnummer(expectedFoedselsnummer).build());
        when(skdEndringsmeldingRepository.findByAarsakskodeInAndTransaksjonstypeAndGruppe(eq(aarsakskoder), eq(transaksjonstype), any())).thenReturn(meldinger);
        
        final Set<String> actualFoedselsnumre = skdEndringsmeldingService.filtrerIdenterPaaAarsakskodeOgTransaksjonstype(gruppeId, aarsakskoder, transaksjonstype);
    
        verify(gruppeRepository).findById(gruppeId);
        assertEquals(1,actualFoedselsnumre.size());
        assertTrue(actualFoedselsnumre.contains(expectedFoedselsnummer));
    }
}