package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aGruppe;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.DeleteSkdEndringsmeldingGruppeById;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.FindAllSkdEndringsmeldingGrupper;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.FindSkdEndringsmeldingGruppeById;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SaveSkdEndringsmeldingGruppe;

@RunWith(MockitoJUnitRunner.class)
public class SkdEndringsmeldingControllerTest {

    @InjectMocks
    private SkdEndringsmeldingController skdEndringsmeldingController;

    @Mock
    private MapperFacade mapper;

    @Mock
    private FindAllSkdEndringsmeldingGrupper findAllSkdEndringsmeldingGrupper;

    @Mock
    private FindSkdEndringsmeldingGruppeById findSkdEndringsmeldingGruppeById;

    @Mock
    private SaveSkdEndringsmeldingGruppe saveSkdEndringsmeldingGruppe;

    @Mock
    private DeleteSkdEndringsmeldingGruppeById deleteSkdEndringsmeldingGruppeById;

    @Mock
    private List<SkdEndringsmeldingGruppe> grupper;

    @Mock
    private List<RsSkdEndringsmeldingGruppe> rsGrupper;

    @Test
    public void getGrupperReturnsAllGrupper() {
        when(rsGrupper.size()).thenReturn(1337);
        when(findAllSkdEndringsmeldingGrupper.execute()).thenReturn(grupper);
        when(mapper.mapAsList(grupper, RsSkdEndringsmeldingGruppe.class)).thenReturn(rsGrupper);

        List<RsSkdEndringsmeldingGruppe> result = skdEndringsmeldingController.getGrupper();

        verify(findAllSkdEndringsmeldingGrupper).execute();
        verify(mapper).mapAsList(grupper, RsSkdEndringsmeldingGruppe.class);
        assertThat(result, hasSize(1337));
    }

    @Test
    public void getGruppeReturnsGruppe() {
        SkdEndringsmeldingGruppe gruppe = aGruppe().id(1337L).build();
        RsSkdEndringsmeldingGruppe rsGruppe = new RsSkdEndringsmeldingGruppe();

        when(findSkdEndringsmeldingGruppeById.execute(gruppe.getId())).thenReturn(gruppe);
        when(mapper.map(gruppe, RsSkdEndringsmeldingGruppe.class)).thenReturn(rsGruppe);

        RsSkdEndringsmeldingGruppe result = skdEndringsmeldingController.getGruppe(gruppe.getId());

        verify(findSkdEndringsmeldingGruppeById).execute(gruppe.getId());
        verify(mapper).map(gruppe, RsSkdEndringsmeldingGruppe.class);
        assertThat(result, is(rsGruppe));
    }

    @Test
    public void createGruppeCreatesGruppe() {
        RsSkdEndringsmeldingGruppe rsGruppeParam = new RsSkdEndringsmeldingGruppe();
        SkdEndringsmeldingGruppe gruppe = aGruppe().build();

        when(mapper.map(rsGruppeParam, SkdEndringsmeldingGruppe.class)).thenReturn(gruppe);

        skdEndringsmeldingController.createGruppe(rsGruppeParam);

        verify(saveSkdEndringsmeldingGruppe).execute(gruppe);
    }

    @Test
    public void deleteGruppeDeletesGruppe() {
        Long gruppeId = 1337L;

        skdEndringsmeldingController.deleteGruppe(gruppeId);

        verify(deleteSkdEndringsmeldingGruppeById).execute(gruppeId);
    }

}