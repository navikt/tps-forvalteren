package no.nav.tps.forvalteren.service.command.avspiller;

import static java.lang.Long.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspiller;
import no.nav.tps.forvalteren.domain.rs.Meldingsformat;
import no.nav.tps.forvalteren.domain.rs.RsAvspillerRequest;
import no.nav.tps.forvalteren.domain.rs.RsTyperOgKilderResponse;
import no.nav.tps.forvalteren.domain.rs.TypeOppslag;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsDistribusjonsmeldingService;
import no.nav.tps.xjc.ctg.domain.s302.EnkeltMeldingType;
import no.nav.tps.xjc.ctg.domain.s302.EnkeltOversiktHendelseType;
import no.nav.tps.xjc.ctg.domain.s302.EnkeltOversiktKildeType;
import no.nav.tps.xjc.ctg.domain.s302.HendelsedataFraTpsS302;
import no.nav.tps.xjc.ctg.domain.s302.HendelsedataFraTpsS302Type;
import no.nav.tps.xjc.ctg.domain.s302.StatusFraTPSType;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;
import no.nav.tps.xjc.ctg.domain.s302.TpsServiceRutineType;
import no.nav.tps.xjc.ctg.domain.s302.TpsSvarType;

@RunWith(MockitoJUnitRunner.class)
public class AvspillerServiceTest {

    private static final String ENVIRONMENT = "u2";
    private static final Meldingsformat MELDING_FORMAT = Meldingsformat.AJOURHOLDSMELDING;
    private static final String MELDING_NR = "123456789";
    private static final String OVERSIKT_MELDING_1 = "Melding 1";
    private static final String OVERSIKT_KILDE_1 = "Kilde 1";
    private static final String ANTALL_MELDING_1 = "50";
    private static final String ANTALL_KILDE_1 = "100";
    private static final Long TIMEOUT = 1L;

    @Mock
    private TpsDistribusjonsmeldingService tpsDistribusjonsmeldingService;

    @Mock
    private MapperFacade mapperFacade;

    @Mock
    private AvspillerDaoService avspillerDaoService;

    @InjectMocks
    private AvspillerService avspillerService;

    @Before
    public void setup() {
        when(avspillerDaoService.getStatus(anyLong())).thenReturn(new TpsAvspiller());
    }

    @Test
    public void getTyperOgKilder_OK() {

        ArgumentCaptor<TpsPersonData> argumentCaptor = ArgumentCaptor.forClass(TpsPersonData.class);

        when(mapperFacade.map(any(RsAvspillerRequest.class), eq(TpsPersonData.class))).thenReturn(buildTpsPersonData());
        when(tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(any(TpsPersonData.class), eq(ENVIRONMENT), eq(TIMEOUT))).thenReturn(buildTpsPersonData());

        RsTyperOgKilderResponse typerOgKilderResponse = avspillerService.getTyperOgKilder(RsAvspillerRequest.builder()
                .miljoeFra(ENVIRONMENT)
                .timeout(TIMEOUT)
                .build());

        verify(mapperFacade).map(any(RsAvspillerRequest.class), eq(TpsPersonData.class));
        verify(tpsDistribusjonsmeldingService).getDistribusjonsmeldinger(argumentCaptor.capture(), eq(ENVIRONMENT), eq(TIMEOUT));

        assertThat(argumentCaptor.getValue().getTpsServiceRutine().getTypeOppslag(), is(equalTo(TypeOppslag.O.name())));
        assertThat(typerOgKilderResponse.getKilder().get(0).getType(), is(equalTo(OVERSIKT_KILDE_1)));
        assertThat(typerOgKilderResponse.getKilder().get(0).getAntall(), is(equalTo(valueOf(ANTALL_KILDE_1))));
        assertThat(typerOgKilderResponse.getTyper().get(0).getType(), is(equalTo(OVERSIKT_MELDING_1)));
        assertThat(typerOgKilderResponse.getTyper().get(0).getAntall(), is(equalTo(valueOf(ANTALL_MELDING_1))));
    }

    @Test
    public void getMeldinger_OK() {

        ArgumentCaptor<TpsPersonData> argumentCaptor = ArgumentCaptor.forClass(TpsPersonData.class);

        when(mapperFacade.map(any(RsAvspillerRequest.class), eq(TpsPersonData.class))).thenReturn(buildTpsPersonData());
        when(tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(any(TpsPersonData.class), eq(ENVIRONMENT), eq(TIMEOUT))).thenReturn(buildTpsPersonData());

        avspillerService.getMeldinger(RsAvspillerRequest.builder()
                .miljoeFra(ENVIRONMENT)
                .timeout(TIMEOUT)
                .build());

        verify(mapperFacade).map(any(RsAvspillerRequest.class), eq(TpsPersonData.class));
        verify(tpsDistribusjonsmeldingService).getDistribusjonsmeldinger(argumentCaptor.capture(), eq(ENVIRONMENT), eq(TIMEOUT));

        assertThat(argumentCaptor.getValue().getTpsServiceRutine().getTypeOppslag(), is(equalTo(TypeOppslag.L.name())));
    }

