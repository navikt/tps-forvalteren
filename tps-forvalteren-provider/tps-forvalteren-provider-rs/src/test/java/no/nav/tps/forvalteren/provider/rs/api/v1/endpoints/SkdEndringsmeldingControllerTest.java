package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
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
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SaveSkdEndringsmeldingerService;
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

    @Mock
    private SaveSkdEndringsmeldingerService saveSkdEndringsmeldingerService;

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

    @Test(expected = SkdEndringsmeldingGruppeTooLargeException.class)
    public void requestForTooManyMessagesShouldCauseException() {
        when(skdEndringsmeldingService.countMeldingerByGruppe(any())).thenReturn(100000);
        skdEndringsmeldingController.getGruppe(123L);
    }

    @Test
    public void getGruppePaginertReturnsPaginatedSkdMeldinger() throws IOException {
        Long meldingsId1 = 1234L;
        Long meldingsId2 = 2468L;
        Long gruppeId = 123L;
        List<SkdEndringsmelding> skdEndringsmeldinger = createSkdMeldinger(meldingsId1, meldingsId2);
        List<RsMeldingstype> rsMeldingstypeMeldinger = createRsMeldingstypeMeldinger(meldingsId1, meldingsId2);

        when(skdEndringsmeldingService.findSkdEndringsmeldingerOnPage(eq(gruppeId), anyInt())).thenReturn(skdEndringsmeldinger);
        when(skdEndringsmeldingService.convertSkdEndringsmeldingerToRsMeldingstyper(skdEndringsmeldinger)).thenReturn(rsMeldingstypeMeldinger);

        List<RsMeldingstype> meldinger = skdEndringsmeldingController.getGruppePaginert(gruppeId, 0);

        assertThat(meldinger.get(0).getId(), is(meldingsId1));
        assertThat(meldinger.get(1).getId(), is(meldingsId2));
    }

    @Test
    public void klonAvspillergruppeShouldCreateCloneOfOriginalGruppe() throws IOException {
        String newName = "Some name";
        Long meldingsId1 = 1234L;
        Long meldingsId2 = 2468L;
        SkdEndringsmeldingGruppe originalGruppe = aSkdEndringsmeldingGruppe().id(1337L).build();
        List<SkdEndringsmelding> originalSkdEndringsmeldingerPage1 = createSkdMeldinger(meldingsId1, meldingsId2);
        List<RsMeldingstype> originalRsMeldingstypeMeldinger = createRsMeldingstypeMeldinger(meldingsId1, meldingsId2);
        originalGruppe.setSkdEndringsmeldinger(originalSkdEndringsmeldingerPage1);

        RsSkdEndringsmeldingGruppe newRsSkdEndringsmeldingGruppe = new RsSkdEndringsmeldingGruppe();
        newRsSkdEndringsmeldingGruppe.setBeskrivelse("Klon av gruppe " + originalGruppe.getNavn() + " med id " + originalGruppe.getId());
        newRsSkdEndringsmeldingGruppe.setNavn(newName);

        SkdEndringsmeldingGruppe newSkdEndringsmeldingGruppe = SkdEndringsmeldingGruppe.builder()
                .id(1338L)
                .beskrivelse("Klon av gruppe " + originalGruppe.getNavn() + " med id " + originalGruppe.getId())
                .navn(newName)
                .skdEndringsmeldinger(originalSkdEndringsmeldingerPage1).build();

        when(skdEndringsmeldingsgruppeService.findGruppeById(originalGruppe.getId())).thenReturn(originalGruppe);
        when(skdEndringsmeldingsgruppeService.konfigurerKlonAvGruppe(any(), any())).thenReturn(newRsSkdEndringsmeldingGruppe);
        when(mapper.map(any(), eq(SkdEndringsmeldingGruppe.class))).thenReturn(newSkdEndringsmeldingGruppe);
        when(skdEndringsmeldingService.countMeldingerByGruppe(originalGruppe)).thenReturn(originalGruppe.getSkdEndringsmeldinger().size());
        when(skdEndringsmeldingService.getAntallSiderIGruppe(originalGruppe.getSkdEndringsmeldinger().size())).thenReturn(1);
        when(skdEndringsmeldingService.findSkdEndringsmeldingerOnPage(originalGruppe.getId(), 0)).thenReturn(originalSkdEndringsmeldingerPage1);
        when(skdEndringsmeldingService.convertSkdEndringsmeldingerToRsMeldingstyper(any())).thenReturn(originalRsMeldingstypeMeldinger);

        skdEndringsmeldingController.klonAvspillergruppe(originalGruppe.getId(), newName);

        verify(skdEndringsmeldingsgruppeService).findGruppeById(originalGruppe.getId());
        verify(skdEndringsmeldingService).countMeldingerByGruppe(originalGruppe);
        verify(skdEndringsmeldingService).getAntallSiderIGruppe(originalGruppe.getSkdEndringsmeldinger().size());
        verify(skdEndringsmeldingService).findSkdEndringsmeldingerOnPage(originalGruppe.getId(), 0);
        verify(skdEndringsmeldingService).convertSkdEndringsmeldingerToRsMeldingstyper(any());
        verify(skdEndringsmeldingsgruppeService).save(newSkdEndringsmeldingGruppe);
        verify(saveSkdEndringsmeldingerService).save(any(), eq(newSkdEndringsmeldingGruppe.getId()));
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
    public void deleteStorGruppeDeletesGruppe() {
        Long gruppeId = 1337L;
        int antallMeldingerIGruppe = 100;
        int antallSiderIGruppe = antallMeldingerIGruppe / 10;
        when(skdEndringsmeldingService.countMeldingerByGruppe(any())).thenReturn(antallMeldingerIGruppe);
        when(skdEndringsmeldingService.getAntallSiderIGruppe(antallMeldingerIGruppe)).thenReturn(antallSiderIGruppe);

        skdEndringsmeldingController.deleteStorGruppe(gruppeId);

        verify(skdEndringsmeldingService, times(antallSiderIGruppe)).findSkdEndringsmeldingerOnPage(eq(gruppeId), anyInt());
        verify(skdEndringsmeldingService, times(antallSiderIGruppe)).deleteById(anyListOf(Long.class));
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

    private List<RsMeldingstype> createRsMeldingstypeMeldinger(Long meldingsId1, Long meldingsId2) {
        List<RsMeldingstype> meldinger = new ArrayList<>();
        meldinger.add(RsMeldingstype1Felter.builder().build());
        meldinger.add(RsMeldingstype1Felter.builder().build());
        meldinger.get(0).setId(meldingsId1);
        meldinger.get(1).setId(meldingsId2);
        return meldinger;
    }
}