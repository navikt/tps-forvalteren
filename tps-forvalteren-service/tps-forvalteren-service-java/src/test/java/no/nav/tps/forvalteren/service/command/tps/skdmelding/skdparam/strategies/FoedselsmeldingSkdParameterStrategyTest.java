package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.FoedselsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.InnvandringUpdateSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonListService;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.anyLong;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FoedselsmeldingSkdParameterStrategyTest {

    @Mock
    private SavePersonListService savePersonListService;

    @Mock
    private RelasjonRepository relasjonRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AdresseRepository adresseRepository;

    @InjectMocks
    private FoedselsmeldingSkdParameterStrategy foedselsmeldingSkdParameterStrategy;

    private Person barn;
    private Person mor;
    private Person far;

    private Relasjon relasjoner;
    private Gateadresse gateadresse;
    private Adresse adresse;
    private SkdMeldingTrans1 result;

    private HashMap<String, String> skdParameters;

    private List<Relasjon> resultRelasjonRepository;

    @Before
    public void setup() {
        barn = new Person();

        barn.setId(0001L);
        barn.setFornavn("MARI");
        barn.setIdent("01011845678");
        barn.setRegdato(LocalDateTime.now());

        mor = new Person();
        mor.setId(0101L);
        mor.setIdent("12128024680");
        mor.setEtternavn("HANSEN");

        far = new Person();
        far.setId(0102L);
        far.setIdent("21027013579");

        gateadresse = new Gateadresse();
        gateadresse.setAdresse("Storgata");
        gateadresse.setHusnummer("2");
        gateadresse.setGatekode("12345");
        gateadresse.setKommunenr("0341");

        resultRelasjonRepository = new ArrayList<>();
        resultRelasjonRepository.add(new Relasjon(0002L, new Person(), mor, "MOR"));
        resultRelasjonRepository.add(new Relasjon(0003L, new Person(), far, "FAR"));

        skdParameters = new HashMap<>();

        when(relasjonRepository.findByPersonId(anyLong())).thenReturn(resultRelasjonRepository);
        when(personRepository.findById(0101L)).thenReturn(mor);
    }

    @Test
    public void isSupportedTest() {
        SkdParametersCreator correctOject = mock(FoedselsmeldingSkdParametere.class);
        SkdParametersCreator incorrectObject = mock(InnvandringUpdateSkdParametere.class);

        assertThat(foedselsmeldingSkdParameterStrategy.isSupported(correctOject), is(true));
        assertThat(foedselsmeldingSkdParameterStrategy.isSupported(incorrectObject), is(false));
    }

    @Test
    public void createCorrectFoedselmeldingParamsFromPerson() {
        when(personRepository.findById(0102L)).thenReturn(far);

        result = foedselsmeldingSkdParameterStrategy.execute(barn);

        assertThat(result.getFodselsdato(), is("010118"));
        assertThat(result.getPersonnummer(), is("45678"));
        assertThat(result.getKjonn(), is("K"));
        assertThat(result.getFarsFodselsdato(), is("210270"));
        assertThat(result.getFarsPersonnummer(), is("13579"));
        assertThat(result.getSlektsnavn(), is("HANSEN"));
        assertThat(result.getForeldreansvar(), is("D"));
    }

    @Test
    public void createFoedselsmeldingParamsWithOnlyOneParent() {
        result = foedselsmeldingSkdParameterStrategy.execute(barn);

        assertThat(result.getFarsFodselsdato(), is(nullValue()));
        assertThat(result.getFarsPersonnummer(), is(nullValue()));
        assertThat(result.getForeldreansvar(), is("M"));

    }

    @Test
    public void createFoedselsmeldingParamsWithGateadresse() {
        gateadresse.setPostnr("1234");

        when(adresseRepository.getAdresseByPersonId(anyLong())).thenReturn(gateadresse);


        result = foedselsmeldingSkdParameterStrategy.execute(barn);

        assertThat(result.getAdressenavn(), is("Storgata"));
        assertThat(result.getHusBruk(), is("2"));
        assertThat(result.getKommunenummer(), is("0341"));
        assertThat(result.getGateGaard(), is("12345"));
        assertThat(result.getAdressetype(), is("O"));
        assertThat(result.getPostnummer(), is("1234"));
    }
}
