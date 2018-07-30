package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.GRUPPE_NOT_FOUND_KEY;
import static no.nav.tps.forvalteren.domain.test.provider.GruppeProvider.aGruppe;
import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateUtvandring;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateVergemaal;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.GRUPPE_NOT_FOUND_KEY;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import static no.nav.tps.forvalteren.domain.test.provider.GruppeProvider.aGruppe;
import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateMeldingWithMeldingstype;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SaveSkdEndringsmeldingerFromText;
import no.nav.tps.forvalteren.service.command.exceptions.GruppeNotFoundException;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateDoedsmeldinger;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateFoedselsmeldinger;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateRelasjoner;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateUtvandring;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateVergemaal;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

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
    private CreateVergemaal createVergemaal;

    @Mock
    private CreateFoedselsmeldinger createFoedselsmeldinger;

    @Mock
    private CreateUtvandring createUtvandring;

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

    private static final String NAVN_INNVANDRINGSMELDING = "InnvandringCreate";
    private static final Long GRUPPE_ID = 1337L;
    private static final boolean ADD_HEADER = false;
    private static final String melding1 = "1", melding2 = "2", melding3 = "3", melding4 = "4";
    private Gruppe testdataGruppe = aGruppe().personer(Collections.emptyList()).build();
    private List<SkdMeldingTrans1> innvandringsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding1).build());
    private List<SkdMelding> relasjonsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding2).build());
    private List<SkdMeldingTrans1> vergemaalsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding4).build());
    private List<SkdMeldingTrans1> doedsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding3).build());
    private SkdEndringsmeldingGruppe skdEndringsmeldingGruppe = aSkdEndringsmeldingGruppe().id(GRUPPE_ID).build();
    private List<SkdMeldingTrans1> utvandringsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding1).build());
    private List<SkdMeldingTrans1> foedselsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding4).build());

    @Before
    public void setup() {
        when(gruppeRepository.findById(GRUPPE_ID)).thenReturn(testdataGruppe);
        when(skdMessageCreatorTrans1.execute(NAVN_INNVANDRINGSMELDING, testdataGruppe.getPersoner(), ADD_HEADER)).thenReturn(innvandringsMeldinger);
        when(createRelasjoner.execute(testdataGruppe.getPersoner(), ADD_HEADER)).thenReturn(relasjonsMeldinger);
        when(createDoedsmeldinger.execute(testdataGruppe.getPersoner(), ADD_HEADER)).thenReturn(doedsMeldinger);
        when(createMeldingWithMeldingstype.execute(anyListOf(SkdMelding.class))).thenReturn(rsMeldinger);
        when(skdEndringsmeldingGruppeRepository.save(any(SkdEndringsmeldingGruppe.class))).thenReturn(skdEndringsmeldingGruppe);
        when(createVergemaal.execute(testdataGruppe.getPersoner(), ADD_HEADER)).thenReturn(vergemaalsMeldinger);
        when(createUtvandring.execute(testdataGruppe.getPersoner(), ADD_HEADER)).thenReturn(utvandringsMeldinger);
        when(createFoedselsmeldinger.execute(testdataGruppe.getPersoner(), ADD_HEADER)).thenReturn(foedselsMeldinger);
    }

    @Test
    public void verifyServices() {
        testdataGruppeToSkdEndringsmeldingGruppe.execute(GRUPPE_ID);

        verify(gruppeRepository).findById(GRUPPE_ID);
        verify(skdMessageCreatorTrans1).execute(NAVN_INNVANDRINGSMELDING, testdataGruppe.getPersoner(), ADD_HEADER);
        verify(createRelasjoner).execute(testdataGruppe.getPersoner(), ADD_HEADER);
        verify(createDoedsmeldinger).execute(testdataGruppe.getPersoner(), ADD_HEADER);
        verify(createMeldingWithMeldingstype).execute(anyListOf(SkdMelding.class));
        verify(skdEndringsmeldingGruppeRepository).save(any(SkdEndringsmeldingGruppe.class));
        verify(createMeldingWithMeldingstype).execute(anyListOf(SkdMelding.class));
        verify(saveSkdEndringsmeldingerFromText).execute(rsMeldinger, GRUPPE_ID);
        verify(createVergemaal).execute(testdataGruppe.getPersoner(), ADD_HEADER);
        verify(createUtvandring).execute(testdataGruppe.getPersoner(), ADD_HEADER);
        verify(createFoedselsmeldinger).execute(testdataGruppe.getPersoner(), ADD_HEADER);

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