package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingIdListToTps;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingLoggRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingGruppeNotFoundException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import no.nav.tps.forvalteren.service.command.tps.SkdStartAjourhold;

@RunWith(MockitoJUnitRunner.class)
public class SendEndringsmeldingGruppeToTpsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private MessageProvider messageProvider;

    @Mock
    private ConvertMeldingFromJsonToText convertMeldingFromJsonToText;

    @Mock
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Mock
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;

    @Mock
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Mock
    private ConvertJsonToRsMeldingstype convertJsonToRsMeldingstype;

    @Mock
    private SkdMeldingResolver innvandring;

    @Mock
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;

    @Mock
    private SkdEndringsmeldingLoggRepository skdEndringsmeldingLoggRepository;

    @Mock
    private SkdStartAjourhold skdStartAjourhold;

    @InjectMocks
    private SendEndringsmeldingGruppeToTps sendEndringsmeldingGruppeToTps;

    private static final Long GRUPPE_ID = 1337L;
    private static final String ENVIRONMENT = "u5";

    @Mock
    private SkdEndringsmelding skdEndringsmelding;

    @Mock
    private SkdEndringsmeldingGruppe gruppe;

    @Mock
    private RsMeldingstype1Felter rsMeldingstype1Felter;

    @Mock
    private List<SkdEndringsmelding> skdEndringsmeldinger;

    @Before
    public void setup() {
        when(rsMeldingstype1Felter.getBeskrivelse()).thenReturn("beskrivelse");
        when(skdEndringsmeldingGruppeRepository.findById(GRUPPE_ID)).thenReturn(gruppe);
        when(skdEndringsmeldingRepository.findAllByGruppe(gruppe)).thenReturn(skdEndringsmeldinger);
        when(convertJsonToRsMeldingstype.execute(skdEndringsmelding)).thenReturn(rsMeldingstype1Felter);
        when(innvandring.resolve()).thenReturn(new TpsSkdRequestMeldingDefinition());
        when(skdAddHeaderToSkdMelding.execute(any(StringBuilder.class))).thenReturn(new StringBuilder("lol"));
        when(skdEndringsmeldinger.stream()).thenReturn(Stream.of(skdEndringsmelding));
        when(convertMeldingFromJsonToText.execute(rsMeldingstype1Felter)).thenReturn("skdMelding");
    }

    @Test
    public void checkThatAllServicesGetsCalled() {
        String environment = "u5";
        List<Long> ids = new ArrayList<>();
        ids.add(100000000L);
        ids.add(100000001L);
        ids.add(100000002L);

        when(skdEndringsmeldingRepository.findById(100000000L)).thenReturn(skdEndringsmelding);
        when(skdEndringsmeldingRepository.findById(100000001L)).thenReturn(skdEndringsmelding);
        when(skdEndringsmeldingRepository.findById(100000002L)).thenReturn(skdEndringsmelding);

        RsSkdEndringsmeldingIdListToTps skdEndringsmeldingIdListToTps = new RsSkdEndringsmeldingIdListToTps();
        skdEndringsmeldingIdListToTps.setEnvironment(environment);
        skdEndringsmeldingIdListToTps.setIds(ids);

        sendEndringsmeldingGruppeToTps.execute(GRUPPE_ID, skdEndringsmeldingIdListToTps);

        verify(skdEndringsmeldingGruppeRepository).findById(GRUPPE_ID);
        verify(convertJsonToRsMeldingstype, times(3)).execute(any(SkdEndringsmelding.class));
        verify(innvandring).resolve();

        verify(convertMeldingFromJsonToText, times(3)).execute(rsMeldingstype1Felter);
        verify(skdAddHeaderToSkdMelding, times(3)).execute(any(StringBuilder.class));
        verify(sendSkdMeldingTilGitteMiljoer, times(3)).execute(anyString(), any(TpsSkdRequestMeldingDefinition.class), anySet());

        verify(skdStartAjourhold).execute(anySet());
        verify(skdEndringsmeldingLoggRepository, times(3)).save(any(SkdEndringsmeldingLogg.class));
    }

    @Test
    public void throwsSkdEndringsmeldingGruppeNotFoundException() {
        when(skdEndringsmeldingGruppeRepository.findById(GRUPPE_ID)).thenReturn(null);
        String message = "exception";
        when(messageProvider.get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, GRUPPE_ID)).thenReturn(message);

        expectedException.expect(SkdEndringsmeldingGruppeNotFoundException.class);
        expectedException.expectMessage(message);

        String environment = "u5";
        List<Long> ids = new ArrayList<>();
        ids.add(100000000L);
        ids.add(100000001L);
        ids.add(100000002L);

        RsSkdEndringsmeldingIdListToTps skdEndringsmeldingIdListToTps = new RsSkdEndringsmeldingIdListToTps();
        skdEndringsmeldingIdListToTps.setEnvironment(environment);
        skdEndringsmeldingIdListToTps.setIds(ids);

        sendEndringsmeldingGruppeToTps.execute(GRUPPE_ID, skdEndringsmeldingIdListToTps);

        verify(skdEndringsmeldingGruppeRepository).findById(GRUPPE_ID);
        verify(messageProvider).get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, GRUPPE_ID);
    }
}