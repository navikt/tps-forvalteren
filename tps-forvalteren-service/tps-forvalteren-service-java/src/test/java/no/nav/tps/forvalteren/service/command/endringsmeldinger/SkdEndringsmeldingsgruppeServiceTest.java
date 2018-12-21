package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;

@RunWith(MockitoJUnitRunner.class)
public class SkdEndringsmeldingsgruppeServiceTest {

    @InjectMocks
    private SkdEndringsmeldingsgruppeService skdEndringsmeldingsgruppeService;

    @Mock
    private SkdEndringsmeldingGruppeRepository repository;

    @Mock
    private SkdEndringsmeldingGruppe gruppe;

    @Test
    public void checkThatGruppeGetsSaved() {
        skdEndringsmeldingsgruppeService.save(gruppe);
        verify(repository).save(gruppe);
    }

    @Test
    public void checkThatGruppeGetsDeleted() {
        Long gruppeId = 1337L;

        skdEndringsmeldingsgruppeService.deleteGruppeById(gruppeId);

        verify(repository).deleteById(gruppeId);
    }

    @Test
    public void checkThatCorrectGruppeIsFound() {
        Long gruppeId = 1337L;
        when(repository.findById(gruppeId)).thenReturn(gruppe);

        SkdEndringsmeldingGruppe result = skdEndringsmeldingsgruppeService.findGruppeById(gruppeId);

        verify(repository).findById(gruppeId);
        assertThat(result, is(gruppe));
    }

    @Test
    public void checkThatFindAllGrupperReturnsAllGrupper() {
        List<SkdEndringsmeldingGruppe> grupper = new ArrayList<>();
        grupper.add(aSkdEndringsmeldingGruppe().build());
        grupper.add(aSkdEndringsmeldingGruppe().build());

        when(repository.findAllByOrderByIdAsc()).thenReturn(grupper);
        List<SkdEndringsmeldingGruppe> result = skdEndringsmeldingsgruppeService.findAllGrupper();

        verify(repository).findAllByOrderByIdAsc();
        assertThat(result, hasSize(2));
    }

    @Test
    public void konfigurerKlonAvGruppeShouldSetupCorrectly() {
        Long meldingsId1 = 1234L;
        SkdEndringsmeldingGruppe originalGruppe = aSkdEndringsmeldingGruppe().id(1337L).build();
        String nyttNavn = "Some name";
        List<RsMeldingstype> originalRsMeldingstypeMeldinger = createRsMeldingstypeMeldinger(meldingsId1);

        RsSkdEndringsmeldingGruppe newGruppe = skdEndringsmeldingsgruppeService.konfigurerKlonAvGruppe(originalGruppe, nyttNavn, originalRsMeldingstypeMeldinger);

        assertThat(newGruppe.getBeskrivelse(), equalTo("Klon av gruppe " + originalGruppe.getNavn() + " med id " + originalGruppe.getId()));
        assertThat(newGruppe.getNavn(), equalTo("Some name"));
        assertThat(newGruppe.getMeldinger(), equalTo(originalRsMeldingstypeMeldinger));
    }

    private List<RsMeldingstype> createRsMeldingstypeMeldinger(Long meldingsId1) {
        List<RsMeldingstype> meldinger = new ArrayList<>();
        meldinger.add(RsMeldingstype1Felter.builder().build());
        meldinger.get(0).setId(meldingsId1);
        return meldinger;
    }
}