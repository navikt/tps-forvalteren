package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingAsText;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsNewSkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsRawMeldinger;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEdnringsmeldingIdListe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.ConvertMeldingFromJsonToText;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateAndSaveSkdEndringsmeldingerFromText;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateSkdEndringsmeldingFromType;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.DeleteSkdEndringsmeldingByIdIn;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.DeleteSkdEndringsmeldingGruppeById;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.FindAllSkdEndringsmeldingGrupper;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.FindSkdEndringsmeldingGruppeById;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.GetLoggForGruppe;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SendEndringsmeldingGruppeToTps;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.UpdateSkdEndringsmelding;
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
    private CreateSkdEndringsmeldingFromType createSkdEndringsmeldingFromType;

    @Mock
    private CreateAndSaveSkdEndringsmeldingerFromText createAndSaveSkdEndringsmeldingerFromText;

    @Mock
    private DeleteSkdEndringsmeldingByIdIn deleteSkdEndringsmeldingByIdIn;

    @Mock
    private UpdateSkdEndringsmelding updateSkdEndringsmelding;
    
    @Mock
    private ConvertMeldingFromJsonToText convertMeldingFromJsonToText;

    @Mock
    private SendEndringsmeldingGruppeToTps sendEndringsmeldingGruppeToTps;

    @Mock
    private GetLoggForGruppe getLoggForGruppe;
    
    @Mock
    private List<SkdEndringsmeldingGruppe> grupper;

    @Mock
    private List<RsSkdEndringsmeldingGruppe> rsGrupper;

    @Mock
    private List<RsMeldingstype> rsMeldinger;
    
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
        SkdEndringsmeldingGruppe gruppe = aSkdEndringsmeldingGruppe().id(1337L).build();
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
        SkdEndringsmeldingGruppe gruppe = aSkdEndringsmeldingGruppe().build();

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

    @Test
    public void createMeldingFromTypeCreatesMelding() {
        Long gruppeId = 1337L;
        RsNewSkdEndringsmelding melding = Mockito.mock(RsNewSkdEndringsmelding.class);

        skdEndringsmeldingController.createMeldingFromMeldingstype(gruppeId, melding);

        verify(createSkdEndringsmeldingFromType).execute(gruppeId, melding);
    }

    @Test
    public void createMeldingerFromText() {
        Long gruppeId = 1337L;
        RsRawMeldinger meldingerAsText = Mockito.mock(RsRawMeldinger.class);

        skdEndringsmeldingController.createMeldingerFromText(gruppeId, meldingerAsText);

        verify(createAndSaveSkdEndringsmeldingerFromText).execute(gruppeId, meldingerAsText);
    }

    @Test
    public void deleteMeldinger() {
        RsSkdEdnringsmeldingIdListe rsSkdEdnringsmeldingIdListe = Mockito.mock(RsSkdEdnringsmeldingIdListe.class);

        skdEndringsmeldingController.deleteSkdEndringsmeldinger(rsSkdEdnringsmeldingIdListe);

        verify(deleteSkdEndringsmeldingByIdIn).execute(rsSkdEdnringsmeldingIdListe.getIds());
    }

    @Test
    public void updateMeldingVerify() {
        skdEndringsmeldingController.updateMeldinger(rsMeldinger);

        verify(updateSkdEndringsmelding).execute(rsMeldinger);
    }

    @Test
    public void convertMelding() {
        RsMeldingstype rsMelding = new RsMeldingstype1Felter();
        String melding = "melding";
        when(convertMeldingFromJsonToText.execute(rsMelding)).thenReturn(melding);
        
        RsMeldingAsText result = skdEndringsmeldingController.convertMelding(rsMelding);

        verify(convertMeldingFromJsonToText).execute(rsMelding);
        assertThat(result.getMelding(), is(melding));
    }
    
    @Test
    public void sendToTps() {
        Long gruppeId = 1337L;
        String environment = "u5";
        
        skdEndringsmeldingController.sendToTps(gruppeId, environment);
        
        verify(sendEndringsmeldingGruppeToTps).execute(gruppeId, environment);
    }
    
    @Test
    public void getLog() {
        Long gruppeId = 1337L;
        List<SkdEndringsmeldingLogg> log = Arrays.asList(new SkdEndringsmeldingLogg());

        when(getLoggForGruppe.execute(gruppeId)).thenReturn(log);

        skdEndringsmeldingController.getLogg(gruppeId);
        
        verify(getLoggForGruppe).execute(gruppeId);
        verify(mapper).mapAsList(log, RsSkdEndringsmeldingLogg.class);
    }

}