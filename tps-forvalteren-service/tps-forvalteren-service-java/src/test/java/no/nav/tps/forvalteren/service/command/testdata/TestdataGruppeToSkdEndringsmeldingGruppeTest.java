package no.nav.tps.forvalteren.service.command.testdata;

import static java.util.Collections.emptySet;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.GRUPPE_NOT_FOUND_KEY;
import static no.nav.tps.forvalteren.domain.test.provider.GruppeProvider.aGruppe;
import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateMeldingWithMeldingstypeService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SaveSkdEndringsmeldingerService;
import no.nav.tps.forvalteren.service.command.exceptions.GruppeNotFoundException;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateDoedsmeldinger;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateFoedselsmeldinger;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateRelasjoner;
import no.nav.tps.forvalteren.service.command.testdata.skd.UtvandringOgInnvandring;
import no.nav.tps.forvalteren.service.command.testdata.skd.CreateVergemaal;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;

@RunWith(MockitoJUnitRunner.class)
public class TestdataGruppeToSkdEndringsmeldingGruppeTest {

    private static final String NAVN_INNVANDRINGSMELDING = "InnvandringOpprettingsmelding";
    private static final Long GRUPPE_ID = 1337L;
    private static final boolean ADD_HEADER = false;
    private static final String melding1 = "1", melding2 = "2", melding3 = "3", melding4 = "4";
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
    private UtvandringOgInnvandring utvandringOgInnvandring;
    @Mock
    private GruppeRepository gruppeRepository;
    @Mock
    private CreateMeldingWithMeldingstypeService createMeldingWithMeldingstypeService;
    @Mock
    private SaveSkdEndringsmeldingerService saveSkdEndringsmeldingerService;
    @InjectMocks
    private TestdataGruppeToSkdEndringsmeldingGruppe testdataGruppeToSkdEndringsmeldingGruppe;
    @Mock
    private List<RsMeldingstype> rsMeldinger;
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
        when(createDoedsmeldinger.execute(testdataGruppe.getPersoner(), emptySet(), ADD_HEADER)).thenReturn(doedsMeldinger);
        when(createMeldingWithMeldingstypeService.execute(anyList())).thenReturn(rsMeldinger);
        when(skdEndringsmeldingGruppeRepository.save(any(SkdEndringsmeldingGruppe.class))).thenReturn(skdEndringsmeldingGruppe);
        when(createVergemaal.execute(testdataGruppe.getPersoner(), ADD_HEADER)).thenReturn(vergemaalsMeldinger);
        when(utvandringOgInnvandring.createMeldinger(testdataGruppe.getPersoner(), ADD_HEADER)).thenReturn(utvandringsMeldinger);
        when(createFoedselsmeldinger.executeFromPersons(testdataGruppe.getPersoner(), ADD_HEADER)).thenReturn(foedselsMeldinger);
    }

    @Test
    public void verifyServices() {
        testdataGruppeToSkdEndringsmeldingGruppe.execute(GRUPPE_ID);

        verify(gruppeRepository).findById(GRUPPE_ID);
        verify(skdMessageCreatorTrans1).execute(NAVN_INNVANDRINGSMELDING, testdataGruppe.getPersoner(), ADD_HEADER);
        verify(createRelasjoner).execute(testdataGruppe.getPersoner(), ADD_HEADER);
        verify(createDoedsmeldinger).execute(testdataGruppe.getPersoner(), emptySet(), ADD_HEADER);
        verify(createMeldingWithMeldingstypeService).execute(anyList());
        verify(skdEndringsmeldingGruppeRepository).save(any(SkdEndringsmeldingGruppe.class));
        verify(createMeldingWithMeldingstypeService).execute(anyList());
        verify(saveSkdEndringsmeldingerService).save(rsMeldinger, GRUPPE_ID);
        verify(createVergemaal).execute(testdataGruppe.getPersoner(), ADD_HEADER);
        verify(utvandringOgInnvandring).createMeldinger(testdataGruppe.getPersoner(), ADD_HEADER);
        verify(createFoedselsmeldinger).executeFromPersons(testdataGruppe.getPersoner(), ADD_HEADER);

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