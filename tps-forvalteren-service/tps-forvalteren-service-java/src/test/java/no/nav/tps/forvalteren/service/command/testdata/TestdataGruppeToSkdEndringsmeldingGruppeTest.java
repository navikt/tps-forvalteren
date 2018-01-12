package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.GRUPPE_NOT_FOUND_KEY;
import static no.nav.tps.forvalteren.domain.test.provider.GruppeProvider.aGruppe;
import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateMeldingWithMeldingstype;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SaveSkdEndringsmeldingerFromText;
import no.nav.tps.forvalteren.service.command.exceptions.GruppeNotFoundException;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateDoedsmeldinger;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateRelasjoner;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;

@RunWith(MockitoJUnitRunner.class)
public class TestdataGruppeToSkdEndringsmeldingGruppeTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private MessageProvider messageProvider;

    @Mock
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Mock
    private CreateRelasjoner createRelasjoner;

    @Mock
    private CreateDoedsmeldinger createDoedsmeldinger;

    @Mock
    private GruppeRepository gruppeRepository;

    @Mock
    private CreateMeldingWithMeldingstype createMeldingWithMeldingstype;

    @Mock
    private SaveSkdEndringsmeldingerFromText saveSkdEndringsmeldingerFromText;

    @InjectMocks
    private TestdataGruppeToSkdEndringsmeldingGruppe testdataGruppeToSkdEndringsmeldingGruppe;

    @Mock
    private List<RsMeldingstype> rsMeldinger;

    private static final String NAVN_INNVANDRINGSMELDING = "Innvandring";
    private static final Long GRUPPE_ID = 1337L;
    private static final boolean ADD_HEADER = false;
    private static final String melding1 = "1", melding2 = "2", melding3 = "3";
    private Gruppe testdataGruppe = aGruppe().personer(Collections.emptyList()).build();
    private List<String> innvandringsMeldinger = Arrays.asList(melding1);
    private List<String> relasjonsMeldinger = Arrays.asList(melding2);
    private List<String> doedsMeldinger = Arrays.asList(melding3);
    private SkdEndringsmeldingGruppe skdEndringsmeldingGruppe = aSkdEndringsmeldingGruppe().id(GRUPPE_ID).build();

    @Before
    public void setup() {
        when(gruppeRepository.findById(GRUPPE_ID)).thenReturn(testdataGruppe);
        when(skdMessageCreatorTrans1.execute(NAVN_INNVANDRINGSMELDING, testdataGruppe.getPersoner(), ADD_HEADER)).thenReturn(innvandringsMeldinger);
        when(createRelasjoner.execute(testdataGruppe.getPersoner(), ADD_HEADER)).thenReturn(relasjonsMeldinger);
        when(createDoedsmeldinger.execute(GRUPPE_ID, ADD_HEADER)).thenReturn(doedsMeldinger);
        when(createMeldingWithMeldingstype.execute(anyListOf(String.class))).thenReturn(rsMeldinger);
        when(skdEndringsmeldingGruppeRepository.save(any(SkdEndringsmeldingGruppe.class))).thenReturn(skdEndringsmeldingGruppe);
    }

    @Test
    public void verifyServices() {
        testdataGruppeToSkdEndringsmeldingGruppe.execute(GRUPPE_ID);

        verify(gruppeRepository).findById(GRUPPE_ID);
        verify(skdMessageCreatorTrans1).execute(NAVN_INNVANDRINGSMELDING, testdataGruppe.getPersoner(), ADD_HEADER);
        verify(createRelasjoner).execute(testdataGruppe.getPersoner(), ADD_HEADER);
        verify(createDoedsmeldinger).execute(GRUPPE_ID, ADD_HEADER);
        verify(createMeldingWithMeldingstype).execute(anyListOf(String.class));
        verify(skdEndringsmeldingGruppeRepository).save(any(SkdEndringsmeldingGruppe.class));
        verify(createMeldingWithMeldingstype).execute(anyListOf(String.class));
        verify(saveSkdEndringsmeldingerFromText).execute(rsMeldinger, GRUPPE_ID);

    }
    
    @Test
    public void throwsGruppeNotFoundException() {
        String exception = "error";
        when(gruppeRepository.findById(GRUPPE_ID)).thenReturn(null);
        when(messageProvider.get(GRUPPE_NOT_FOUND_KEY, GRUPPE_ID)).thenReturn(exception);
        
        expectedException.expect(GruppeNotFoundException.class);
        expectedException.expectMessage(exception);

        testdataGruppeToSkdEndringsmeldingGruppe.execute(GRUPPE_ID);
        
        verify(messageProvider).get(GRUPPE_NOT_FOUND_KEY, GRUPPE_ID);

    }
}