    @Test
    public void sendTilTps_OK() {

        ArgumentCaptor<TpsPersonData> argumentCaptor = ArgumentCaptor.forClass(TpsPersonData.class);
        RsAvspillerRequest request = RsAvspillerRequest.builder()
                .miljoeFra(ENVIRONMENT)
                .timeout(TIMEOUT)
                .build();
        TpsAvspiller tpsAvspiller = TpsAvspiller.builder().bestillingId(1L).build();

        when(mapperFacade.map(any(RsAvspillerRequest.class), eq(TpsPersonData.class))).thenReturn(buildTpsPersonData());
        when(tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(any(TpsPersonData.class), eq(ENVIRONMENT), eq(TIMEOUT))).thenReturn(buildTpsPersonData());
        when(avspillerDaoService.save(any(TpsAvspiller.class))).thenReturn(tpsAvspiller);
        when(avspillerDaoService.getStatus(anyLong())).thenReturn(tpsAvspiller);

        avspillerService.sendTilTps(request, tpsAvspiller);

        verify(mapperFacade, times(2)).map(any(RsAvspillerRequest.class), eq(TpsPersonData.class));
        verify(tpsDistribusjonsmeldingService, times(2)).getDistribusjonsmeldinger(argumentCaptor.capture(), eq(ENVIRONMENT), eq(TIMEOUT));
        verify(avspillerDaoService, times(2)).save(any(TpsAvspiller.class));

        assertThat(argumentCaptor.getAllValues().get(1).getTpsServiceRutine().getTypeOppslag(), is(equalTo(TypeOppslag.H.name())));
        assertThat(argumentCaptor.getAllValues().get(1).getTpsServiceRutine().getMeldingNummer(), is(equalTo(MELDING_NR)));
    }

    @Test
    public void showRequest_OK() {

        ArgumentCaptor<TpsPersonData> argumentCaptor = ArgumentCaptor.forClass(TpsPersonData.class);
        when(tpsDistribusjonsmeldingService.getDistribusjonsmeldinger(any(TpsPersonData.class), eq(ENVIRONMENT), eq(TIMEOUT))).thenReturn(buildTpsPersonData());

        avspillerService.showRequest(ENVIRONMENT, MELDING_FORMAT, MELDING_NR);

        verify(tpsDistribusjonsmeldingService).getDistribusjonsmeldinger(argumentCaptor.capture(), eq(ENVIRONMENT), eq(TIMEOUT));

        assertThat(argumentCaptor.getValue().getTpsServiceRutine().getMeldingFormat(), is(equalTo(MELDING_FORMAT.getMeldingFormat())));
        assertThat(argumentCaptor.getValue().getTpsServiceRutine().getMeldingNummer(), is(equalTo(MELDING_NR)));
        assertThat(argumentCaptor.getValue().getTpsServiceRutine().getTypeOppslag(), is(equalTo(TypeOppslag.H.name())));
    }

    private static TpsPersonData buildTpsPersonData() {

        TpsPersonData tpsPersonData = new TpsPersonData();
        tpsPersonData.setTpsServiceRutine(new TpsServiceRutineType());
        tpsPersonData.setTpsSvar(new TpsSvarType());
        tpsPersonData.getTpsSvar().setSvarStatus(new StatusFraTPSType());
        tpsPersonData.getTpsSvar().setHendelseDataS302(new HendelsedataFraTpsS302Type());
        tpsPersonData.getTpsSvar().getHendelseDataS302().setRespons(new HendelsedataFraTpsS302());
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().setMeldingerTotalt("100");
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().setAntallRaderprSide("50");
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().setSideNummer("1");
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().setMelding(new HendelsedataFraTpsS302.Melding());
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().getMelding().getEnmelding().add(new EnkeltMeldingType());
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().getMelding().getEnmelding().get(0).setMNr(MELDING_NR);
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().setOversiktHendelse(new HendelsedataFraTpsS302.OversiktHendelse());
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().getOversiktHendelse().getEnoversiktHendelse().add(new EnkeltOversiktHendelseType());
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().getOversiktHendelse().getEnoversiktHendelse().get(0).setOversiktMeldingType(OVERSIKT_MELDING_1);
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().getOversiktHendelse().getEnoversiktHendelse().get(0).setOversiktMeldingTypeAntall(ANTALL_MELDING_1);
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().setOversiktKilde(new HendelsedataFraTpsS302.OversiktKilde());
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().getOversiktKilde().getEnoversiktKilde().add(new EnkeltOversiktKildeType());
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().getOversiktKilde().getEnoversiktKilde().get(0).setOversiktKildeSystem(OVERSIKT_KILDE_1);
        tpsPersonData.getTpsSvar().getHendelseDataS302().getRespons().getOversiktKilde().getEnoversiktKilde().get(0).setOversiktKildeSystemAntall(ANTALL_KILDE_1);

        return tpsPersonData;
    }
}