package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class GetMeldingIdFraGruppeTest {

    private static final Long GRUPPE_ID = 100000000L;

    private static final Long MLD_ID_1 = 10L;
    private static final Long MLD_ID_2 = 12L;

    @InjectMocks
    private GetMeldingIdFraGruppe getMeldingIdFraGruppe;

    @Mock
    private SkdEndringsmeldingGruppeRepository endringsmeldingGruppeRepository;

    @Mock
    private SkdEndringsmeldingGruppe skdEndringsmeldingGruppe;

    private SkdEndringsmelding skdEndringsmelding1;
    private SkdEndringsmelding skdEndringsmelding2;

    @Before
    public void setup() {
        skdEndringsmelding1 = SkdEndringsmelding.builder().id(MLD_ID_1).build();
        skdEndringsmelding2 = SkdEndringsmelding.builder().id(MLD_ID_2).build();
    }

    @Test
    public void hentMeldingerFraGruppeOk() {
        when(endringsmeldingGruppeRepository.findById(GRUPPE_ID)).thenReturn(skdEndringsmeldingGruppe);
        when(skdEndringsmeldingGruppe.getSkdEndringsmeldinger()).thenReturn(Lists.newArrayList(skdEndringsmelding1, skdEndringsmelding2));

        List<Long> result = getMeldingIdFraGruppe.execute(GRUPPE_ID);

        verify(endringsmeldingGruppeRepository).findById(GRUPPE_ID);
        assertThat(result, containsInAnyOrder(MLD_ID_1, MLD_ID_2));
    }

    @Test(expected = NotFoundException.class)
    public void hentMeldingerFraGruppeNotFound() {
        when(endringsmeldingGruppeRepository.findById(GRUPPE_ID)).thenReturn(null);

        getMeldingIdFraGruppe.execute(GRUPPE_ID);

        verify(endringsmeldingGruppeRepository).findById(GRUPPE_ID);
    }
}