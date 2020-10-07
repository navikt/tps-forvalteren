package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@RunWith(MockitoJUnitRunner.class)
public class SkdEndringsmeldingsgruppeServiceTest {

    @InjectMocks
    private SkdEndringsmeldingsgruppeService skdEndringsmeldingsgruppeService;

    @Mock
    private SkdEndringsmeldingGruppeRepository endringsmeldingGruppeRepository;

    @Mock
    private SkdEndringsmeldingRepository endringsmeldingRepository;

    @Mock
    private SkdEndringsmeldingGruppe gruppe;

    @Mock
    private MapperFacade mapperFacade;

    @Test
    public void checkThatGruppeGetsSaved() {
        skdEndringsmeldingsgruppeService.save(gruppe);
        verify(endringsmeldingGruppeRepository).save(gruppe);
    }

    @Test
    public void checkThatGruppeGetsDeleted() {
        Long gruppeId = 1337L;

        skdEndringsmeldingsgruppeService.deleteGruppeById(gruppeId);

        verify(endringsmeldingGruppeRepository).deleteById(gruppeId);
    }

    @Test
    public void checkThatCorrectGruppeIsFound() {
        Long gruppeId = 1337L;
        when(endringsmeldingGruppeRepository.findById(gruppeId)).thenReturn(gruppe);

        SkdEndringsmeldingGruppe result = skdEndringsmeldingsgruppeService.findGruppeById(gruppeId);

        verify(endringsmeldingGruppeRepository).findById(gruppeId);
        assertThat(result, is(gruppe));
    }

    @Test
    public void checkThatFindAllGrupperReturnsAllGrupper() {
        List<SkdEndringsmeldingGruppe> grupper = new ArrayList<>();
        grupper.add(aSkdEndringsmeldingGruppe().build());
        grupper.add(aSkdEndringsmeldingGruppe().build());

        when(endringsmeldingGruppeRepository.findAllByOrderByIdAsc()).thenReturn(grupper);
        when(mapperFacade.map(any(SkdEndringsmeldingGruppe.class), eq(RsSkdEndringsmeldingGruppe.class)))
                .thenReturn(new RsSkdEndringsmeldingGruppe());
        List<RsSkdEndringsmeldingGruppe> result = skdEndringsmeldingsgruppeService.findAllGrupper();

        verify(endringsmeldingGruppeRepository).findAllByOrderByIdAsc();
        assertThat(result, hasSize(2));
    }

    @Test
    public void konfigurerKlonAvGruppeShouldSetupCorrectly() {
        SkdEndringsmeldingGruppe originalGruppe = aSkdEndringsmeldingGruppe().id(1337L).build();
        String nyttNavn = "Some name";

        RsSkdEndringsmeldingGruppe newGruppe = skdEndringsmeldingsgruppeService.konfigurerKlonAvGruppe(originalGruppe, nyttNavn);

        assertThat(newGruppe.getBeskrivelse(), is(equalTo("Klon av gruppe " + originalGruppe.getNavn() + " med id " + originalGruppe.getId())));
        assertThat(newGruppe.getNavn(), is(equalTo("Some name")));
    }
}