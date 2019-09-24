package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.DoedsmeldingHandlingType;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonAdresseService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonstatusService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.xjc.ctg.domain.s004.PersondataFraTpsS004;

@RunWith(MockitoJUnitRunner.class)
public class SendDodsmeldingTilTpsServiceTest {

    private static final String IDENT = "12345678901";
    private static final String MILJOE = "u5";
    private static final String SKDMLD = "Very long unreadable message";
    private static final LocalDateTime DOEDSDATO = LocalDateTime.of(2018, 9, 19, 0, 0);

    @InjectMocks
    private SendDodsmeldingTilTpsService sendDodsmeldingTilTpsService;

    @Mock
    private DeathRowRepository deathRowRepository;

    @Mock
    private SkdMessageCreatorTrans1 skdCreator;

    @Mock
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilMiljoe;

    @Mock
    private SkdMeldingResolver doedsmelding;

    @Mock
    private SkdMeldingResolver doedsmeldingAnnuller;

    @Mock
    private PersonstatusService personstatusService;

    @Mock
    private PersonAdresseService personAdresseService;

    @Mock
    private PersondataFraTpsS004 persondataFraTpsS004;

    @Mock
    private SkdMeldingTrans1 skdMeldingTrans1;

    @Mock
    private TpsSkdRequestMeldingDefinition tpsSkdRequestMeldingDefinition;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void sendDoedsmeldingPersonAlleredeDoed() {
        when(persondataFraTpsS004.getDatoDo()).thenReturn(ConvertDateToString.yyyysMMsdd(DOEDSDATO));
        when(deathRowRepository.findAllByStatus("Ikke sendt")).thenReturn(Collections.singletonList(buildDoedsmelding(DoedsmeldingHandlingType.C)));
        when(personstatusService.hentPersonstatus(anyString(), anyString())).thenReturn(persondataFraTpsS004);

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Personen med ident " + IDENT + " er allerede død i miljø " + MILJOE);
        sendDodsmeldingTilTpsService.execute();
    }

    @Test
    public void sendDoedsmeldingForPersonOk() {
        when(persondataFraTpsS004.getDatoDo()).thenReturn(null);
        when(deathRowRepository.findAllByStatus("Ikke sendt")).thenReturn(Collections.singletonList(buildDoedsmelding(DoedsmeldingHandlingType.C)));
        when(personstatusService.hentPersonstatus(anyString(), anyString())).thenReturn(persondataFraTpsS004);
        when(skdCreator.execute(eq("Dødsmelding"), any(Person.class), eq(true))).thenReturn(skdMeldingTrans1);
        when(skdMeldingTrans1.toString()).thenReturn(SKDMLD);
        when(doedsmelding.resolve()).thenReturn(tpsSkdRequestMeldingDefinition);

        sendDodsmeldingTilTpsService.execute();

        verify(sendSkdMeldingTilMiljoe).execute(eq(SKDMLD), eq(tpsSkdRequestMeldingDefinition), anySet());
        verify(deathRowRepository).save(any(DeathRow.class));
    }

    @Test
    public void sendAnnuleringPersonErIkkeDoed() {
        when(persondataFraTpsS004.getDatoDo()).thenReturn(null);
        when(deathRowRepository.findAllByStatus("Ikke sendt")).thenReturn(Collections.singletonList(buildDoedsmelding(DoedsmeldingHandlingType.D)));
        when(personstatusService.hentPersonstatus(anyString(), anyString())).thenReturn(persondataFraTpsS004);

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Personen med ident " + IDENT + " er ikke død i miljø " + MILJOE);
        sendDodsmeldingTilTpsService.execute();
    }

    @Test
    public void sendAnnuleringForDoedPersonOk() {
        when(persondataFraTpsS004.getDatoDo()).thenReturn(ConvertDateToString.yyyysMMsdd(DOEDSDATO));
        when(deathRowRepository.findAllByStatus("Ikke sendt")).thenReturn(Collections.singletonList(buildDoedsmelding(DoedsmeldingHandlingType.D)));
        when(personstatusService.hentPersonstatus(anyString(), anyString())).thenReturn(persondataFraTpsS004);
        when(skdCreator.execute(eq("DødsmeldingAnnullering"), any(Person.class), eq(true))).thenReturn(skdMeldingTrans1);
        when(skdMeldingTrans1.toString()).thenReturn(SKDMLD);
        when(doedsmeldingAnnuller.resolve()).thenReturn(tpsSkdRequestMeldingDefinition);

        sendDodsmeldingTilTpsService.execute();

        verify(sendSkdMeldingTilMiljoe).execute(eq(SKDMLD), eq(tpsSkdRequestMeldingDefinition), anySet());
        verify(deathRowRepository).save(any(DeathRow.class));
    }

    @Test
    public void sendEndreDoedsdatoPersonErIkkeDoed() {
        when(persondataFraTpsS004.getDatoDo()).thenReturn(null);
        when(deathRowRepository.findAllByStatus("Ikke sendt")).thenReturn(Collections.singletonList(buildDoedsmelding(DoedsmeldingHandlingType.U)));
        when(personstatusService.hentPersonstatus(anyString(), anyString())).thenReturn(persondataFraTpsS004);

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Personen med ident " + IDENT + " er ikke død i miljø " + MILJOE);
        sendDodsmeldingTilTpsService.execute();
    }

    @Test
    public void sendEndreDoedsdatoPersonOk() {
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        Adresse adresse = Gateadresse.builder().build();

        when(persondataFraTpsS004.getDatoDo()).thenReturn(ConvertDateToString.yyyysMMsdd(DOEDSDATO));
        when(deathRowRepository.findAllByStatus("Ikke sendt")).thenReturn(Collections.singletonList(buildDoedsmelding(DoedsmeldingHandlingType.U)));
        when(personstatusService.hentPersonstatus(anyString(), anyString())).thenReturn(persondataFraTpsS004);
        when(skdCreator.execute(eq("DødsmeldingAnnullering"), any(Person.class), eq(true))).thenReturn(skdMeldingTrans1);
        when(skdCreator.execute(eq("Dødsmelding"), any(Person.class), eq(true))).thenReturn(skdMeldingTrans1);
        when(skdMeldingTrans1.toString()).thenReturn(SKDMLD);
        when(doedsmeldingAnnuller.resolve()).thenReturn(tpsSkdRequestMeldingDefinition);
        when(doedsmelding.resolve()).thenReturn(tpsSkdRequestMeldingDefinition);
        when(personAdresseService.hentBoadresseForDato(eq(IDENT), eq(DOEDSDATO.minusDays(1)), eq(MILJOE))).thenReturn(adresse);

        sendDodsmeldingTilTpsService.execute();

        verify(skdCreator).execute(eq("DødsmeldingAnnullering"), personCaptor.capture(), eq(true));
        verify(sendSkdMeldingTilMiljoe, times(2)).execute(eq(SKDMLD), eq(tpsSkdRequestMeldingDefinition), anySet());
        verify(deathRowRepository).save(any(DeathRow.class));
        assertThat(personCaptor.getValue().getIdent(), is(equalTo(IDENT)));
        assertThat(personCaptor.getValue().getBoadresse(), is(equalTo(adresse)));
    }

    private DeathRow buildDoedsmelding(DoedsmeldingHandlingType action) {
        return DeathRow.builder()
                .ident(IDENT)
                .handling(action.name())
                .doedsdato(DOEDSDATO)
                .miljoe(MILJOE)
                .status("Ikke sendt")
                .build();
    }
}