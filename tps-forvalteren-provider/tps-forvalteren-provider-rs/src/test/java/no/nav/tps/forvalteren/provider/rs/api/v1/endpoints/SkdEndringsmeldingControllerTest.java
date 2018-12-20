package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingAsText;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsNewSkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsRawMeldinger;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEdnringsmeldingIdListe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingIdListToTps;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.ConvertMeldingFromJsonToText;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateAndSaveSkdEndringsmeldingerFromTextService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateSkdEndringsmeldingFromTypeService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.GetLoggForGruppeService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SendEndringsmeldingToTpsService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SkdEndringsmeldingService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SkdEndringsmeldingsgruppeService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.UpdateSkdEndringsmeldingService;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingGruppeTooLargeException;

@RunWith(MockitoJUnitRunner.class)
public class SkdEndringsmeldingControllerTest {

    @InjectMocks
    private SkdEndringsmeldingController skdEndringsmeldingController;

    @Mock
    private MapperFacade mapper;

    @Mock
    private SkdEndringsmeldingsgruppeService skdEndringsmeldingsgruppeService;

    @Mock
    private SkdEndringsmeldingService skdEndringsmeldingService;

    @Mock
    private CreateSkdEndringsmeldingFromTypeService createSkdEndringsmeldingFromTypeService;

    @Mock
    private CreateAndSaveSkdEndringsmeldingerFromTextService createAndSaveSkdEndringsmeldingerFromTextService;

    @Mock
    private UpdateSkdEndringsmeldingService updateSkdEndringsmeldingService;

    @Mock
    private ConvertMeldingFromJsonToText convertMeldingFromJsonToText;

    @Mock
    private SendEndringsmeldingToTpsService sendEndringsmeldingToTpsService;

    @Mock
    private GetLoggForGruppeService getLoggForGruppeService;

    @Mock
    private List<SkdEndringsmeldingGruppe> grupper;

    @Mock
    private List<RsSkdEndringsmeldingGruppe> rsGrupper;

    @Mock
    private List<RsMeldingstype> rsMeldinger;

    @Test
    public void getGrupperReturnsAllGrupper() {
        when(rsGrupper.size()).thenReturn(1337);
        when(skdEndringsmeldingsgruppeService.findAllGrupper()).thenReturn(grupper);
        when(mapper.mapAsList(grupper, RsSkdEndringsmeldingGruppe.class)).thenReturn(rsGrupper);

        List<RsSkdEndringsmeldingGruppe> result = skdEndringsmeldingController.getGrupper();

        verify(skdEndringsmeldingsgruppeService).findAllGrupper();
        verify(mapper).mapAsList(grupper, RsSkdEndringsmeldingGruppe.class);
        assertThat(result, hasSize(1337));
    }

    @Test
    public void getGruppeReturnsGruppe() {
        SkdEndringsmeldingGruppe gruppe = aSkdEndringsmeldingGruppe().id(1337L).build();
        RsSkdEndringsmeldingGruppe rsGruppe = new RsSkdEndringsmeldingGruppe();

        when(skdEndringsmeldingsgruppeService.findGruppeById(gruppe.getId())).thenReturn(gruppe);
        when(mapper.map(gruppe, RsSkdEndringsmeldingGruppe.class)).thenReturn(rsGruppe);

        RsSkdEndringsmeldingGruppe result = skdEndringsmeldingController.getGruppe(gruppe.getId());

        verify(skdEndringsmeldingsgruppeService).findGruppeById(gruppe.getId());
        verify(mapper).map(gruppe, RsSkdEndringsmeldingGruppe.class);
        assertThat(result, is(rsGruppe));
    }

    @Test
    public void requestForTooManyMessagesShouldCauseException() {
        when(skdEndringsmeldingService.countMeldingerByGruppe(any())).thenReturn(100000);

        try {
            skdEndringsmeldingController.getGruppe(123L);
            fail();
        } catch (SkdEndringsmeldingGruppeTooLargeException e) {
            assertEquals("Kan ikke hente gruppe med flere enn " + 50000 + " meldinger på " +
                    "grunn av minnebegrensninger. Vennligst bruk endepunkt '/gruppe/meldinger/{gruppeId}/{pageNumber}' " +
                    "for å hente meldinger i denne gruppen. Frontend foreløpig ikke implementert for dette endepunktet.", e.getMessage());
        }
    }

