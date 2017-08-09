package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligeIMiljo;
import no.nav.tps.forvalteren.service.command.testdata.skd.utils.PersonToSkdParametersMapper;
import no.nav.tps.forvalteren.service.command.tps.SkdMeldingRequest;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetTpsSkdmeldingService;
import no.nav.tps.forvalteren.service.command.vera.GetEnvironments;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SkdCreatePersonerTest {

    private Set<String> identer = new HashSet<>();
    private Person person1, person2;
    Map<String, String> skdParam1, skdParam2;
    Set<String> environments;
    TpsSkdRequestMeldingDefinition skdMeldingDefinition;
    String skdMelding = "skd";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private PersonToSkdParametersMapper personToSkdParametersMapperMock;

    @Mock
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnholdMock;

    @Mock
    private SkdMeldingRequest skdMeldingRequestMock;

    @Mock
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljoMock;

    @Mock
    private GetTpsSkdmeldingService getTpsSkdmeldingServiceMock;

    @Mock
    private GetEnvironments getEnvironmentsCommandMock;

    @Mock
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironmentMock;

    @InjectMocks
    private SkdCreatePersoner skdCreatePersoner;

    @Test
    public void tom(){

    }

    /*h
    @Before
    public void setup(){
        identer.add("en");
        identer.add("to");

        person1 = new Person();
        person2 = new Person();

        person1.setIdent("en");
        person2.setIdent("to");

        skdParam1 = new HashMap<>();
        skdParam2 = new HashMap<>();

        environments = new HashSet<>();
        environments.add("q0");
        environments.add("q1");

        skdMeldingDefinition = new TpsSkdRequestMeldingDefinition();

        when(filtrerPaaIdenterTilgjengeligeIMiljoMock.filtrer(any())).thenReturn(identer);
        when(personToSkdParametersMapperMock.create(any())).thenReturn(skdParam1, skdParam2);
        when(getEnvironmentsCommandMock.getEnvironmentsFromVera(any())).thenReturn(environments);
        when(filterEnvironmentsOnDeployedEnvironmentMock.execute(any())).thenReturn(environments);
        when(getTpsSkdmeldingServiceMock.execute()).thenReturn(Arrays.asList(skdMeldingDefinition));
        when(skdOpprettSkdMeldingMedHeaderOgInnholdMock.execute(any())).thenReturn(skdMelding);
    }

    @Test
    public void happyPath() {
        skdUpdateCreatePersoner.execute(Arrays.asList(person1, person2));
    }

    @Test
    public void hvisIdenterIkkeFinnesIMiljoeSaaKallesCreateParamOgIkkeUpdateSkdParams(){
        skdUpdateCreatePersoner.execute(Arrays.asList(person1, person2));
        verify(personToSkdParametersMapperMock, times(2)).create(any());
    }

    @Test
    public void hvisIdenterFinnesIMiljoeSaaKallesUpdateParamOgIkkeCreateSkdParams(){
        skdUpdateCreatePersoner.execute(Arrays.asList(person1, person2));

        when(filtrerPaaIdenterTilgjengeligeIMiljoMock.filtrer(any())).thenReturn(new HashSet<>());

        verify(personToSkdParametersMapperMock, times(2)).create(any());
    }

    @Test
    public void skdRequestBlirKaltMedAlleMiljoeneSomMaaSjekkes()  throws Exception{

        skdUpdateCreatePersoner.execute(Arrays.asList(person1));

        verify(skdMeldingRequestMock).execute(skdMelding, skdMeldingDefinition, "q0");
        verify(skdMeldingRequestMock).execute(skdMelding, skdMeldingDefinition, "q1");
    }

    @Test
    public void hvisSkdMeldingRequestKastExceptionSaaKasterDenneKlassenException() throws Exception{

        when(skdMeldingRequestMock.execute(any(),any(),any())).thenThrow(new JMSException(any()));

        expectedException.expect(HttpInternalServerErrorException.class);

        skdUpdateCreatePersoner.execute(Arrays.asList(person1, person2));
    }

    @Test
    public void hvisSkdMeldingRequestKastExceptionUnAuthorisedSaaKasterDenneKlassenException() throws Exception{

        when(skdMeldingRequestMock.execute(any(),any(),any())).thenThrow(new HttpForbiddenException("msg","path"));

        expectedException.expect(HttpForbiddenException.class);

        skdUpdateCreatePersoner.execute(Arrays.asList(person1, person2));
    }
    */
}