    @Test
    public void getGruppePaginertReturnsPaginatedSkdMeldinger() throws IOException {
        Long meldingsId1 = 1234L;
        Long meldingsId2 = 2468L;
        Long gruppeId = 123L;
        List<SkdEndringsmelding> skdEndringsmeldinger = createSkdMeldinger(meldingsId1, meldingsId2);

        when(skdEndringsmeldingService.findSkdEndringsmeldingerOnPage(eq(gruppeId), anyInt())).thenReturn(skdEndringsmeldinger);

        List<RsMeldingstype> meldinger = skdEndringsmeldingController.getGruppePaginert(gruppeId, 0);

        assertEquals(meldingsId1, meldinger.get(0).getId());
        assertEquals(meldingsId2, meldinger.get(1).getId());
    }

    @Test
    public void createGruppeCreatesGruppe() {
        RsSkdEndringsmeldingGruppe rsGruppeParam = new RsSkdEndringsmeldingGruppe();
        SkdEndringsmeldingGruppe gruppe = aSkdEndringsmeldingGruppe().build();

        when(mapper.map(rsGruppeParam, SkdEndringsmeldingGruppe.class)).thenReturn(gruppe);

        skdEndringsmeldingController.createGruppe(rsGruppeParam);

        verify(skdEndringsmeldingsgruppeService).save(gruppe);
    }

    @Test
    public void deleteGruppeDeletesGruppe() {
        Long gruppeId = 1337L;

        skdEndringsmeldingController.deleteGruppe(gruppeId);

        verify(skdEndringsmeldingsgruppeService).deleteGruppeById(gruppeId);
    }

    @Test
    public void createMeldingFromTypeCreatesMelding() {
        Long gruppeId = 1337L;
        RsNewSkdEndringsmelding melding = Mockito.mock(RsNewSkdEndringsmelding.class);

        skdEndringsmeldingController.createMeldingFromMeldingstype(gruppeId, melding);

        verify(createSkdEndringsmeldingFromTypeService).execute(gruppeId, melding);
    }

    @Test
    public void createMeldingerFromText() {
        Long gruppeId = 1337L;
        RsRawMeldinger meldingerAsText = Mockito.mock(RsRawMeldinger.class);

        skdEndringsmeldingController.createMeldingerFromText(gruppeId, meldingerAsText);

        verify(createAndSaveSkdEndringsmeldingerFromTextService).execute(gruppeId, meldingerAsText);
    }

    @Test
    public void deleteMeldinger() {
        RsSkdEdnringsmeldingIdListe rsSkdEdnringsmeldingIdListe = Mockito.mock(RsSkdEdnringsmeldingIdListe.class);

        skdEndringsmeldingController.deleteSkdEndringsmeldinger(rsSkdEdnringsmeldingIdListe);

        verify(skdEndringsmeldingService).deleteById(rsSkdEdnringsmeldingIdListe.getIds());
    }

    @Test
    public void updateMeldingVerify() {
        skdEndringsmeldingController.updateMeldinger(rsMeldinger);

        verify(updateSkdEndringsmeldingService).update(rsMeldinger);
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
        List<Long> ids = new ArrayList<>();
        ids.add(100000000L);
        ids.add(100000001L);
        ids.add(100000002L);

        RsSkdEndringsmeldingIdListToTps skdEndringsmeldingIdListToTps = new RsSkdEndringsmeldingIdListToTps();
        skdEndringsmeldingIdListToTps.setEnvironment(environment);
        skdEndringsmeldingIdListToTps.setIds(ids);

        skdEndringsmeldingController.sendToTps(gruppeId, skdEndringsmeldingIdListToTps);

        verify(sendEndringsmeldingToTpsService).execute(gruppeId, skdEndringsmeldingIdListToTps);
    }

    @Test
    public void getLog() {
        Long gruppeId = 1337L;
        List<SkdEndringsmeldingLogg> log = Arrays.asList(new SkdEndringsmeldingLogg());

        when(getLoggForGruppeService.execute(gruppeId)).thenReturn(log);

        skdEndringsmeldingController.getLogg(gruppeId);

        verify(getLoggForGruppeService).execute(gruppeId);
        verify(mapper).mapAsList(log, RsSkdEndringsmeldingLogg.class);
    }

    private List<SkdEndringsmelding> createSkdMeldinger(Long meldingsId1, Long meldingsId2) {
        return Arrays.asList(
                SkdEndringsmelding.builder().id(meldingsId1).endringsmelding("{\"meldingstype\": \"t1\",\"id\": " + meldingsId1 + "}").build(),
                SkdEndringsmelding.builder().id(meldingsId2).endringsmelding("{\"meldingstype\": \"t1\",\"id\": " + meldingsId2 + "}").build());
    }
}